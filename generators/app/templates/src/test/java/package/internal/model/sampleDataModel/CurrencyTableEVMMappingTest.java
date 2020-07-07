package <%= packageName %>.internal.model.sampleDataModel;

import <%= packageName %>.internal.Mapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import <%= packageName %>.service.dto.CurrencyTableDTO;

import static <%= packageName %>.domain.enumeration.CurrencyLocality.FOREIGN;
import static <%= packageName %>.domain.enumeration.CurrencyLocality.LOCAL;
import static org.assertj.core.api.Assertions.assertThat;

class CurrencyTableEVMMappingTest {

    private Mapping<CurrencyTableEVM, CurrencyTableDTO> currencyTableEVMMapping;

    @BeforeEach
    void setUp() {
        currencyTableEVMMapping = new CurrencyTableEVMMappingImpl();
    }

    @Test
    void conversionToDTO() {

            CurrencyTableDTO dto1 = new CurrencyTableDTO();
            dto1.setCountry("KENYA");
            dto1.setCurrencyCode("KES");
            dto1.setCurrencyName("KENYA SHILLING");
            dto1.setLocality(LOCAL);

            CurrencyTableDTO dto2 = new CurrencyTableDTO();
            dto2.setCountry("UGANDA");
            dto2.setCurrencyCode("UGX");
            dto2.setCurrencyName("UGANDA SHILLING");
            dto2.setLocality(FOREIGN);

        assertThat(currencyTableEVMMapping.toValue2(evm1).getLocality()).isEqualTo(LOCAL);
        assertThat(currencyTableEVMMapping.toValue2(evm2).getLocality()).isEqualTo(FOREIGN);
    }

    @Test
    void conversionToEVM() {

        CurrencyTableDTO dto1 = CurrencyTableDTO.builder().country("KENYA").currencyCode("KES").currencyName("KENYA SHILLING").locality(LOCAL).build();
        CurrencyTableDTO dto2 = CurrencyTableDTO.builder().country("UGANDA").currencyCode("UGX").currencyName("UGANDA SHILLING").locality(FOREIGN).build();

        assertThat(currencyTableEVMMapping.toValue1(dto1).getLocality()).isEqualTo("LOCAL");
        assertThat(currencyTableEVMMapping.toValue1(dto2).getLocality()).isEqualTo("FOREIGN");
    }
}
