package <%= packageName %>.internal.excel;

import <%= packageName %>.internal.model.sampleDataModel.CurrencyTableEVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static <%= packageName %>.internal.excel.ExcelTestUtil.readFile;
import static <%= packageName %>.internal.excel.ExcelTestUtil.toBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

public class ExcelFileUtilsTest {

    private ExcelDeserializerContainer container;

    @BeforeEach
    void setUp() {
        container = new ExcelDeserializerContainer();
    }

    @Test
    public void deserializeCurrenciesFile() throws Exception {

        ExcelFileDeserializer<CurrencyTableEVM> deserializer = container.currencyTableExcelFileDeserializer();

        List<CurrencyTableEVM> currencies = deserializer.deserialize(toBytes(readFile("currencies.xlsx")));

        assertThat(currencies.size()).isEqualTo(13);
        assertThat(currencies.get(0)).isEqualTo(CurrencyTableEVM.builder().rowIndex(1).currencyCode("USD").locality("FOREIGN").currencyName("US DOLLAR").country("USA").build());
        assertThat(currencies.get(1)).isEqualTo(CurrencyTableEVM.builder().rowIndex(2).currencyCode("GBP").locality("FOREIGN").currencyName("STERLING POUND").country("UNITED KINGDOM").build());
        assertThat(currencies.get(2)).isEqualTo(CurrencyTableEVM.builder().rowIndex(3).currencyCode("EUR").locality("FOREIGN").currencyName("EURO").country("EURO-ZONE").build());
    }
}
