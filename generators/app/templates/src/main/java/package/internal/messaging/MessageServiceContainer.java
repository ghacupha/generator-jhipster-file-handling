package <%= packageName %>.internal.messaging;

import <%= packageName %>.internal.messaging.fileNotification.FileNotificationStreams;
import <%= packageName %>.internal.messaging.platform.MessageService;
import <%= packageName %>.internal.messaging.platform.StringTokenMessageService;
import <%= packageName %>.internal.messaging.platform.TokenizableMessage;
import <%= packageName %>.internal.util.TokenGenerator;
import <%= packageName %>.service.MessageTokenService;
import <%= packageName %>.service.dto.MessageTokenDTO;
import <%= packageName %>.service.mapper.MessageTokenMapper;
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
    private MessageTokenService messageTokenService;
    @Autowired
    private MessageTokenMapper messageTokenMapper;
    @Autowired
    private FileNotificationStreams fileNotificationStreams;

    @Transactional
    @Bean("fileNotificationMessageService")
    public MessageService<TokenizableMessage<String>, MessageTokenDTO> fileNotificationMessageService() {

        return configureService(tokenGenerator, messageTokenService, fileNotificationStreams.outbound(), messageTokenMapper);
    }

    /**
     * Utility configuration for message service to enable on the fly stream-syntax configuration inline
     * 
     * @param tokenGenerator
     * @param messageTokenService
     * @param outbound
     * @param messageTokenMapper
     * @return configured MessageService<TokenizableMessage<String>, MessageTokenDTO> implementation
     */
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
