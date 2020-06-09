package <%= packageName %>.internal.messaging.fileNotification.processors;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// todo loop for every file model type
import static <%= packageName %>.domain.enumeration.<%= classNamesPrefix %>FileModelType.BRANCH_LIST;
import static <%= packageName %>.domain.enumeration.<%= classNamesPrefix %>FileModelType.CURRENCY_LIST;
import static <%= packageName %>.domain.enumeration.<%= classNamesPrefix %>FileModelType.DEPOSIT_LIST;
import static <%= packageName %>.domain.enumeration.<%= classNamesPrefix %>FileModelType.GENERAL_LEDGERS;
import static <%= packageName %>.domain.enumeration.<%= classNamesPrefix %>FileModelType.SBU_LIST;
import static <%= packageName %>.domain.enumeration.<%= classNamesPrefix %>FileModelType.SCHEME_LIST;

/**
 * This object maintains a list of all existing processors. This is a short in the dark about automatically configuring the chain at start up
 */
@Configuration
public class FileUploadProcessorContainer {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("depositAccountPersistenceJob")
    private Job depositAccountPersistenceJob;

    @Autowired
    @Qualifier("currencyTablePersistenceJob")
    private Job currencyTablePersistenceJob;

    @Autowired
    @Qualifier("schemeTablePersistenceJob")
    private Job schemeTablePersistenceJob;

    @Autowired
    @Qualifier("branchTablePersistenceJob")
    private Job branchTablePersistenceJob;

    @Autowired
    @Qualifier("sbuTablePersistenceJob")
    private Job sbuTablePersistenceJob;

    @Autowired
    @Qualifier("typeTablePersistenceJob")
    private Job typeTablePersistenceJob;

    @Bean("fileUploadProcessorChain")
    public FileUploadProcessorChain fileUploadProcessorChain() {

        FileUploadProcessorChain theChain = new FileUploadProcessorChain();

        // Create the chain
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, schemeTablePersistenceJob, SCHEME_LIST));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, branchTablePersistenceJob, BRANCH_LIST));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, sbuTablePersistenceJob, SBU_LIST));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, typeTablePersistenceJob, GENERAL_LEDGERS));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, currencyTablePersistenceJob, CURRENCY_LIST));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, depositAccountPersistenceJob, DEPOSIT_LIST));

        return theChain;
    }

}
