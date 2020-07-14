package <%= packageName %>.internal.fileProcessing;

import <%= packageName %>.internal.fileProcessing.fileNotification.FileNotificationStreams;
import <%= packageName %>.internal.fileProcessing.jsonStrings.JsonStringStreams;
import <%= packageName %>.internal.fileProcessing.platform.MessageService;
import <%= packageName %>.internal.fileProcessing.platform.StringTokenMessageService;
import <%= packageName %>.internal.fileProcessing.platform.TokenizableMessage;
import <%= packageName %>.internal.util.TokenGenerator;
import <%= packageName %>.service.<%= classNamesPrefix %>MessageTokenService;
import <%= packageName %>.service.dto.<%= classNamesPrefix %>MessageTokenDTO;
import <%= packageName %>.service.mapper.<%= classNamesPrefix %>MessageTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.MessageChannel;
import org.springframework.transaction.annotation.Transactional;

/**
 * This container generally contains services configurations for message services.
 * <p/>
 * Each of these beans had initially been configured as transaction,
 * <p/>
 * a recipe for big disaster as the framework kept uploading the same
 * <p/>
 * messages especially if big amounts of information is involved in the file.
 */
@Configuration
public class MessageServiceContainer {

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private <%= classNamesPrefix %>MessageTokenService messageTokenService;
    @Autowired
    private <%= classNamesPrefix %>MessageTokenMapper messageTokenMapper;
    @Autowired
    private FileNotificationStreams fileNotificationStreams;
    @Autowired
    private JsonStringStreams jsonStringStreams;

    /**
     * Configuration for the file-notifications-message-service
     *
     * @return MessageService<TokenizableMessage<String>, <%= classNamesPrefix %>MessageTokenDTO>
     */
    @Transactional
    @Bean("fileNotificationMessageService")
    public MessageService<TokenizableMessage<String>, <%= classNamesPrefix %>MessageTokenDTO> fileNotificationMessageService() {

        return configureService(tokenGenerator, messageTokenService, fileNotificationStreams.outbound(), messageTokenMapper);
    }

    /**
     * Configuration for the string message service
     *
     * @return MessageService<TokenizableMessage<String>, <%= classNamesPrefix %>MessageTokenDTO>
     */
    @Transactional
    @Bean("jsonStringMessageService")
    public MessageService<TokenizableMessage<String>, <%= classNamesPrefix %>MessageTokenDTO> jsonStringMessageService() {

        return configureService(tokenGenerator, messageTokenService, jsonStringStreams.depositsCreateOutbound(), messageTokenMapper);
    }

    /**
     * Utility configuration for message service to enable on the fly stream-syntax configuration inline
     *
     * @param tokenGenerator
     * @param messageTokenService
     * @param outbound
     * @param messageTokenMapper
     * @return configured MessageService<TokenizableMessage<String>, <%= classNamesPrefix %>MessageTokenDTO> implementation
     */
    public static MessageService<TokenizableMessage<String>, <%= classNamesPrefix %>MessageTokenDTO> configureService(final TokenGenerator tokenGenerator, final <%= classNamesPrefix %>MessageTokenService messageTokenService,
                                                                                               final MessageChannel outbound, final <%= classNamesPrefix %>MessageTokenMapper messageTokenMapper) {
        return message -> {
            MessageService<TokenizableMessage<String>, <%= classNamesPrefix %>MessageTokenDTO> service = new StringTokenMessageService(tokenGenerator, messageTokenService, outbound, messageTokenMapper);
            <%= classNamesPrefix %>MessageTokenDTO messageTokenDTO = service.sendMessage(message);
            messageTokenDTO.setReceived(true);
            messageTokenDTO.setActioned(true);

            return messageTokenDTO;
        };
    }
}
