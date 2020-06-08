package io.github.deposits.app.resource;

import io.github.deposits.app.model.TableFNewSummary;
import io.github.deposits.app.report.Report;
import io.github.deposits.app.report.params.TableFNewSummaryParameters;
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
public class SummaryTableFNewReportListResource implements ReportList<TableFNewSummary, LocalDate>  {

    private final Report<List<TableFNewSummary>, TableFNewSummaryParameters> tableFNewSummaryReport;

    public SummaryTableFNewReportListResource(final Report<List<TableFNewSummary>, TableFNewSummaryParameters> tableFSummaryReport) {
        this.tableFNewSummaryReport = tableFSummaryReport;
    }

    @Override
    @GetMapping("/summary/table-F-new")
    public ResponseEntity<List<TableFNewSummary>> getEntityList(@RequestParam LocalDate monthOfStudy) {
        log.info("Request for date : {} received...", monthOfStudy.format(DATETIME_FORMATTER));
        return ResponseEntity.ok(tableFNewSummaryReport.createReport(new TableFNewSummaryParameters(monthOfStudy)));
    }
}
