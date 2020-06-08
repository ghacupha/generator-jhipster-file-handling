package io.github.deposits.app.report;

import io.github.deposits.app.model.TableFSummary;
import io.github.deposits.app.report.params.TableFSummaryParameters;
import io.github.deposits.domain.CurrencyTable;
import io.github.deposits.domain.CurrencyTable_;
import io.github.deposits.domain.DepositAccount;
import io.github.deposits.domain.TypeTable;
import io.github.deposits.domain.TypeTable_;
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
import static io.github.deposits.domain.DepositAccount_.GL_CODE;
import static io.github.deposits.domain.DepositAccount_.MONTH_OF_STUDY;
import static io.github.deposits.domain.DepositAccount_.RATE;
import static io.github.deposits.domain.DepositAccount_.REVALUED_INTEREST_AMOUNT;
import static io.github.deposits.domain.DepositAccount_.REVALUED_PRINCIPAL_AMOUNT;
import static io.github.deposits.domain.DepositAccount_.REVALUED_TOTAL_AMOUNT;

@Slf4j
@Service("tableFSummaryReportService")
public class TableFSummaryReportService implements Report<List<TableFSummary>, TableFSummaryParameters> {

    private final SessionFactory sessionFactory;

    public TableFSummaryReportService(EntityManagerFactory entityManager) {
        this.sessionFactory = entityManager.unwrap(SessionFactory.class);
    }

    @Override
    @Cacheable("table-f-summary-report-cache")
    public List<TableFSummary> createReport(final TableFSummaryParameters reportParameter) {

        // @formatter:off
        long requestStart = System.currentTimeMillis();
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<TableFSummary> criteriaQuery = cb.createQuery(TableFSummary.class);

        // Define FROM clause
        Root<DepositAccount> depositRoot = criteriaQuery.from(DepositAccount.class);
        Root<CurrencyTable> currencyRoot = criteriaQuery.from(CurrencyTable.class);
        Root<TypeTable> typeRoot = criteriaQuery.from(TypeTable.class);

        // Predicates
        Predicate currencyLocalePredicate = cb.equal(depositRoot.get(CURRENCY_CODE), currencyRoot.get(CurrencyTable_.CURRENCY_CODE));
        Predicate typeDefPredicate = cb.equal(depositRoot.get(GL_CODE), typeRoot.get(TypeTable_.GL_CODE));
        Predicate monthOfStudyPredicate = cb.equal(depositRoot.get(MONTH_OF_STUDY), reportParameter.getMonthOfStudy());
        Predicate noneZeroSumTotalPredicate = cb.notEqual(cb.sum(depositRoot.get(REVALUED_TOTAL_AMOUNT)), BigDecimal.ZERO);

        // Define the SELECT clause
        criteriaQuery.multiselect(
            currencyRoot.get(CurrencyTable_.LOCALITY),
            typeRoot.get(TypeTable_.TABLE_FDEFINITION),
            cb.sum(depositRoot.get(REVALUED_PRINCIPAL_AMOUNT)),
            cb.sum(depositRoot.get(REVALUED_INTEREST_AMOUNT)),
            cb.sum(depositRoot.get(REVALUED_TOTAL_AMOUNT)),
            cb.quot(cb.sum(cb.prod(depositRoot.get(RATE), depositRoot.get(REVALUED_TOTAL_AMOUNT))), cb.sum(depositRoot.get(REVALUED_TOTAL_AMOUNT))))
         .groupBy(currencyRoot.get(CurrencyTable_.LOCALITY), typeRoot.get(TypeTable_.TABLE_FDEFINITION))
         .where(monthOfStudyPredicate, currencyLocalePredicate, typeDefPredicate)
         .having(noneZeroSumTotalPredicate);

        List<TableFSummary> resultList = sessionFactory.createEntityManager().createQuery(criteriaQuery).getResultList();

        log.info("Table F summary list with {} items, generated in {} ms", resultList.size(), System.currentTimeMillis() - requestStart);

        // @formatter:on
        return resultList;
    }
}
