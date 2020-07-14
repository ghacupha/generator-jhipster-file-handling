package <%= packageName %>.internal.fileProcessing.sample;

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

import static <%= packageName %>.internal.fileProcessing.MessageServiceContainer.configureService;

/**
 * Example configuration of a typical streams container
 *
 * @return
 */
@Configuration
public class GreetingsContainer {

    @Autowired
    private TokenGenerator tokenGenerator;
    @Autowired
    private <%= classNamesPrefix %>MessageTokenService messageTokenService;
    @Autowired
    private <%= classNamesPrefix %>MessageTokenMapper messageTokenMapper;
    @Autowired
    private GreetingsStreams greetingsStreams;

    @Bean("greetingsService")
    public MessageService<TokenizableMessage<String>, <%= classNamesPrefix %>MessageTokenDTO> greetingsService() {

        return configureService(tokenGenerator, messageTokenService, greetingsStreams.outbound(), messageTokenMapper);
    }
}
