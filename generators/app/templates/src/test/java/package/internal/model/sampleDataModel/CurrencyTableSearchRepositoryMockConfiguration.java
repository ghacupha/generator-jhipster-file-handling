package <%= packageName %>.internal.model.sampleDataModel;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of {@link CurrencyTableSearchRepository} to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class CurrencyTableSearchRepositoryMockConfiguration {

    @MockBean
    private CurrencyTableSearchRepository mockCurrencyTableSearchRepository;

}
