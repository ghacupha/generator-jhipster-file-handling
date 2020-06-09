package <%= packageName %>.internal.messaging.platform;

import com.fasterxml.jackson.core.JsonProcessingException;
import <%= packageName %>.internal.util.TokenGenerator;
import <%= packageName %>.domain.<%= classNamesPrefix %>MessageToken;
import <%= packageName %>.service.<%= classNamesPrefix %>MessageTokenService;
import <%= packageName %>.service.dto.<%= classNamesPrefix %>MessageTokenDTO;
import <%= packageName %>.service.mapper.<%= classNamesPrefix %>MessageTokenMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;

/**
 * This implementation handles sending of tokenizable messages and generating persisted tokens on the same. It is not managed by the spring  container but rather is extended by client implementations
 * which themselves implement @{code MessageService<TokenizableMessage<String>>} interface
 */
@Slf4j
public class StringTokenMessageService implements MessageService<TokenizableMessage<String>, <%= classNamesPrefix %>MessageTokenDTO> {

    private final TokenGenerator tokenGenerator;
    private final <%= classNamesPrefix %>MessageTokenService messageTokenService;
    private final MessageChannel messageChannel;
    private final <%= classNamesPrefix %>MessageTokenMapper messageTokenMapper;

    public StringTokenMessageService(final TokenGenerator tokenGenerator, final <%= classNamesPrefix %>MessageTokenService messageTokenService, final MessageChannel messageChannel,
                                     final <%= classNamesPrefix %>MessageTokenMapper messageTokenMapper) {
        this.tokenGenerator = tokenGenerator;
        this.messageTokenService = messageTokenService;
        this.messageChannel = messageChannel;
        this.messageTokenMapper = messageTokenMapper;
    }

    /**
     * This method sends a services of type T into a queue destination and returns a token id.
     *
     * @param message This is the item being sent
     * @return This is the token for the message that has just been sent
     */
    @Override
    public <%= classNamesPrefix %>MessageTokenDTO sendMessage(final TokenizableMessage<String> message) {

        log.debug("Tokenizable message {} received for submission", message);

        // Generate token before getting timestamps
        String token  = null;
        try {
            token = tokenGenerator.md5Digest(message);
        } catch (JsonProcessingException e) {
            log.error("The service has failed to create a message-token and has been aborted : ", e);
        }

        long timestamp = System.currentTimeMillis();
        message.setTimestamp(timestamp);

        <%= classNamesPrefix %>MessageToken messageToken = new <%= classNamesPrefix %>MessageToken().tokenValue(token).description(message.getDescription()).timeSent(timestamp);

        if (messageToken != null) {
            message.setMessageToken(messageToken.getTokenValue());
        }

        messageChannel.send(MessageBuilder.withPayload(message).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());

        return messageTokenService.save(messageTokenMapper.toDto(messageToken));
    }
}
