package <%= packageName %>.internal.fileProcessing;

import <%= packageName %>.internal.model.FileNotification;
import <%= packageName %>.domain.enumeration.<%= classNamesPrefix %>FileModelType;
import <%= packageName %>.service.dto.<%= classNamesPrefix %>FileUploadDTO;
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
public class BatchSupportedFileUploadProcessor implements FileUploadProcessor<<%= classNamesPrefix %>FileUploadDTO>  {


    private final JobLauncher jobLauncher;
    public final Job batchJob;
    private final <%= classNamesPrefix %>FileModelType fileModelType;

    public BatchSupportedFileUploadProcessor(final JobLauncher jobLauncher, final Job batchJob, final <%= classNamesPrefix %>FileModelType fileModelType) {
        this.jobLauncher = jobLauncher;
        this.batchJob = batchJob;
        this.fileModelType = fileModelType;
    }

    @Override
    public <%= classNamesPrefix %>FileUploadDTO processFileUpload(final <%= classNamesPrefix %>FileUploadDTO fileUpload, final FileNotification fileNotification) {

        if (fileNotification.get<%= classNamesPrefix %>fileModelType() == fileModelType) {
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
