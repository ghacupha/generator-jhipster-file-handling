package io.github.deposits.app.report;

import io.github.deposits.app.model.CurrencySummary;
import io.github.deposits.app.report.params.CurrencySummaryParameters;
import io.github.deposits.domain.DepositAccount;
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

import static io.github.deposits.domain.DepositAccount_.ACCOUNT_NUMBER;
import static io.github.deposits.domain.DepositAccount_.CURRENCY_CODE;
import static io.github.deposits.domain.DepositAccount_.MONTH_OF_STUDY;
import static io.github.deposits.domain.DepositAccount_.REVALUED_TOTAL_AMOUNT;

/**
 * Creating a service to fetch a projection with the fields currency-code, volume and number of accounts.
 */
@Slf4j
@Service("currencySummaryReportService")
public class CurrencySummaryReportService implements Report<List<CurrencySummary>, CurrencySummaryParameters> {

    private final SessionFactory sessionFactory;

    public CurrencySummaryReportService(EntityManagerFactory entityManager) {
        this.sessionFactory = entityManager.unwrap(SessionFactory.class);
    }

    @Override
    @Cacheable("currency-summary-report-cache")
    public List<CurrencySummary> createReport(final CurrencySummaryParameters reportParameter) {
        // @formatter:off

        long requestStart = System.currentTimeMillis();
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<CurrencySummary> criteriaQuery = cb.createQuery(CurrencySummary.class);

        Root<DepositAccount> depositRoot = criteriaQuery.from(DepositAccount.class);

        Predicate monthOfStudyPredicate = cb.equal(depositRoot.get(MONTH_OF_STUDY), reportParameter.getMonthOfStudy());
        Predicate noneZeroSumTotalPredicate = cb.notEqual(cb.sum(depositRoot.get(REVALUED_TOTAL_AMOUNT)), BigDecimal.ZERO);

        // Define the SELECT clause
        criteriaQuery.multiselect(
            depositRoot.get(CURRENCY_CODE),
            cb.sum(depositRoot.get(REVALUED_TOTAL_AMOUNT)),
            cb.count(depositRoot.get(ACCOUNT_NUMBER)))
        .groupBy(depositRoot.get(CURRENCY_CODE))
        .where(monthOfStudyPredicate)
        .having(noneZeroSumTotalPredicate);

        List<CurrencySummary> resultList = sessionFactory.createEntityManager().createQuery(criteriaQuery).getResultList();

        log.info("Currency list with {} items, generated in {} ms", resultList.size(), System.currentTimeMillis() - requestStart);

        return resultList;

        // @formatter:on
    }
}
