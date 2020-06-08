package io.github.deposits.app.batch.depositAccount;

import com.google.common.collect.ImmutableList;
import io.github.deposits.app.Mapping;
import io.github.deposits.app.excel.ExcelFileDeserializer;
import io.github.deposits.app.model.DepositAccountEVM;
import io.github.deposits.app.service.DepositAccountBatchService;
import io.github.deposits.service.FileUploadService;
import io.github.deposits.service.dto.DepositAccountDTO;
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
public class DepositAccountBatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    private Mapping<DepositAccountEVM, DepositAccountDTO> mapping;

    @Autowired
    private DepositAccountBatchService depositAccountBatchService;

    @Autowired
    private ExcelFileDeserializer<DepositAccountEVM> deserializer;

    @Autowired
    private FileUploadService fileUploadService;

    @Value("${reader.deposit.list.size}")
    private static int maximumPageSize;

    @Value("#{jobParameters['fileId']}")
    private static long fileId;

    @Value("#{jobParameters['startUpTime']}")
    private static long startUpTime;

    @Autowired
    private JobExecutionListener persistenceJobListener;


    @Bean("depositAccountPersistenceJob")
    public Job depositAccountPersistenceJob() {
        // @formatter:off
        return jobBuilderFactory.get("depositAccountPersistenceJob")
                                .preventRestart()
                                .listener(persistenceJobListener)
                                .incrementer(new RunIdIncrementer())
                                .flow(readDepositListsFromFileStep())
                                .end()
                                .build();
        // @formatter:on
    }

    @Bean("depositAccountBatchListItemWriter")
    public ItemWriter<List<DepositAccountDTO>> depositAccountBatchListItemWriter() {

        return items -> items.stream().map(depositAccountBatchService::save).forEach(depositAccountBatchService::index);
    }

    @Bean
    public ItemProcessor<List<DepositAccountEVM>, List<DepositAccountDTO>> depositAccountBatchListItemProcessor() {

        return item -> item.stream().map(mapping::toValue2).collect(ImmutableList.toImmutableList());
    }

    @Bean
    public Step readDepositListsFromFileStep() {
        // @formatter:off
        return stepBuilderFactory.get("readDepositListsFromFileStep")
            .<List<DepositAccountEVM>, List<DepositAccountDTO>>chunk(2)
            .reader(depositAccountListItemReader(fileId, maximumPageSize))
            .processor(depositAccountBatchListItemProcessor())
            .writer(depositAccountBatchListItemWriter())
            .build();
        // @formatter:off
    }

    @Bean
    @JobScope
    public DepositAccountListItemReader depositAccountListItemReader(@Value("#{jobParameters['fileId']}") long fileId, @Value("${reader.deposit.list.size}") int maximumPageSize) {

        return new DepositAccountListItemReader(deserializer, fileUploadService, fileId, maximumPageSize);
    }
}
