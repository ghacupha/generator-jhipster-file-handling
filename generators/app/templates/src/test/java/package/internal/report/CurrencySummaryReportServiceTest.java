package io.github.deposits.app.report;

import io.github.deposits.DepositAnalysisMainApp;
import io.github.deposits.app.model.CurrencySummary;
import io.github.deposits.app.report.params.CurrencySummaryParameters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing this services is tough since we do not have a way of actually querying the sql string produced by the criteria api FIXME find a way of querying criteria api sql strings
 */
@SpringBootTest(classes = {DepositAnalysisMainApp.class})
public class CurrencySummaryReportServiceTest {

    @Autowired
    private Report<List<CurrencySummary>, CurrencySummaryParameters> currencySummaryReportService;

    @Test
    public void sessionFactoryIsInjected() {

        assertThat(currencySummaryReportService).isNotNull();
    }

    @Test
    @Transactional
    public void createReport() throws Exception {
        List<CurrencySummary> summaryList = currencySummaryReportService.createReport(new CurrencySummaryParameters(LocalDate.of(2018, 9, 1)));

        // ! Fix this!
        assertThat(summaryList.size()).isEqualTo(0);
    }
}
