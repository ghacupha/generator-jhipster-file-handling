package <%= packageName %>.internal.resource;

import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * This is a general interface that gives a report but summarised as a 
 * <p/>
 * list of pre-designated measures, and served as a resource.
 * <p/>
 *  Check the DTO used for each implementation. Strictly speaking this has nothing
 * <p/>
 * to do with file processing, but might be the only available way you will have for checking
 * <p/>
 * that your data has been uploaded for a given entity
 * <p/>
 * A typical implementation might look like so:
 * <p/>
 * {@code 
 * 
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
 * 
 * }
 * 
 * @param parameters
 * @return
 */
public interface ReportList<Entity, Parameter> {

    ResponseEntity<List<Entity>> getEntityList(Parameter parameters);
}
