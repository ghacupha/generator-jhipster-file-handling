package <%= packageName %>.internal.excel;

import <%= packageName %>.internal.excel.deserializer.DefaultExcelFileDeserializer;
// todo loop for each data model
// import <%= packageName %>.internal.model.BranchTableEVM;
// import <%= packageName %>.internal.model.CurrencyTableEVM;
// import <%= packageName %>.internal.model.DepositAccountEVM;
// import <%= packageName %>.internal.model.SBUTableEVM;
// import <%= packageName %>.internal.model.SchemeTableEVM;
// import <%= packageName %>.internal.model.TypeTableEVM;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static <%= packageName %>.internal.excel.PoijiOptionsConfig.getDefaultPoijiOptions;

@Configuration
public class ExcelDeserializerContainer {

    // todo loop for each data model
    // @Bean("schemeTableExcelFileDeserializer")
    // public ExcelFileDeserializer<SchemeTableEVM> schemeTableExcelFileDeserializer() {
    //     return excelFile -> new DefaultExcelFileDeserializer<>(SchemeTableEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    // }

    // @Bean("currencyTableExcelFileDeserializer")
    // public ExcelFileDeserializer<CurrencyTableEVM> currencyTableExcelFileDeserializer() {
    //     return excelFile -> new DefaultExcelFileDeserializer<>(CurrencyTableEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    // }

    // @Bean("branchTableExcelFileDeserializer")
    // public ExcelFileDeserializer<BranchTableEVM> branchTableExcelFileDeserializer() {
    //     return excelFile -> new DefaultExcelFileDeserializer<>(BranchTableEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    // }

    // @Bean("typeTableExcelFileDeserializer")
    // public ExcelFileDeserializer<TypeTableEVM> typeTableExcelFileDeserializer() {
    //     return excelFile -> new DefaultExcelFileDeserializer<>(TypeTableEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    // }

    // @Bean("sbuTableExcelFileDeserializer")
    // public ExcelFileDeserializer<SBUTableEVM> sbuTableExcelFileDeserializer() {
    //     return excelFile -> new DefaultExcelFileDeserializer<>(SBUTableEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    // }
}
