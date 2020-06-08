package io.github.deposits.app.excel;

import io.github.deposits.app.model.BranchTableEVM;
import io.github.deposits.app.model.CurrencyTableEVM;
import io.github.deposits.app.model.DepositAccountEVM;
import io.github.deposits.app.model.SBUTableEVM;
import io.github.deposits.app.model.SchemeTableEVM;
import io.github.deposits.app.model.TypeTableEVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static io.github.deposits.app.excel.ExcelTestUtil.readFile;
import static io.github.deposits.app.excel.ExcelTestUtil.toBytes;
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

    @Test
    public void deserializeBranchFile() throws Exception {

        ExcelFileDeserializer<BranchTableEVM> deserializer = container.branchTableExcelFileDeserializer();

        List<BranchTableEVM> branches = deserializer.deserialize(toBytes(readFile("branches.xlsx")));

        assertThat(branches.size()).isEqualTo(3);
        assertThat(branches.get(0)).isEqualTo(BranchTableEVM.builder().rowIndex(1).branchName("Branch 1").serviceOutletCode("Sol 1").county("County 1").town("Town 1").yearOpened(2019).build());
        assertThat(branches.get(1)).isEqualTo(BranchTableEVM.builder().rowIndex(2).branchName("Branch 2").serviceOutletCode("Sol 2").county("County 2").town("Town 2").yearOpened(2020).build());
        assertThat(branches.get(2)).isEqualTo(BranchTableEVM.builder().rowIndex(3).branchName("Branch 3").serviceOutletCode("Sol 3").county("County 3").town("Town 3").yearOpened(2021).build());
    }

    @Test
    public void deserializeSBUsFile() throws Exception {

        ExcelFileDeserializer<SBUTableEVM> deserializer = container.sbuTableExcelFileDeserializer();

        // @formatter:off
        List<SBUTableEVM> types =
            deserializer.deserialize(toBytes(readFile("sbus.xlsx")));
        // @formatter:on

        assertThat(types.size()).isEqualTo(3);
        assertThat(types.get(0)).isEqualTo(SBUTableEVM.builder().rowIndex(1).sbuCode("mult").sbuName("multinational").build());
        assertThat(types.get(1)).isEqualTo(SBUTableEVM.builder().rowIndex(2).sbuCode("cons").sbuName("consulting").build());
        assertThat(types.get(2)).isEqualTo(SBUTableEVM.builder().rowIndex(3).sbuCode("pr").sbuName("public relations").build());
    }

    @Test
    public void deserializeTypesFile() throws Exception {

        ExcelFileDeserializer<TypeTableEVM> deserializer = container.typeTableExcelFileDeserializer();

        // @formatter:off
        List<TypeTableEVM> types =
            deserializer.deserialize(toBytes(readFile("types.xlsx")));
        // @formatter:on

        assertThat(types.size()).isEqualTo(9);
        assertThat(types.get(0)).isEqualTo(TypeTableEVM.builder().rowIndex(1).glCode("gl1").typeDefinition("type1").tableFDefinition("table_f_type1").dpfDefinition("dpf_type1").build());
        assertThat(types.get(1)).isEqualTo(TypeTableEVM.builder().rowIndex(2).glCode("gl2").typeDefinition("type2").tableFDefinition("table_f_type2").dpfDefinition("dpf_type2").build());
        assertThat(types.get(2)).isEqualTo(TypeTableEVM.builder().rowIndex(3).glCode("gl3").typeDefinition("type3").tableFDefinition("table_f_type3").dpfDefinition("dpf_type3").build());
    }


    @Test
    public void deserializeSchemesFile() throws Exception {

        ExcelFileDeserializer<SchemeTableEVM> deserializer = container.schemeTableExcelFileDeserializer();

        // @formatter:off
        List<SchemeTableEVM> schemes =
            deserializer.deserialize(toBytes(readFile("schemes.xlsx")));
        // @formatter:on

        assertThat(schemes.size()).isEqualTo(3);
        assertThat(schemes.get(0)).isEqualTo(SchemeTableEVM.builder().rowIndex(1).schemeCode("scheme1").description("scheme1description").build());
        assertThat(schemes.get(1)).isEqualTo(SchemeTableEVM.builder().rowIndex(2).schemeCode("scheme2").description("scheme2description").build());
        assertThat(schemes.get(2)).isEqualTo(SchemeTableEVM.builder().rowIndex(3).schemeCode("scheme3").description("scheme3description").build());
    }

    @Test
    public void deserializeAssetAcquisitionsFile() throws IOException {

        ExcelDeserializerContainer container = new ExcelDeserializerContainer();

        ExcelFileDeserializer<DepositAccountEVM> depositsExcelFileDeserializer = container.depositsExcelFileDeserializer();

        // @formatter:off
        List<DepositAccountEVM> deposits =
            depositsExcelFileDeserializer.deserialize(toBytes(readFile("deposits.xlsx")));
        // @formatter:on

        assertEquals(10, deposits.size());
        assertEquals("mobile Fish", deposits.get(0).getSbuCode());
        assertEquals("Home Loan Account Table Computer", deposits.get(0).getRmCode());
        assertEquals("Trace", deposits.get(0).getSchemeCode());
        assertEquals("Savings Account alarm", deposits.get(0).getGlCode());
        assertEquals("AMD", deposits.get(0).getCurrencyCode());
        assertEquals("Producer", deposits.get(0).getCustomerId());
        assertEquals("Implementation Electronics Officer", deposits.get(0).getAccountNumber());
        assertEquals("Home Loan Account", deposits.get(0).getAccountName());
        assertEquals(30424.0, deposits.get(0).getAccountBalance(), 0.00);
        assertEquals("Albania web-readiness", deposits.get(0).getSector());
        assertEquals("Namibia", deposits.get(0).getSubSector());
        assertEquals("2019/11/21", deposits.get(0).getAccountOpeningDate());
        assertEquals("2019/11/21", deposits.get(0).getAccountMaturityDate());
        assertEquals("zero administration", deposits.get(0).getAccountStatus());
        assertEquals(36636.0, deposits.get(0).getRate(), 0.00);
        assertEquals(23277.0, deposits.get(0).getBookedInterestAmount(), 0.00);
        assertEquals(45606.0, deposits.get(0).getInterestAmount(), 0.00);
        assertEquals(9060.0, deposits.get(0).getAccruedInterestAmount(), 0.00);
        assertEquals("Savings Account syndicate Planner", deposits.get(0).getDepositScheme());
        assertEquals(46676.0, deposits.get(0).getRevaluedTotalAmount(), 0.00);
        assertEquals(4645.0, deposits.get(0).getRevaluedPrincipalAmount(), 0.00);
        assertEquals(85994.0, deposits.get(0).getRevaluedInterestAmount(), 0.00);
        assertEquals("2019/11/21", deposits.get(0).getMonthOfStudy());


        assertEquals("Brunei Darussalam interface", deposits.get(9).getSbuCode());
        assertEquals("compress lavender Investment Account", deposits.get(9).getRmCode());
        assertEquals("backing up", deposits.get(9).getSchemeCode());
        assertEquals("Iran", deposits.get(9).getGlCode());
        assertEquals("MRO", deposits.get(9).getCurrencyCode());
        assertEquals("deposit panel Strategist", deposits.get(9).getCustomerId());
        assertEquals("card Licensed Cotton Ball", deposits.get(9).getAccountNumber());
        assertEquals("Investment Account", deposits.get(9).getAccountName());
        assertEquals(43897.0, deposits.get(9).getAccountBalance(), 0.00);
        assertEquals("Gorgeous Electronics Brand", deposits.get(9).getSector());
        assertEquals("infomediaries Som Rustic Frozen Keyboard", deposits.get(9).getSubSector());
        assertEquals("2019/11/21", deposits.get(9).getAccountOpeningDate());
        assertEquals("2019/11/21", deposits.get(9).getAccountMaturityDate());
        assertEquals("Benin", deposits.get(9).getAccountStatus());
        assertEquals(71459.0, deposits.get(9).getRate(), 0.00);
        assertEquals(18286.0, deposits.get(9).getBookedInterestAmount(), 0.00);
        assertEquals(66019.0, deposits.get(9).getInterestAmount(), 0.00);
        assertEquals(93461.0, deposits.get(9).getAccruedInterestAmount(), 0.00);
        assertEquals("invoice", deposits.get(9).getDepositScheme());
        assertEquals(51669.0, deposits.get(9).getRevaluedTotalAmount(), 0.00);
        assertEquals(73789.0, deposits.get(9).getRevaluedPrincipalAmount(), 0.00);
        assertEquals(65711.0, deposits.get(9).getRevaluedInterestAmount(), 0.00);
        assertEquals("2019/11/21", deposits.get(0).getMonthOfStudy());
    }
}
