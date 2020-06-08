package io.github.deposits.app.batch.currencyTable;

import com.google.common.collect.ImmutableList;
import io.github.deposits.app.Mapping;
import io.github.deposits.app.excel.ExcelFileDeserializer;
import io.github.deposits.app.model.CurrencyTableEVM;
import io.github.deposits.app.service.BatchService;
import io.github.deposits.service.FileUploadService;
import io.github.deposits.service.dto.CurrencyTableDTO;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CurrencyTableBatchConfig {


    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ExcelFileDeserializer<CurrencyTableEVM> deserializer;

    @Autowired
    private FileUploadService fileUploadService;

    @Value("${reader.data_table.list.size}")
    private static int maximumPageSize;

    @Value("#{jobParameters['fileId']}")
    private static long fileId;

    @Value("#{jobParameters['startUpTime']}")
    private static long startUpTime;

    @Autowired
    private JobExecutionListener persistenceJobListener;

    @Autowired
    private BatchService<CurrencyTableDTO> currencyTableDTOBatchService;

    @Autowired
    private Mapping<CurrencyTableEVM, CurrencyTableDTO> mapping;


    @Bean("currencyTablePersistenceJob")
    public Job currencyTablePersistenceJob() {
        // @formatter:off
        return jobBuilderFactory.get("currencyTablePersistenceJob")
                                .preventRestart()
                                .listener(persistenceJobListener)
                                .incrementer(new RunIdIncrementer())
                                .flow(readCurrencyListFromFile())
                                .end()
                                .build();
        // @formatter:on
    }

    @Bean
    public ItemWriter<List<CurrencyTableDTO>> currencyTableItemWriter() {

        return items -> items.stream().peek(currencyTableDTOBatchService::save).forEach(currencyTableDTOBatchService::index);
    }

    @Bean
    public ItemProcessor<List<CurrencyTableEVM>, List<CurrencyTableDTO>> currencyTableItemProcessor() {

        return evms -> evms.stream().map(mapping::toValue2).collect(ImmutableList.toImmutableList());
    }

    @Bean("readCurrencyListFromFile")
    public Step readCurrencyListFromFile() {
        // @formatter:off
        return stepBuilderFactory.get("readCurrencyListFromFile")
            .<List<CurrencyTableEVM>, List<CurrencyTableDTO>>chunk(2)
            .reader(currencyTableItemReader(fileId, maximumPageSize))
            .processor(currencyTableItemProcessor())
            .writer(currencyTableItemWriter())
            .build();
        // @formatter:off
    }

    @Bean("currencyTableItemReader")
    @JobScope
    public CurrencyTableListItemReader currencyTableItemReader(@Value("#{jobParameters['fileId']}") long fileId, @Value("${reader.data_table.list.size}") int maximumPageSize) {

        return new CurrencyTableListItemReader(deserializer, fileUploadService, fileId, maximumPageSize);
    }
}
