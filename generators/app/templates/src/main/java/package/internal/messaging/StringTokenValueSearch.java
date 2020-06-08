package <%= packageName %>.internal.messaging;

import <%= packageName %>.service.MessageTokenQueryService;
import <%= packageName %>.service.dto.MessageTokenCriteria;
import <%= packageName %>.service.dto.MessageTokenDTO;
import io.github.jhipster.service.filter.StringFilter;
import org.springframework.stereotype.Service;

/**
 * Implentation of token-search where the token value itself is of the value string
 */
@Service("stringTokenValueSearch")
public class StringTokenValueSearch implements TokenValueSearch<String> {

    private final MessageTokenQueryService messageTokenQueryService;

    public StringTokenValueSearch(final MessageTokenQueryService messageTokenQueryService) {
        this.messageTokenQueryService = messageTokenQueryService;
    }

    public MessageTokenDTO getMessageToken(final String tokenValue) {
        StringFilter tokenFilter = new StringFilter();
        tokenFilter.setEquals(tokenValue);
        MessageTokenCriteria tokenValueCriteria = new MessageTokenCriteria();
        tokenValueCriteria.setTokenValue(tokenFilter);
        return messageTokenQueryService.findByCriteria(tokenValueCriteria).get(0);
    }
}
