package <%= packageName %>.internal.messaging.jsonStrings;

import com.google.common.collect.ImmutableList;
import <%= packageName %>.internal.Mapping;
import <%= packageName %>.internal.messaging.platform.MuteListener;
import <%= packageName %>.internal.model.DepositAccountEVM;
import <%= packageName %>.service.DepositAccountService;
import <%= packageName %>.service.dto.DepositAccountDTO;
import <%= packageName %>.service.dto.<%= classNamesPrefix %>MessageTokenDTO;
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
