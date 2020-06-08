package io.github.deposits.app.batch.currencyTable;

import io.github.deposits.app.batch.ListPartition;
import io.github.deposits.app.excel.ExcelFileDeserializer;
import io.github.deposits.app.model.CurrencyTableEVM;
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

@Slf4j
@Scope("job")
public class CurrencyTableListItemReader implements ItemReader<List<CurrencyTableEVM>> {

    private int listPageSize;

    private ExcelFileDeserializer<CurrencyTableEVM> deserializer;
    private FileUploadService fileUploadService;
    private long fileId;

    private ListPartition<CurrencyTableEVM> currencyTableEVMPartition;

    CurrencyTableListItemReader(final ExcelFileDeserializer<CurrencyTableEVM> deserializer, FileUploadService fileUploadService, @Value("#{jobParameters['fileId']}") long fileId,
                                @Value("${reader.data_table.list.size}") int maximumPageSize) {
        this.deserializer = deserializer;
        this.fileUploadService = fileUploadService;
        this.fileId = fileId;

        this.listPageSize = maximumPageSize;
    }

    @PostConstruct
    private void resetIndex() {

        final List<CurrencyTableEVM> unProcessedItems =
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
    public List<CurrencyTableEVM> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        List<CurrencyTableEVM> forProcessing = currencyTableEVMPartition.getPartition();

        log.info("Returning list of {} items", forProcessing.size());

        return forProcessing.size() == 0 ? null : forProcessing;
    }
}
