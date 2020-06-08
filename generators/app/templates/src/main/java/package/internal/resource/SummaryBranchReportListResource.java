package io.github.deposits.app.resource;

import io.github.deposits.app.model.BranchSummary;
import io.github.deposits.app.report.Report;
import io.github.deposits.app.report.params.BranchSummaryParameters;
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
public class SummaryBranchReportListResource implements ReportList<BranchSummary, LocalDate> {

    private final Report<List<BranchSummary>, BranchSummaryParameters> branchSummaryReportService;

    public SummaryBranchReportListResource(final Report<List<BranchSummary>, BranchSummaryParameters> branchSummaryReportService) {
        this.branchSummaryReportService = branchSummaryReportService;
    }

    @Override
    @GetMapping("/summary/branch-report")
    public ResponseEntity<List<BranchSummary>> getEntityList(@Valid @RequestParam LocalDate monthOfStudy) {
        log.info("Request for date : {} received...", monthOfStudy.format(DATETIME_FORMATTER));
        return ResponseEntity.ok(branchSummaryReportService.createReport(new BranchSummaryParameters(monthOfStudy)));
    }
}
