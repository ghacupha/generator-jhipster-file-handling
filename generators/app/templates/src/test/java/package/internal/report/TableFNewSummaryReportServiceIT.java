package io.github.deposits.app.report;

import io.github.deposits.DepositAnalysisMainApp;
import io.github.deposits.app.model.TableFNewSummary;
import io.github.deposits.app.report.params.TableFNewSummaryParameters;
import org.springframework.boot.test.context.SpringBootTest;
import io.github.deposits.repository.DepositAccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {DepositAnalysisMainApp.class})
public class TableFNewSummaryReportServiceIT {


    @Autowired
    private EntityManagerFactory entityManager;

    @Autowired
    private EntityManager em;

    @Autowired
    private DepositAccountRepository depositAccountRepository;

    @Autowired
    private Report<List<TableFNewSummary>, TableFNewSummaryParameters> tableFNewSummaryService;

    @Test
    public void sessionFactoryIsInjected() {

        assertThat(entityManager).isNotNull();
        assertThat(tableFNewSummaryService).isNotNull();
    }

    @Test
    @Transactional
    public void createReport() throws Exception {
        List<TableFNewSummary> summaryList = tableFNewSummaryService.createReport(new TableFNewSummaryParameters(LocalDate.of(2018, 9, 1)));

        // ! Fix this!
        assertThat(summaryList.size()).isEqualTo(0);
    }
}
