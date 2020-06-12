package <%= packageName %>.internal.messaging.jsonStrings;

import <%= packageName %>.internal.Mapping;
import <%= packageName %>.internal.messaging.platform.MuteListener;
import <%= packageName %>.internal.model.sampleDataModel.CurrencyTableEVM;
import <%= packageName %>.internal.model.sampleDataModel.CurrencyTableDTO;
import <%= packageName %>.internal.model.sampleDataModel.CurrencyTableService;
import com.google.common.collect.ImmutableList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * This is a sample listener for currency-table entity that handles messages from the queue
 * <p>
 * by persisting to the database
 */
@Slf4j
@Transactional
@Service
public class CurrencyTableJsonStringStreamsListener implements MuteListener<StringMessageDTO> {

    private final CurrencyTableService currencyTableService;
    private final Mapping<CurrencyTableEVM, CurrencyTableDTO> currencyTableEVMMapping;

    public CurrencyTableJsonStringStreamsListener(final CurrencyTableService currencyTableService, final Mapping<CurrencyTableEVM, CurrencyTableDTO> currencyTableEVMMapping) {
        this.currencyTableService = currencyTableService;
        this.currencyTableEVMMapping = currencyTableEVMMapping;
    }

    @Override
    @StreamListener(JsonStringStreams.INBOUND)
    public void handleMessage(@Payload StringMessageDTO message) {

        log.info("JSON string list items received for persistence : {}", message);

        List<CurrencyTableEVM> messageQueueData = GsonUtils.stringToList(message.getJsonString(), CurrencyTableEVM[].class);

        List<CurrencyTableDTO> persistedData = messageQueueData.stream().map(currencyTableEVMMapping::toValue2).map(currencyTableService::save).collect(ImmutableList.toImmutableList());

        log.info("{} Items persisted to the sink", persistedData.size());
    }
}
