package <%= packageName %>.internal.messaging;

import <%= packageName %>.<%= appName %>App;
import <%= packageName %>.internal.messaging.platform.MessageService;
import <%= packageName %>.internal.messaging.platform.TokenizableMessage;
import <%= packageName %>.internal.messaging.sample.Greetings;
import <%= packageName %>.internal.messaging.sample.GreetingsStreams;
import <%= packageName %>.internal.util.TokenGenerator;
import <%= packageName %>.service.<%= classNamesPrefix %>MessageTokenService;
import <%= packageName %>.service.dto.<%= classNamesPrefix %>MessageTokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.test.binder.MessageCollector;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.MimeTypeUtils;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@SpringBootTest(classes = {<%= appName %>App.class})
public class GreetingsControllerIT {

    @Autowired
    private MessageService<TokenizableMessage<String>, <%= classNamesPrefix %>MessageTokenDTO> greetingsService;

    @Autowired
    private MessageCollector messageCollector;

    @Autowired
    private GreetingsStreams greetingsStreams;
    @Autowired
    private TokenGenerator tokenGenerator;

    @Autowired
    private <%= classNamesPrefix %>MessageTokenService messageTokenService;

    @AfterEach
    void tearDown() {
        log.debug("Right, time for some manual message-token-table cleanup");
        messageTokenService.findAll(Pageable.unpaged())
                           .stream()
                           .map(<%= classNamesPrefix %>MessageTokenDTO::getId)
                           .forEach(messageTokenService::delete);
    }

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void callGreetingsAPIStandAlone() throws Exception {

        String message = "There must always be a Stark in Winterfell";
        String description = "About the Starks";
        long timestamp = System.currentTimeMillis();
        Greetings greeting = Greetings.builder().message(message).timestamp(timestamp).description(description).build();
        String messageToken = tokenGenerator.md5Digest(greeting);
        greeting.setMessageToken(messageToken);

        greetingsStreams.outbound().send(MessageBuilder.withPayload(greeting).setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON).build());

        Object payload = messageCollector.forChannel(greetingsStreams.outbound()).poll().getPayload();

        assertThat(payload.toString()).containsSequence(String.valueOf(timestamp));
        assertThat(payload.toString()).containsSequence(String.valueOf(description));
        assertThat(payload.toString()).containsSequence(String.valueOf(message));
        assertThat(payload.toString()).containsSequence(String.valueOf(messageToken));
    }

    @Test
    public void callGreetingsService() throws Exception {

        String message = "There must always be a Stark in Winterfell";
        String description = "About the Starks";
        long timestamp = System.currentTimeMillis();

        final Greetings greeting = Greetings.builder().message(message).description(description).timestamp(timestamp).build();

        final Greetings unMutatedGreeting = SerializationUtils.clone(greeting);

        <%= classNamesPrefix %>MessageTokenDTO messageToken = greetingsService.sendMessage(greeting);

        log.info("Message sent with the token: {}", messageToken.getTokenValue());

        Object payload = messageCollector.forChannel(greetingsStreams.outbound()).poll().getPayload();

        // Check that message-token has been created in the db
        assertThat(messageToken.getId()).isNotNull();
        assertThat(messageToken.getTokenValue()).isEqualTo(tokenGenerator.md5Digest(unMutatedGreeting));
        assertThat(messageToken.getTimeSent()).isEqualTo(greeting.getTimestamp());
        //        assertThat(payload.toString()).containsSequence(String.valueOf(timestamp));
        assertThat(payload.toString()).containsSequence(message);
        assertThat(payload.toString()).containsSequence(description);
        assertThat(payload.toString()).containsSequence(messageToken.getTokenValue());
    }
}
