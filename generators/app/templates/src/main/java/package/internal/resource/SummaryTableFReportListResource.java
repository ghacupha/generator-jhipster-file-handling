package io.github.deposits.app.resource;

import io.github.deposits.app.model.TableFSummary;
import io.github.deposits.app.report.Report;
import io.github.deposits.app.report.params.TableFSummaryParameters;
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
public class SummaryTableFReportListResource implements ReportList<TableFSummary, LocalDate> {

    private final Report<List<TableFSummary>, TableFSummaryParameters> tableFSummaryReport;

    public SummaryTableFReportListResource(final Report<List<TableFSummary>, TableFSummaryParameters> tableFSummaryReport) {
        this.tableFSummaryReport = tableFSummaryReport;
    }

    @Override
    @GetMapping("/summary/table-F")
    public ResponseEntity<List<TableFSummary>> getEntityList(@RequestParam LocalDate monthOfStudy) {
        log.info("Request for date : {} received...", monthOfStudy.format(DATETIME_FORMATTER));
        return ResponseEntity.ok(tableFSummaryReport.createReport(new TableFSummaryParameters(monthOfStudy)));
    }
}
