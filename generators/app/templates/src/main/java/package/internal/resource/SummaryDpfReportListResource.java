package io.github.deposits.app.resource;

import io.github.deposits.app.model.DPFSummary;
import io.github.deposits.app.report.Report;
import io.github.deposits.app.report.params.DPFSummaryParameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static io.github.deposits.app.AppConstants.DATETIME_FORMATTER;

@Slf4j
@RestController
@RequestMapping("/api/app")
public class SummaryDpfReportListResource implements ReportList<DPFSummary, LocalDate> {

    private final Report<List<DPFSummary>, DPFSummaryParameters> dpfSummaryReport;

    public SummaryDpfReportListResource(final Report<List<DPFSummary>, DPFSummaryParameters> dpfSummaryReport) {
        this.dpfSummaryReport = dpfSummaryReport;
    }

    @Override
    @GetMapping("/summary/dpf")
    public ResponseEntity<List<DPFSummary>> getEntityList(@RequestParam LocalDate monthOfStudy) {
        log.info("Request for date : {} received...", monthOfStudy.format(DATETIME_FORMATTER));
        return ResponseEntity.ok(dpfSummaryReport.createReport(new DPFSummaryParameters(monthOfStudy)));
    }
}
