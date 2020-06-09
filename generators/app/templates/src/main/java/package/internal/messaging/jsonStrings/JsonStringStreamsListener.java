package <%= packagename %>.internal.messaging.jsonStrings;

import com.google.common.collect.ImmutableList;
import <%= packagename %>.internal.Mapping;
import <%= packagename %>.internal.messaging.platform.MuteListener;
import <%= packagename %>.internal.model.DepositAccountEVM;
import <%= packagename %>.service.DepositAccountService;
import <%= packagename %>.service.dto.DepositAccountDTO;
import <%= packagename %>.service.dto.<%= classNamesPrefix %>MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
@Service
public class JsonStringStreamsListener implements MuteListener<StringMessageDTO> {

    private final DepositAccountService depositAccountService;
    private final Mapping<DepositAccountEVM, DepositAccountDTO> depositAccountDTOMapping;

    public JsonStringStreamsListener(final DepositAccountService depositAccountService, final Mapping<DepositAccountEVM, DepositAccountDTO> depositAccountDTOMapping) {
        this.depositAccountService = depositAccountService;
        this.depositAccountDTOMapping = depositAccountDTOMapping;
    }

    @Override
    @StreamListener(JsonStringStreams.INBOUND)
    public void handleMessage(@Payload StringMessageDTO message) {

        log.info("JSON string list items received for persistence : {}", message);

//        List<DepositAccountEVM> messageQueueData = GsonUtils.stringToList(message.getJsonString(), DepositAccountEVM[].class);
//
//        List<DepositAccountDTO> persistedData =
//            messageQueueData.stream().map(depositAccountDTOMapping::toValue2).map(depositAccountService::save).collect(ImmutableList.toImmutableList());
//
//        log.info("{} Items persisted to the sink", persistedData.size());
    }
}
