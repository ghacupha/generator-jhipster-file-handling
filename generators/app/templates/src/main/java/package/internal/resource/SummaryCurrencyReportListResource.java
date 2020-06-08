package io.github.deposits.app.resource;

import io.github.deposits.app.model.BranchSummary;
import io.github.deposits.app.model.CurrencySummary;
import io.github.deposits.app.report.Report;
import io.github.deposits.app.report.params.BranchSummaryParameters;
import io.github.deposits.app.report.params.CurrencySummaryParameters;
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
public class SummaryCurrencyReportListResource implements ReportList<CurrencySummary, LocalDate> {

    private final Report<List<CurrencySummary>, CurrencySummaryParameters> currencySummaryReportService;

    public SummaryCurrencyReportListResource(final Report<List<CurrencySummary>, CurrencySummaryParameters> currencySummaryReportService) {
        this.currencySummaryReportService = currencySummaryReportService;
    }

    @Override
    @GetMapping("/summary/currency-report")
    public ResponseEntity<List<CurrencySummary>> getEntityList(@Valid @RequestParam LocalDate monthOfStudy) {
        log.info("Request for date : {} received...", monthOfStudy.format(DATETIME_FORMATTER));
        return ResponseEntity.ok(currencySummaryReportService.createReport(new CurrencySummaryParameters(monthOfStudy)));
    }
}
