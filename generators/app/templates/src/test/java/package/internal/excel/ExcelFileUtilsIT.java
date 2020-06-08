package io.github.deposits.app.excel;

import io.github.deposits.DepositAnalysisMainApp;
import io.github.deposits.app.model.DepositAccountEVM;
import io.github.deposits.app.model.SchemeTableEVM;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static io.github.deposits.app.excel.ExcelTestUtil.readFile;
import static io.github.deposits.app.excel.ExcelTestUtil.toBytes;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

@SpringBootTest(classes = {DepositAnalysisMainApp.class})
public class ExcelFileUtilsIT {

    @Autowired
    private ExcelFileDeserializer<DepositAccountEVM> depositsExcelFileDeserializer;

    @Autowired
    private  ExcelFileDeserializer<SchemeTableEVM> schemeTableExcelFileDeserializer;

    @Test
    public void deserializeSchemesFile() throws Exception {

        // @formatter:off
        List<SchemeTableEVM> schemes =
            schemeTableExcelFileDeserializer.deserialize(toBytes(readFile("schemes.xlsx")));
        // @formatter:on

        assertThat(schemes.size()).isEqualTo(3);
        assertThat(schemes.get(0)).isEqualTo(SchemeTableEVM.builder().rowIndex(1).schemeCode("scheme1").description("scheme1description").build());
        assertThat(schemes.get(1)).isEqualTo(SchemeTableEVM.builder().rowIndex(2).schemeCode("scheme2").description("scheme2description").build());
        assertThat(schemes.get(2)).isEqualTo(SchemeTableEVM.builder().rowIndex(3).schemeCode("scheme3").description("scheme3description").build());
    }


    @Test
    public void deserializeAssetAcquisitionsFile() throws IOException {

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
