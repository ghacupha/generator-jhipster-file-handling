package <%= packageName %>.internal.batch;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.context.annotation.Scope;

/**
 * This class can be used to configure batch related actions before, and after but only within this job
 */
@Slf4j
@Scope("job")
public class PersistenceJobListener implements JobExecutionListener {

    private final long fileId;
    private final long startUpTime;

    public PersistenceJobListener(final long fileId, final long startUpTime) {
        this.fileId = fileId;
        this.startUpTime = startUpTime;
    }

    /**
     * Callback before a job executes.
     *
     * @param jobExecution the current {@link JobExecution}
     */
    @Override
    public void beforeJob(final JobExecution jobExecution) {

        log.info("New job id : {} has started for file id : {}, with start time : {}", jobExecution.getJobId(), fileId, startUpTime);

    }

    /**
     * Callback after completion of a job. Called after both both successful and failed executions. To perform logic on a particular status, use "if (jobExecution.getStatus() == BatchStatus.X)".
     *
     * @param jobExecution the current {@link JobExecution}
     */
    @Override
    public void afterJob(final JobExecution jobExecution) {

        String exitStatus = jobExecution.getExitStatus().getExitCode();

        log.info("Job Id {}, for file-id : {} completed in : {}ms with status {}", jobExecution.getJobId(), fileId, System.currentTimeMillis() - startUpTime, exitStatus);
    }
}
