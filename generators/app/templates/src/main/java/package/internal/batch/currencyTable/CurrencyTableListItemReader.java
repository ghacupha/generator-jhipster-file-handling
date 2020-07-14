package <%= packageName %>.internal.batch.currencyTable;

import <%= packageName %>.config.FileUploadsProperties;
import <%= packageName %>.internal.batch.ListPartition;
import <%= packageName %>.internal.excel.ExcelFileDeserializer;
import <%= packageName %>.internal.model.sampleDataModel.CurrencyTableEVM;
import <%= packageName %>.service.<%= classNamesPrefix %>FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * This is sample configuration of the currency-table list-item-reader.
 * Take special note of how the listPartition is configured once the object is created at the
 * beginning of a job. This only works because the bean is configured with job-scope.
 */
@Slf4j
@Scope("job")
public class CurrencyTableListItemReader implements ItemReader<List<CurrencyTableEVM>> {

    private final FileUploadsProperties fileUploadsProperties;

    private final ExcelFileDeserializer<CurrencyTableEVM> deserializer;
    private final <%= classNamesPrefix %>FileUploadService fileUploadService;
    private long fileId;

    private ListPartition<CurrencyTableEVM> currencyTableEVMPartition;

    CurrencyTableListItemReader(final ExcelFileDeserializer<CurrencyTableEVM> deserializer, final <%= classNamesPrefix %>FileUploadService fileUploadService, @Value("#{jobParameters['fileId']}") long fileId,
                                final FileUploadsProperties fileUploadsProperties) {
        this.deserializer = deserializer;
        this.fileUploadService = fileUploadService;
        this.fileId = fileId;
        this.fileUploadsProperties = fileUploadsProperties;
    }

    @PostConstruct
    private void resetIndex() {

        final List<CurrencyTableEVM> unProcessedItems =
            deserializer.deserialize(fileUploadService.findOne(fileId).orElseThrow(() -> new IllegalArgumentException(fileId + " was not found in the system")).getDataFile());

        currencyTableEVMPartition = new ListPartition<>(fileUploadsProperties.getListSize(), unProcessedItems);

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
    public List<CurrencyTableEVM> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        List<CurrencyTableEVM> forProcessing = currencyTableEVMPartition.getPartition();

        log.info("Returning list of {} items", forProcessing.size());

        return forProcessing.size() == 0 ? null : forProcessing;
    }
}
