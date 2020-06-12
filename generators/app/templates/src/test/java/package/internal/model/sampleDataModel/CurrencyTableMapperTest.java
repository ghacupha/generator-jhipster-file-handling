package <%= packageName %>.internal.model.sampleDataModel;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class CurrencyTableMapperTest {

    private CurrencyTableMapper currencyTableMapper;

    @BeforeEach
    public void setUp() {
        currencyTableMapper = new CurrencyTableMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(currencyTableMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(currencyTableMapper.fromId(null)).isNull();
    }
}
