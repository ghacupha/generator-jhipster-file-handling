package io.github.deposits.app.batch.depositAccount;

import io.github.deposits.app.batch.ListPartition;
import io.github.deposits.app.excel.ExcelFileDeserializer;
import io.github.deposits.app.model.DepositAccountEVM;
import io.github.deposits.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.List;

import static io.github.deposits.app.AppConstants.MAX_DEPOSIT_UPDATE_SIZE;

/**
 * The design idea behind this class is that there is a message which will be received as file-notification which is used to launch the job. The notification contains a file id which is used to
 * retrieve the file required for processing. The processing though involves paging through a list of items read from the file allowing the system to process a chunk at a time. It is assumed that the
 * job launcher process will give at the least an id of the file for processing and this reader proceeds from there.
 */
@Slf4j
@Scope("job")
public class DepositAccountListItemReader implements ItemReader<List<DepositAccountEVM>> {

    private int listPageSize;

    private ExcelFileDeserializer<DepositAccountEVM> deserializer;
    private FileUploadService fileUploadService;
    private long fileId;

    private ListPartition<DepositAccountEVM> currencyTableEVMPartition;

    DepositAccountListItemReader(final ExcelFileDeserializer<DepositAccountEVM> deserializer, FileUploadService fileUploadService, @Value("#{jobParameters['fileId']}") long fileId,
                                 @Value("${reader.deposit.list.size}") int maximumPageSize) {
        this.deserializer = deserializer;
        this.fileUploadService = fileUploadService;
        this.fileId = fileId;

        this.listPageSize = maximumPageSize;
    }

    @PostConstruct
    private void resetIndex() {

        final List<DepositAccountEVM> unProcessedItems =
            deserializer.deserialize(fileUploadService.findOne(fileId).orElseThrow(() -> new IllegalArgumentException(fileId + " was not found in the system")).getDataFile());

        currencyTableEVMPartition = new ListPartition<>(listPageSize, unProcessedItems);

        log.info("Currency table items deserialized : {}", unProcessedItems.size());
    }

    /**
     * {@inheritDoc}
     * <p>
     * Every time this method is called, it will return a List of unprocessed items the size of which is dictated by the maximumPageSize;
     * <p>
     * Once the list of unprocessed items hits zero, the method call will return null;
     * </p>
     */
    @Override
    public List<DepositAccountEVM> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        List<DepositAccountEVM> forProcessing  = currencyTableEVMPartition.getPartition();

        log.info("Returning list of {} items", forProcessing.size());

        return forProcessing.size() == 0 ? null : forProcessing;
    }
}
