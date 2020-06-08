package io.github.deposits.app.resource;

import io.github.deposits.app.model.MoneyMarketSummary;
import io.github.deposits.app.report.params.MoneyMarketSummaryParameters;
import io.github.deposits.app.report.Report;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

import static io.github.deposits.app.AppConstants.DATETIME_FORMATTER;

@Slf4j
@RestController
@RequestMapping("/api/app")
public class SummaryMoneyMarketReportListResource implements ReportList<MoneyMarketSummary, LocalDate> {

    private final Report<List<MoneyMarketSummary>, MoneyMarketSummaryParameters> moneyMarketSummaryReport;

    public SummaryMoneyMarketReportListResource(final Report<List<MoneyMarketSummary>, MoneyMarketSummaryParameters> moneyMarketSummaryReport) {
        this.moneyMarketSummaryReport = moneyMarketSummaryReport;
    }

    @Override
    @GetMapping("/summary/money-market-report")
    public ResponseEntity<List<MoneyMarketSummary>> getEntityList(@Valid @RequestParam LocalDate monthOfStudy) {
        log.info("Request for date : {} received...", monthOfStudy.format(DATETIME_FORMATTER));
        return ResponseEntity.ok(moneyMarketSummaryReport.createReport(new MoneyMarketSummaryParameters(monthOfStudy)));
    }
}
