package io.github.deposits.app.batch.typeTable;

import com.google.common.collect.ImmutableList;
import io.github.deposits.app.Mapping;
import io.github.deposits.app.excel.ExcelFileDeserializer;
import io.github.deposits.app.model.TypeTableEVM;
import io.github.deposits.app.service.BatchService;
import io.github.deposits.service.FileUploadService;
import io.github.deposits.service.dto.TypeTableDTO;
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
public class TypeTableBatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private ExcelFileDeserializer<TypeTableEVM> deserializer;

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
    private BatchService<TypeTableDTO> batchService;

    @Autowired
    private Mapping<TypeTableEVM, TypeTableDTO> mapping;


    @Bean("typeTablePersistenceJob")
    public Job typeTablePersistenceJob() {
        // @formatter:off
        return jobBuilderFactory.get("typeTablePersistenceJob")
                                .preventRestart()
                                .listener(persistenceJobListener)
                                .incrementer(new RunIdIncrementer())
                                .flow(readTypeTableFile())
                                .end()
                                .build();
        // @formatter:on
    }

    @Bean
    public ItemWriter<List<TypeTableDTO>> typeTableItemWriter() {

        // save the items and index in the search engine
        return items -> items.stream().peek(batchService::save).forEach(batchService::index);
    }

    @Bean
    public ItemProcessor<List<TypeTableEVM>, List<TypeTableDTO>> typeTableItemProcessor() {

        return evms -> evms.stream().map(mapping::toValue2).collect(ImmutableList.toImmutableList());
    }

    @Bean
    public Step readTypeTableFile() {
        // @formatter:off
        return stepBuilderFactory.get("readTypeTableFile")
            .<List<TypeTableEVM>, List<TypeTableDTO>>chunk(2)
            .reader(typeTableItemReader(fileId, maximumPageSize))
            .processor(typeTableItemProcessor())
            .writer(typeTableItemWriter())
            .build();
        // @formatter:on
    }

    @Bean
    @JobScope
    public ItemReader<List<TypeTableEVM>> typeTableItemReader(@Value("#{jobParameters['fileId']}") long fileId, @Value("${reader.data_table.list.size}") int maximumPageSize) {

        return new TypeTableItemReader(deserializer, fileUploadService, fileId, maximumPageSize);
    }
}
