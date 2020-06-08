package io.github.deposits.app.batch.sbuTable;

import com.google.common.collect.ImmutableList;
import io.github.deposits.app.Mapping;
import io.github.deposits.app.excel.ExcelFileDeserializer;
import io.github.deposits.app.model.SBUTableEVM;
import io.github.deposits.app.service.BatchService;
import io.github.deposits.service.FileUploadService;
import io.github.deposits.service.dto.SbuTableDTO;
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
public class SBUTableBatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ExcelFileDeserializer<SBUTableEVM> deserializer;

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
    private BatchService<SbuTableDTO> sbuTableDTOBatchService;

    @Autowired
    private Mapping<SBUTableEVM, SbuTableDTO> mapping;


    @Bean("sbuTablePersistenceJob")
    public Job sbuTablePersistenceJob() {
        // @formatter:off
        return jobBuilderFactory.get("sbuTablePersistenceJob")
                                .preventRestart()
                                .listener(persistenceJobListener)
                                .incrementer(new RunIdIncrementer())
                                .flow(readSBUListFromFile())
                                .end()
                                .build();
        // @formatter:on
    }

    @Bean
    public ItemWriter<List<SbuTableDTO>> sbuTableItemWriter() {

        // save the items and index in the search engine
        return items -> items.stream().peek(sbuTableDTOBatchService::save).forEach(sbuTableDTOBatchService::index);
    }

    @Bean
    public ItemProcessor<List<SBUTableEVM>, List<SbuTableDTO>> sbuTableItemProcessor() {

        return evms -> evms.stream().map(mapping::toValue2).collect(ImmutableList.toImmutableList());
    }

    @Bean
    public Step readSBUListFromFile() {
        // @formatter:off
        return stepBuilderFactory.get("readSBUListFromFile")
            .<List<SBUTableEVM>, List<SbuTableDTO>>chunk(2)
            .reader(sbuTableItemReader(fileId, maximumPageSize))
            .processor(sbuTableItemProcessor())
            .writer(sbuTableItemWriter())
            .build();
        // @formatter:on
    }

    @Bean
    @JobScope
    public ItemReader<List<SBUTableEVM>> sbuTableItemReader(@Value("#{jobParameters['fileId']}") long fileId, @Value("${reader.data_table.list.size}") int maximumPageSize) {

        return new SBUTableItemReader(deserializer, fileUploadService, fileId, maximumPageSize);
    }
}
