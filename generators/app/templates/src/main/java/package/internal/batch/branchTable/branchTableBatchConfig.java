package io.github.deposits.app.batch.branchTable;

import io.github.deposits.app.Mapping;
import io.github.deposits.app.excel.ExcelFileDeserializer;
import io.github.deposits.app.model.BranchTableEVM;
import io.github.deposits.app.service.BatchService;
import io.github.deposits.service.FileUploadService;
import io.github.deposits.service.dto.BranchTableDTO;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Configuration
public class branchTableBatchConfig {

    @Autowired
    private Mapping<BranchTableEVM, BranchTableDTO> mapping;

    @Autowired
    private BatchService<BranchTableDTO> batchService;

    @Autowired
    private ExcelFileDeserializer<BranchTableEVM> deserializer;


    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

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


    @Bean("branchTablePersistenceJob")
    public Job branchTablePersistenceJob() {
        // @formatter:off
        return jobBuilderFactory.get("branchTablePersistenceJob")
                                .preventRestart()
                                .listener(persistenceJobListener)
                                .incrementer(new RunIdIncrementer())
                                .flow(readBranchTableListsStep())
                                .end()
                                .build();
        // @formatter:on
    }

    @Bean("branchTableListWriter")
    public ItemWriter<List<BranchTableDTO>> branchTableListWriter() {

        return items -> items.stream().peek(batchService::save).forEach(batchService::index);
    }

    @Bean("branchTableListProcessor")
    public ItemProcessor<List<BranchTableEVM>, List<BranchTableDTO>> branchTableListProcessor() {

        return item -> mapping.toValue2(item);
    }

    @Bean("readBranchTableListsStep")
    public Step readBranchTableListsStep() {
        // @formatter:off
        return stepBuilderFactory.get("readBranchTableListsStep")
            .<List<BranchTableEVM>, List<BranchTableDTO>>chunk(2)
            .reader(branchTableListReader(fileId, maximumPageSize))
            .processor(branchTableListProcessor())
            .writer(branchTableListWriter())
            .build();
        // @formatter:off
    }

    @Bean
    @JobScope
    public BranchTableListReader branchTableListReader(@Value("#{jobParameters['fileId']}") long fileId, @Value("${reader.data_table.list.size}") int maximumPageSize) {

        return new BranchTableListReader(deserializer, fileUploadService, fileId, maximumPageSize);
    }
}
