package <%= packageName %>.internal.excel;

import <%= packageName %>.<%= appName %>App;
import <%= packageName %>.internal.model.sampleDataModel.CurrencyTableEVM;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static <%= packageName %>.internal.excel.ExcelTestUtil.readFile;
import static <%= packageName %>.internal.excel.ExcelTestUtil.toBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * If nothing is added in value for this test it confirms that the excel deserializer beans
 * <p>
 * are correctly configured. We are using the sample currency-table model from the internal
 * <p>
 * package but and if the user wishes additional tests can be added for custom data models
 */
@SpringBootTest(classes = <%= appName %>App.class)
public class ExcelFileUtilsIT {

    @Autowired
    private ExcelFileDeserializer<CurrencyTableEVM> currencyTableEVMExcelFileDeserializer;

    @Test
    public void deserializeCurrencyTableFile() throws Exception {

        // @formatter:off
        List<CurrencyTableEVM> currencies =
            currencyTableEVMExcelFileDeserializer.deserialize(toBytes(readFile("currencies.xlsx")));
        // @formatter:on

        assertThat(currencies.size()).isEqualTo(13);
        assertThat(currencies.get(0)).isEqualTo(CurrencyTableEVM.builder().rowIndex(1).country("USA").currencyCode("USD").currencyName("US DOLLAR").locality("FOREIGN").build());
        assertThat(currencies.get(1)).isEqualTo(CurrencyTableEVM.builder().rowIndex(2).country("UNITED KINGDOM").currencyCode("GBP").currencyName("STERLING POUND").locality("FOREIGN").build());
        assertThat(currencies.get(2)).isEqualTo(CurrencyTableEVM.builder().rowIndex(3).country("EURO-ZONE").currencyCode("EUR").currencyName("EURO").locality("FOREIGN").build());
        assertThat(currencies.get(3)).isEqualTo(CurrencyTableEVM.builder().rowIndex(4).country("KENYA").currencyCode("KES").currencyName("KENYA SHILLING").locality("LOCAL").build());
        assertThat(currencies.get(4)).isEqualTo(CurrencyTableEVM.builder().rowIndex(5).country("SWITZERLAND").currencyCode("CHF").currencyName("SWISS FRANC").locality("FOREIGN").build());
        assertThat(currencies.get(5)).isEqualTo(CurrencyTableEVM.builder().rowIndex(6).country("SOUTH AFRICA").currencyCode("ZAR").currencyName("SOUTH AFRICAN RAND").locality("FOREIGN").build());
        assertThat(currencies.get(12)).isEqualTo(CurrencyTableEVM.builder().rowIndex(13).country("CHINA").currencyCode("CNY").currencyName("CHINESE YUAN").locality("FOREIGN").build());
    }
}
