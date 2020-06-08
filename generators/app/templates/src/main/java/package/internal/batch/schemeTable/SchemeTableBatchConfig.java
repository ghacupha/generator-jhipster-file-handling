package io.github.deposits.app.batch.schemeTable;

import com.google.common.collect.ImmutableList;
import io.github.deposits.app.Mapping;
import io.github.deposits.app.excel.ExcelFileDeserializer;
import io.github.deposits.app.model.SchemeTableEVM;
import io.github.deposits.app.service.BatchService;
import io.github.deposits.service.FileUploadService;
import io.github.deposits.service.dto.SchemeTableDTO;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SchemeTableBatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ExcelFileDeserializer<SchemeTableEVM> deserializer;

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
    private BatchService<SchemeTableDTO> sbuTableDTOBatchService;

    @Autowired
    private Mapping<SchemeTableEVM, SchemeTableDTO> mapping;


    @Bean("schemeTablePersistenceJob")
    public Job schemeTablePersistenceJob() {
        // @formatter:off
        return jobBuilderFactory.get("schemeTablePersistenceJob")
                                .preventRestart()
                                .listener(persistenceJobListener)
                                .incrementer(new RunIdIncrementer())
                                .flow(readSchemeTableFile())
                                .end()
                                .build();
        // @formatter:on
    }

    @Bean
    public ItemWriter<List<SchemeTableDTO>> schemeTableItemWriter() {

        // save the items and index in the search engine
        return items -> items.stream().peek(sbuTableDTOBatchService::save).forEach(sbuTableDTOBatchService::index);
    }

    @Bean
    public ItemProcessor<List<SchemeTableEVM>, List<SchemeTableDTO>> schemeTableItemProcessor() {

        return evms -> evms.stream().map(mapping::toValue2).collect(ImmutableList.toImmutableList());
    }

    @Bean
    public Step readSchemeTableFile() {
        // @formatter:off
        return stepBuilderFactory.get("readTypeTableFile")
            .<List<SchemeTableEVM>, List<SchemeTableDTO>>chunk(2)
            .reader(schemeTableItemReader(fileId, maximumPageSize))
            .processor(schemeTableItemProcessor())
            .writer(schemeTableItemWriter())
            .build();
        // @formatter:on
    }

    @Bean
    @JobScope
    public ItemReader<List<SchemeTableEVM>> schemeTableItemReader(@Value("#{jobParameters['fileId']}") long fileId, @Value("${reader.data_table.list.size}") int maximumPageSize) {

        return new SchemeTableItemReader(deserializer, fileUploadService, fileId, maximumPageSize);
    }
}
