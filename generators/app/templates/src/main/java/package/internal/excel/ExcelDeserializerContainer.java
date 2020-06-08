package io.github.deposits.app.excel;

import io.github.deposits.app.excel.deserializer.DefaultExcelFileDeserializer;
import io.github.deposits.app.model.BranchTableEVM;
import io.github.deposits.app.model.CurrencyTableEVM;
import io.github.deposits.app.model.DepositAccountEVM;
import io.github.deposits.app.model.SBUTableEVM;
import io.github.deposits.app.model.SchemeTableEVM;
import io.github.deposits.app.model.TypeTableEVM;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.deposits.app.excel.PoijiOptionsConfig.getDefaultPoijiOptions;

@Configuration
public class ExcelDeserializerContainer {

    @Bean("depositsExcelFileDeserializer")
    public ExcelFileDeserializer<DepositAccountEVM> depositsExcelFileDeserializer() {
        return excelFile -> new DefaultExcelFileDeserializer<>(DepositAccountEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    }

    @Bean("schemeTableExcelFileDeserializer")
    public ExcelFileDeserializer<SchemeTableEVM> schemeTableExcelFileDeserializer() {
        return excelFile -> new DefaultExcelFileDeserializer<>(SchemeTableEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    }

    @Bean("currencyTableExcelFileDeserializer")
    public ExcelFileDeserializer<CurrencyTableEVM> currencyTableExcelFileDeserializer() {
        return excelFile -> new DefaultExcelFileDeserializer<>(CurrencyTableEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    }

    @Bean("branchTableExcelFileDeserializer")
    public ExcelFileDeserializer<BranchTableEVM> branchTableExcelFileDeserializer() {
        return excelFile -> new DefaultExcelFileDeserializer<>(BranchTableEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    }

    @Bean("typeTableExcelFileDeserializer")
    public ExcelFileDeserializer<TypeTableEVM> typeTableExcelFileDeserializer() {
        return excelFile -> new DefaultExcelFileDeserializer<>(TypeTableEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    }

    @Bean("sbuTableExcelFileDeserializer")
    public ExcelFileDeserializer<SBUTableEVM> sbuTableExcelFileDeserializer() {
        return excelFile -> new DefaultExcelFileDeserializer<>(SBUTableEVM.class, getDefaultPoijiOptions()).deserialize(excelFile);
    }
}
