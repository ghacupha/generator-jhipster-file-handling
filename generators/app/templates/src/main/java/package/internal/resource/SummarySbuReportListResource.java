package io.github.deposits.app.resource;

import io.github.deposits.app.model.SBUSummary;
import io.github.deposits.app.report.Report;
import io.github.deposits.app.report.params.SBUSummaryParameters;
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
public class SummarySbuReportListResource implements ReportList<SBUSummary, LocalDate> {

    private final Report<List<SBUSummary>, SBUSummaryParameters> sbuSummaryReport;

    public SummarySbuReportListResource(final Report<List<SBUSummary>, SBUSummaryParameters> sbuSummaryReport) {
        this.sbuSummaryReport = sbuSummaryReport;
    }

    @Override
    @GetMapping("/summary/sbu-report")
    public ResponseEntity<List<SBUSummary>> getEntityList(@Valid @RequestParam LocalDate monthOfStudy) {
        log.info("Request for date : {} received...", monthOfStudy.format(DATETIME_FORMATTER));
        return ResponseEntity.ok(sbuSummaryReport.createReport(new SBUSummaryParameters(monthOfStudy)));
    }
}
