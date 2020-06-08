package io.github.deposits.app.messaging.sample;

import io.github.deposits.app.messaging.platform.MessageService;
import io.github.deposits.app.messaging.platform.StringTokenMessageService;
import io.github.deposits.app.messaging.platform.TokenizableMessage;
import io.github.deposits.app.util.TokenGenerator;
import io.github.deposits.service.MessageTokenService;
import io.github.deposits.service.dto.MessageTokenDTO;
import io.github.deposits.service.mapper.MessageTokenMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.deposits.app.messaging.MessageServiceContainer.configureService;

@Configuration
public class GreetingsContainer {

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private MessageTokenService messageTokenService;
    @Autowired
    private MessageTokenMapper messageTokenMapper;
    @Autowired
    private GreetingsStreams greetingsStreams;

    @Bean("greetingsService")
    public MessageService<TokenizableMessage<String>, MessageTokenDTO> greetingsService() {

        return configureService(tokenGenerator, messageTokenService, greetingsStreams.outbound(), messageTokenMapper);
    }
}
