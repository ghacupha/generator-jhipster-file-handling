package <%= packageName %>.internal.messaging;

import <%= packageName %>.service.<%= classNamesPrefix %>MessageTokenQueryService;
import <%= packageName %>.service.dto.<%= classNamesPrefix %>MessageTokenCriteria;
import <%= packageName %>.service.dto.<%= classNamesPrefix %>MessageTokenDTO;
import io.github.jhipster.service.filter.StringFilter;
import org.springframework.stereotype.Service;

/**
 * Implentation of token-search where the token value itself is of the value string
 */
@Service("stringTokenValueSearch")
public class StringTokenValueSearch implements TokenValueSearch<String> {

    private final <%= classNamesPrefix %>MessageTokenQueryService messageTokenQueryService;

    public StringTokenValueSearch(final <%= classNamesPrefix %>MessageTokenQueryService messageTokenQueryService) {
        this.messageTokenQueryService = messageTokenQueryService;
    }

    public <%= classNamesPrefix %>MessageTokenDTO getMessageToken(final String tokenValue) {
        StringFilter tokenFilter = new StringFilter();
        tokenFilter.setEquals(tokenValue);
        <%= classNamesPrefix %>MessageTokenCriteria tokenValueCriteria = new <%= classNamesPrefix %>MessageTokenCriteria();
        tokenValueCriteria.setTokenValue(tokenFilter);
        return messageTokenQueryService.findByCriteria(tokenValueCriteria).get(0);
    }
}
