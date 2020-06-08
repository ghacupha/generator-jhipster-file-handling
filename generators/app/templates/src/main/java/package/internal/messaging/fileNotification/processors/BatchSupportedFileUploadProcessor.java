package io.github.deposits.app.messaging.fileNotification.processors;

import io.github.deposits.app.messaging.fileNotification.FileNotification;
import io.github.deposits.domain.enumeration.FileModelType;
import io.github.deposits.service.dto.FileUploadDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;

/**
 * This class is create to enable code reuse where the only parameter that changes the file-model-type
 */
@Slf4j
public class BatchSupportedFileUploadProcessor implements FileUploadProcessor<FileUploadDTO>  {


    private final JobLauncher jobLauncher;
    public final Job batchJob;
    private final FileModelType fileModelType;

    public BatchSupportedFileUploadProcessor(final JobLauncher jobLauncher, final Job batchJob, final FileModelType fileModelType) {
        this.jobLauncher = jobLauncher;
        this.batchJob = batchJob;
        this.fileModelType = fileModelType;
    }

    @Override
    public FileUploadDTO processFileUpload(final FileUploadDTO fileUpload, final FileNotification fileNotification) {

        if (fileNotification.getFileModelType() == fileModelType) {
            log.debug("File-upload type confirmed commencing process...");

            JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
            jobParametersBuilder.addLong("fileId", fileUpload.getId());
            jobParametersBuilder.addLong("startUpTime", fileNotification.getTimestamp());
            jobParametersBuilder.addString("messageToken", fileNotification.getMessageToken());

            try {
                jobLauncher.run(batchJob, jobParametersBuilder.toJobParameters());
            } catch (JobExecutionAlreadyRunningException | JobRestartException | JobParametersInvalidException | JobInstanceAlreadyCompleteException e) {
                e.printStackTrace();
            }
        } else {

            log.debug("File upload inconsistent with the data model supported by this processor");
        }

        return fileUpload;
    }
}
