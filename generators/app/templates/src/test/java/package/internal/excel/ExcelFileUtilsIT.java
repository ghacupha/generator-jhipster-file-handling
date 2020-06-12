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

        assertThat(currencies.size()).isEqualTo(3);
        assertThat(currencies.get(0)).isEqualTo(CurrencyTableEVM.builder().rowIndex(1).country("USA").currencyCode("USD").currencyName("US DOLLAR").locality("FOREIGN").build());
        assertThat(currencies.get(0)).isEqualTo(CurrencyTableEVM.builder().rowIndex(1).country("UNITED KINGDOM").currencyCode("GBP").currencyName("STERLING").locality("FOREIGN").build());
    }
}
