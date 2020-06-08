package io.github.deposits.app.messaging;

import io.github.deposits.app.messaging.depositAccount.DepositAccountStreams;
import io.github.deposits.app.messaging.fileNotification.FileNotificationStreams;
import io.github.deposits.app.messaging.jsonStrings.JsonStringStreams;
import io.github.deposits.app.messaging.platform.MessageService;
import io.github.deposits.app.messaging.platform.StringTokenMessageService;
import io.github.deposits.app.messaging.platform.TokenizableMessage;
import io.github.deposits.app.messaging.schemeTable.SchemeTableStreams;
import io.github.deposits.app.util.TokenGenerator;
import io.github.deposits.service.MessageTokenService;
import io.github.deposits.service.dto.MessageTokenDTO;
import io.github.deposits.service.mapper.MessageTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.annotation.Transactional;

/**
 * Each of these beans had initially been configured as transaction, a recipe for big disaster as the
 * framework kept uploading the same messages especially if big amounts of information is involved in the
 * file.
 */
@Configuration
public class MessageServiceContainer {

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private MessageTokenService messageTokenService;
    @Autowired
    private MessageTokenMapper messageTokenMapper;
    @Autowired
    private FileNotificationStreams fileNotificationStreams;
    @Autowired
    private JsonStringStreams jsonStringStreams;

    @Autowired
    private SchemeTableStreams schemeTableStreams;

    @Autowired
    private DepositAccountStreams depositAccountStreams;

    @Transactional
    @Bean("depositAccountMessageService")
    public MessageService<TokenizableMessage<String>, MessageTokenDTO> depositAccountMessageService() {

        return configureService(tokenGenerator, messageTokenService, depositAccountStreams.outbound(), messageTokenMapper);
    }

    @Transactional
    @Bean("schemeTableMessageService")
    public MessageService<TokenizableMessage<String>, MessageTokenDTO> schemeTableMessageService() {

        return configureService(tokenGenerator, messageTokenService, schemeTableStreams.outbound(), messageTokenMapper);
    }

    @Transactional
    @Bean("jsonStringMessageService")
    public MessageService<TokenizableMessage<String>, MessageTokenDTO> jsonStringMessageService() {

        return configureService(tokenGenerator, messageTokenService, jsonStringStreams.depositsCreateOutbound(), messageTokenMapper);
    }

    @Transactional
    @Bean("fileNotificationMessageService")
    public MessageService<TokenizableMessage<String>, MessageTokenDTO> fileNotificationMessageService() {

        return configureService(tokenGenerator, messageTokenService, fileNotificationStreams.outbound(), messageTokenMapper);
    }

    public static MessageService<TokenizableMessage<String>, MessageTokenDTO> configureService(final TokenGenerator tokenGenerator, final MessageTokenService messageTokenService,
                                                                                               final MessageChannel outbound, final MessageTokenMapper messageTokenMapper) {
        return message -> {
            MessageService<TokenizableMessage<String>, MessageTokenDTO> service = new StringTokenMessageService(tokenGenerator, messageTokenService, outbound, messageTokenMapper);
            MessageTokenDTO messageTokenDTO = service.sendMessage(message);
            messageTokenDTO.setReceived(true);
            messageTokenDTO.setActioned(true);

            return messageTokenDTO;
        };
    }
}
