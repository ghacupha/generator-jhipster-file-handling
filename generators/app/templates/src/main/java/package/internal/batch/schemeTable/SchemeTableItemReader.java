package io.github.deposits.app.batch.schemeTable;

import io.github.deposits.app.batch.ListPartition;
import io.github.deposits.app.excel.ExcelFileDeserializer;
import io.github.deposits.app.model.SchemeTableEVM;
import io.github.deposits.service.FileUploadService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.util.List;

@Slf4j
public class SchemeTableItemReader implements ItemReader<List<SchemeTableEVM>> {


    private int listPageSize;

    private ExcelFileDeserializer<SchemeTableEVM> deserializer;
    private FileUploadService fileUploadService;
    private long fileId;

    private ListPartition<SchemeTableEVM> listPartition;

    SchemeTableItemReader(final ExcelFileDeserializer<SchemeTableEVM> deserializer, FileUploadService fileUploadService, @Value("#{jobParameters['fileId']}") long fileId,
                       @Value("${reader.data_table.list.size}") int maximumPageSize) {
        this.deserializer = deserializer;
        this.fileUploadService = fileUploadService;
        this.fileId = fileId;
        this.listPageSize = maximumPageSize;
    }

    @PostConstruct
    private void resetIndex() {

        final List<SchemeTableEVM> unProcessedItems =
            deserializer.deserialize(fileUploadService.findOne(fileId).orElseThrow(() -> new IllegalArgumentException(fileId + " was not found in the system")).getDataFile());

        listPartition = new ListPartition<>(listPageSize, unProcessedItems);

        log.info("Table items deserialized : {}", unProcessedItems.size());
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
    public List<SchemeTableEVM> read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

        List<SchemeTableEVM> forProcessing = listPartition.getPartition();

        log.info("Returning list of {} items", forProcessing.size());

        return forProcessing.size() == 0 ? null : forProcessing;
    }
}
