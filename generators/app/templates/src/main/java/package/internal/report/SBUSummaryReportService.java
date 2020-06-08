package io.github.deposits.app.report;

import io.github.deposits.app.model.SBUSummary;
import io.github.deposits.app.report.params.SBUSummaryParameters;
import io.github.deposits.domain.DepositAccount;
import io.github.deposits.domain.DepositAccount_;
import io.github.deposits.domain.SbuTable;
import io.github.deposits.domain.SbuTable_;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.List;

import static io.github.deposits.domain.DepositAccount_.MONTH_OF_STUDY;
import static io.github.deposits.domain.DepositAccount_.RATE;
import static io.github.deposits.domain.DepositAccount_.REVALUED_INTEREST_AMOUNT;
import static io.github.deposits.domain.DepositAccount_.REVALUED_PRINCIPAL_AMOUNT;
import static io.github.deposits.domain.DepositAccount_.REVALUED_TOTAL_AMOUNT;

@Slf4j
@Service("sbuSummaryReportService")
public class SBUSummaryReportService implements Report<List<SBUSummary>, SBUSummaryParameters> {


    private final SessionFactory sessionFactory;


    public SBUSummaryReportService(EntityManagerFactory entityManager) {
        this.sessionFactory = entityManager.unwrap(SessionFactory.class);
    }

    // @formatter:off
    @Override
    @Cacheable("sbu-summary-report-cache")
    public List<SBUSummary> createReport(final SBUSummaryParameters reportParameter) {

        long requestStart = System.currentTimeMillis();
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<SBUSummary> criteriaQuery = cb.createQuery(SBUSummary.class);
        Root<DepositAccount> depositRoot = criteriaQuery.from(DepositAccount.class);
        Root<SbuTable> sbuRoot = criteriaQuery.from(SbuTable.class);

        // Predicates
        Predicate monthOfStudyPredicate = cb.equal(depositRoot.get(MONTH_OF_STUDY), reportParameter.getMonthOfStudy());
        Predicate sbuPredicate = cb.equal(depositRoot.get(DepositAccount_.SBU_CODE), sbuRoot.get(SbuTable_.SBU_CODE));
        Predicate noneZeroSumTotalPredicate = cb.notEqual(cb.sum(depositRoot.get(REVALUED_TOTAL_AMOUNT)), BigDecimal.ZERO);

        // Define the SELECT clause
        criteriaQuery
           .multiselect(
//                root.get(SBU_CODE),
               sbuRoot.get(SbuTable_.SBU_NAME),
                cb.sum(depositRoot.get(REVALUED_PRINCIPAL_AMOUNT)),
                cb.sum(depositRoot.get(REVALUED_INTEREST_AMOUNT)),
                cb.sum(depositRoot.get(REVALUED_TOTAL_AMOUNT)),
                cb.min(depositRoot.get(RATE)),
                cb.max(depositRoot.get(RATE)),
//                cb.avg(root.get("rate")))
                cb.quot(cb.sum(cb.prod(depositRoot.get(RATE), depositRoot.get(REVALUED_TOTAL_AMOUNT))), cb.sum(depositRoot.get(REVALUED_TOTAL_AMOUNT))))
           .groupBy(sbuRoot.get(SbuTable_.SBU_NAME))
           .where(monthOfStudyPredicate, sbuPredicate)
           .having(noneZeroSumTotalPredicate);

        List<SBUSummary> sbuSummaryList = sessionFactory.createEntityManager().createQuery(criteriaQuery).getResultList();

        log.info("Sbu-summary list with {} items, generated in {} ms", sbuSummaryList.size(), System.currentTimeMillis() - requestStart);

        return sbuSummaryList;
    }
    // @formatter:on
}
