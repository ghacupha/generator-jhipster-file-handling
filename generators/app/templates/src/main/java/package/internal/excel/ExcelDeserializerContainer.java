package <%= packageName %>.internal.excel;

import <%= packageName %>.internal.excel.deserializer.DefaultExcelFileDeserializer;
import <%= packageName %>.internal.model.sampleDataModel.CurrencyTableEVM;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static <%= packageName %>.internal.excel.PoijiOptionsConfig.getDefaultPoijiOptions;

/**
 * This container has configurations for our excel file deserializers and a sample is provided for currency-table
 */
@Configuration
public class ExcelDeserializerContainer {

    @Bean("currencyTableExcelFileDeserializer")
    public ExcelFileDeserializer<CurrencyTableEVM> currencyTableExcelFileDeserializer() {
        return excelFile -> new DefaultExcelFileDeserializer<>(CurrencyTableEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    }

    // todo create bean for every data model
}
