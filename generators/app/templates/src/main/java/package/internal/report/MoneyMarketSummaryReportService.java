package io.github.deposits.app.report;

import io.github.deposits.app.model.MoneyMarketSummary;
import io.github.deposits.app.report.params.MoneyMarketSummaryParameters;
import io.github.deposits.domain.CurrencyTable;
import io.github.deposits.domain.CurrencyTable_;
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

import static io.github.deposits.domain.DepositAccount_.CURRENCY_CODE;
import static io.github.deposits.domain.DepositAccount_.MONTH_OF_STUDY;
import static io.github.deposits.domain.DepositAccount_.RATE;
import static io.github.deposits.domain.DepositAccount_.REVALUED_INTEREST_AMOUNT;
import static io.github.deposits.domain.DepositAccount_.REVALUED_PRINCIPAL_AMOUNT;
import static io.github.deposits.domain.DepositAccount_.REVALUED_TOTAL_AMOUNT;
import static io.github.deposits.domain.DepositAccount_.SBU_CODE;

@Slf4j
@Service("moneyMarketSummaryReportService")
public class MoneyMarketSummaryReportService implements Report<List<MoneyMarketSummary>, MoneyMarketSummaryParameters> {

    private final SessionFactory sessionFactory;

    public MoneyMarketSummaryReportService(EntityManagerFactory entityManager) {
        this.sessionFactory = entityManager.unwrap(SessionFactory.class);
    }

    // @formatter:off
    @Override
    @Cacheable("money-market-summary-report-cache")
    public List<MoneyMarketSummary> createReport(final MoneyMarketSummaryParameters reportParameter) {

        long requestStart = System.currentTimeMillis();
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<MoneyMarketSummary> criteriaQuery = cb.createQuery(MoneyMarketSummary.class);
        Root<DepositAccount> depositRoot = criteriaQuery.from(DepositAccount.class);
        Root<CurrencyTable> currencyRoot = criteriaQuery.from(CurrencyTable.class);

        // Predicates
        Predicate currencyLocalePredicate = cb.equal(depositRoot.get(CURRENCY_CODE), currencyRoot.get(CurrencyTable_.CURRENCY_CODE));
        Predicate monthOfStudyPredicate = cb.equal(depositRoot.get(MONTH_OF_STUDY), reportParameter.getMonthOfStudy());
        Predicate noneZeroSumTotalPredicate = cb.notEqual(cb.sum(depositRoot.get(REVALUED_TOTAL_AMOUNT)), BigDecimal.ZERO);

        // Define the SELECT clause
        criteriaQuery.multiselect(
            currencyRoot.get(CurrencyTable_.LOCALITY),
            depositRoot.get(SBU_CODE),
            cb.sum(depositRoot.get(REVALUED_PRINCIPAL_AMOUNT)),
            cb.sum(depositRoot.get(REVALUED_INTEREST_AMOUNT)),
            cb.sum(depositRoot.get(REVALUED_TOTAL_AMOUNT)),
            cb.min(depositRoot.get(RATE)),
            cb.max(depositRoot.get(RATE)),
                                  //                cb.avg(root.get("rate")))
            cb.quot(cb.sum(cb.prod(depositRoot.get(RATE), depositRoot.get(REVALUED_TOTAL_AMOUNT))), cb.sum(depositRoot.get(REVALUED_TOTAL_AMOUNT))))
        .groupBy(currencyRoot.get(CurrencyTable_.LOCALITY), depositRoot.get(SBU_CODE))
        .where(monthOfStudyPredicate, currencyLocalePredicate)
        .having(noneZeroSumTotalPredicate);

        List<MoneyMarketSummary> resultList = sessionFactory.createEntityManager().createQuery(criteriaQuery).getResultList();

        log.info("Money market list with {} items, generated in {} ms", resultList.size(), System.currentTimeMillis() - requestStart);

        return resultList;
    }
    // @formatter:on
}
