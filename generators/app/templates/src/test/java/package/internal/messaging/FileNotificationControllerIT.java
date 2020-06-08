package io.github.deposits.app.messaging;

import io.github.deposits.DepositAnalysisMainApp;
import io.github.deposits.app.messaging.fileNotification.FileNotification;
import io.github.deposits.app.messaging.fileNotification.FileNotificationStreams;
import io.github.deposits.app.messaging.platform.MessageService;
import io.github.deposits.app.messaging.platform.TokenizableMessage;
import io.github.deposits.app.util.TokenGenerator;
import io.github.deposits.service.dto.MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;

import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = {DepositAnalysisMainApp.class})
public class FileNotificationControllerIT {


    @Autowired
    private MessageService<TokenizableMessage<String>, MessageTokenDTO> fileNotificationMessageService;

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private FileNotificationStreams fileNotificationStreams;

    @Autowired
    private TokenGenerator tokenGenerator;


    @Test
    public void callNotificationService() throws Exception {

        long timestamp = System.currentTimeMillis();
        String fileId = "1001";
        String filename = "AssetAdditions2019.xlsx";
        String description = "Assets Acquired in FY 2019";

        final FileNotification fileNotification = FileNotification.builder().description(description).fileId(fileId).filename(filename).timestamp(timestamp).build();

        final FileNotification unMutatedFileNotification = SerializationUtils.clone(fileNotification);

        MessageTokenDTO messageToken = fileNotificationMessageService.sendMessage(fileNotification);

        BlockingQueue<?> mq = messageCollector.forChannel(fileNotificationStreams.outbound());
        assertThat(mq).isNotNull();

        // TODO FIX NULL POINTER EXCEPTION AND TEST WITH JAVA 13
        Object payload =
            Objects.requireNonNull(
                messageCollector.forChannel(fileNotificationStreams.outbound()).poll(10000, TimeUnit.MILLISECONDS).getPayload());

        // Check that message-token has been created in the db
        assertThat(messageToken.getId()).isNotNull();
        assertThat(messageToken.getTokenValue()).isEqualTo(tokenGenerator.md5Digest(unMutatedFileNotification));
        assertThat(messageToken.getTimeSent()).isEqualTo(fileNotification.getTimestamp());
        //        assertThat(payload.toString()).containsSequence(String.valueOf(timestamp));
        assertThat(payload.toString()).containsSequence(description);
        assertThat(payload.toString()).containsSequence(messageToken.getTokenValue());
    }
}
