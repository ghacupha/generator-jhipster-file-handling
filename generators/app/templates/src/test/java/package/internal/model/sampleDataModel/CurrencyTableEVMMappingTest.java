package <%= packageName %>.internal.model.sampleDataModel;

import <%= packageName %>.internal.Mapping;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static <%= packageName %>.internal.model.sampleDataModel.CurrencyLocality.FOREIGN;
import static <%= packageName %>.internal.model.sampleDataModel.CurrencyLocality.LOCAL;
import static org.assertj.core.api.Assertions.assertThat;

class CurrencyTableEVMMappingTest {

    private Mapping<CurrencyTableEVM, CurrencyTableDTO> currencyTableEVMMapping;

    @BeforeEach
    void setUp() {
        currencyTableEVMMapping = new CurrencyTableEVMMappingImpl();
    }

    @Test
    void conversionToDTO() {

        CurrencyTableEVM evm1 = CurrencyTableEVM.builder().country("KENYA").currencyCode("KES").currencyName("SHILLING").locality("LOCAL").build();
        CurrencyTableEVM evm2 = CurrencyTableEVM.builder().country("UGANDA").currencyCode("UGX").currencyName("SHILLING").locality("FOREIGN").build();

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
