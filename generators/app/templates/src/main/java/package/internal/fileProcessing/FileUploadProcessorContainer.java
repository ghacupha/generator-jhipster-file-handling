package <%= packageName %>.internal.fileProcessing;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// todo loop for every file model type
import static <%= packageName %>.domain.enumeration.<%= classNamesPrefix %>FileModelType.CURRENCY_LIST;

/**
 * This object maintains a list of all existing processors. This is a short in the dark about automatically configuring the chain at start up
 */
@Configuration
public class FileUploadProcessorContainer {

    @Autowired
    private JobLauncher jobLauncher;

    // todo auto-wire each job for each data model type
    @Autowired
    @Qualifier("currencyTablePersistenceJob")
    private Job currencyTablePersistenceJob;

    @Bean("fileUploadProcessorChain")
    public FileUploadProcessorChain fileUploadProcessorChain() {

        FileUploadProcessorChain theChain = new FileUploadProcessorChain();

        // Create the chain, each should match against it's specific key of data-model type
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, currencyTablePersistenceJob, CURRENCY_LIST));

        // TODO Add processors for each data model type
        //theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, schemeTablePersistenceJob, SCHEME_LIST));
        //theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, branchTablePersistenceJob, BRANCH_LIST));
        //theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, sbuTablePersistenceJob, SBU_LIST));
        //theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, typeTablePersistenceJob, GENERAL_LEDGERS));
        //theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, depositAccountPersistenceJob, DEPOSIT_LIST));

        return theChain;
    }

}
