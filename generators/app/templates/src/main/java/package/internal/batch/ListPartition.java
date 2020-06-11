package <%= packageName %>.internal.batch;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is for handling bits as sub-lists and for batches should have a job-scope
 * <p>
 * The object is initialized once for every list that is being processed and should exist
 * <p>
 * the entire time the list if still in progress. If a new one is initialized it will cause
 * <p>
 * trouble
 */
@Slf4j
public class ListPartition<T> {

    private final int dataSize;

    private final int listPageSize;
    private final List<T> unProcessedItems;

    private int listPageNumber;
    private int processedDataSize;

    public ListPartition(final int listPageSize, final List<T> unProcessedItems) {
        this.listPageSize = listPageSize;
        this.unProcessedItems = unProcessedItems;
        this.dataSize = unProcessedItems.size();
        this.listPageNumber = -1;
        this.processedDataSize = 0;
    }



    public List<T> getPartition() {

        List<T> forProcessing = new ArrayList<>();

        if (dataSize > processedDataSize) {

            listPageNumber++;

            log.info("Getting page number : {}", listPageNumber);

            try {
                forProcessing = ImmutableList.copyOf(Lists.partition(unProcessedItems, listPageSize).get(listPageNumber));
            } catch (Exception e) {
                // ? DO NOTHING?
                // An error is quietly thrown, making the forProcessing list null
            }

            // TODO WHY THIS DIDN'T WORK processedDataSize =+ forProcessing.size();
            processedDataSize = processedDataSize + forProcessing.size();

            log.info("{} items enqueued for processing", processedDataSize);
        } else {

            forProcessing = ImmutableList.of();
        }
        return forProcessing;
    }
}
