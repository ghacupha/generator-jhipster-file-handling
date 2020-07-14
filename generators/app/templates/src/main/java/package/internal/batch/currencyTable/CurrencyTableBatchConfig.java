package <%= packageName %>.internal.batch.currencyTable;

import com.google.common.collect.ImmutableList;
import <%= packageName %>.config.FileUploadsProperties;
import <%= packageName %>.internal.Mapping;
import <%= packageName %>.internal.excel.ExcelFileDeserializer;
import <%= packageName %>.internal.service.BatchService;
import <%= packageName %>.service.<%= classNamesPrefix %>FileUploadService;
// todo replace these entities with entity names from client
import <%= packageName %>.internal.model.sampleDataModel.CurrencyTableEVM;
import <%= packageName %>.service.dto.CurrencyTableDTO;
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

/**
 * This is a sample batch configuration for the currency-table
 */
@Configuration
public class CurrencyTableBatchConfig {


    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ExcelFileDeserializer<CurrencyTableEVM> deserializer;

    @Autowired
    private <%= classNamesPrefix %>FileUploadService fileUploadService;

    @Autowired
    private FileUploadsProperties fileUploadsProperties;

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
            .reader(currencyTableItemReader(fileId))
            .processor(currencyTableItemProcessor())
            .writer(currencyTableItemWriter())
            .build();
        // @formatter:off
    }

    @Bean("currencyTableItemReader")
    @JobScope
    public CurrencyTableListItemReader currencyTableItemReader(@Value("#{jobParameters['fileId']}") long fileId) {

        return new CurrencyTableListItemReader(deserializer, fileUploadService, fileId, fileUploadsProperties);
    }
}
