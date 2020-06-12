package <%= packageName %>.internal.model.sampleDataModel;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import <%= packageName %>.web.rest.TestUtil;

public class CurrencyTableDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CurrencyTableDTO.class);
        CurrencyTableDTO currencyTableDTO1 = new CurrencyTableDTO();
        currencyTableDTO1.setId(1L);
        CurrencyTableDTO currencyTableDTO2 = new CurrencyTableDTO();
        assertThat(currencyTableDTO1).isNotEqualTo(currencyTableDTO2);
        currencyTableDTO2.setId(currencyTableDTO1.getId());
        assertThat(currencyTableDTO1).isEqualTo(currencyTableDTO2);
        currencyTableDTO2.setId(2L);
        assertThat(currencyTableDTO1).isNotEqualTo(currencyTableDTO2);
        currencyTableDTO1.setId(null);
        assertThat(currencyTableDTO1).isNotEqualTo(currencyTableDTO2);
    }
}
