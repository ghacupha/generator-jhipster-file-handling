package io.github.deposits.app.report;

import io.github.deposits.app.model.TableFNewSummary;
import io.github.deposits.app.report.params.TableFNewSummaryParameters;
import io.github.deposits.domain.CurrencyTable;
import io.github.deposits.domain.CurrencyTable_;
import io.github.deposits.domain.DepositAccount;
import io.github.deposits.domain.DepositAccount_;
import io.github.deposits.domain.TableFNewBucket;
import io.github.deposits.domain.TypeTable;
import io.github.deposits.domain.TypeTable_;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import static io.github.deposits.domain.CurrencyTable_.LOCALITY;
import static io.github.deposits.domain.DepositAccount_.ACCOUNT_MATURITY_DATE;
import static io.github.deposits.domain.DepositAccount_.ACCOUNT_OPENING_DATE;
import static io.github.deposits.domain.DepositAccount_.GL_CODE;
import static io.github.deposits.domain.DepositAccount_.MONTH_OF_STUDY;
import static io.github.deposits.domain.DepositAccount_.RATE;
import static io.github.deposits.domain.DepositAccount_.REVALUED_INTEREST_AMOUNT;
import static io.github.deposits.domain.DepositAccount_.REVALUED_PRINCIPAL_AMOUNT;
import static io.github.deposits.domain.DepositAccount_.REVALUED_TOTAL_AMOUNT;
import static io.github.deposits.domain.TableFNewBucket_.BUCKET;
import static io.github.deposits.domain.TableFNewBucket_.NUMBER_OF_DAYS;
import static io.github.deposits.domain.TypeTable_.TABLE_FDEFINITION;

/**
 * In this busy class I have had to manually join data from the DB using a list since I could
 * not find in the JPA a method for performing a union in the database.
 * So i have to different queries ran in the database and the result-list combined in the client method.
 */
@Slf4j
@Service("tableFNewSummaryReportService")
public class TableFNewSummaryReportService implements Report<List<TableFNewSummary>, TableFNewSummaryParameters> {

    private final SessionFactory sessionFactory;

    public TableFNewSummaryReportService(EntityManagerFactory entityManager) {
        this.sessionFactory = entityManager.unwrap(SessionFactory.class);
    }

    @Override
    @Cacheable("table-f-new-summary-report-cache")
    public List<TableFNewSummary> createReport(final TableFNewSummaryParameters reportParameter) {
        long requestStart = System.currentTimeMillis();
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        List<TableFNewSummary> resultList = getTableFNewSummaries(reportParameter, cb, cb.createQuery(TableFNewSummary.class));

        // Now do a UNION in java MuHaHaHa!
        resultList.addAll(getTableFNewSummariesTermDeposits(reportParameter, cb, cb.createQuery(TableFNewSummary.class)));

        resultList.sort(
            Comparator.comparing(TableFNewSummary::getCurrencyLocality)
                      .thenComparing(TableFNewSummary::getDepositType));

        log.info("Table F summary list with {} items, generated in {} ms", resultList.size(), System.currentTimeMillis() - requestStart);

        return resultList;
    }

    private List<TableFNewSummary> getTableFNewSummaries(final TableFNewSummaryParameters reportParameter, final CriteriaBuilder cb, final CriteriaQuery<TableFNewSummary> query) {

        // Define FROM clause
        Root<DepositAccount> depositRoot = query.from(DepositAccount.class);
        Root<CurrencyTable> currencyRoot = query.from(CurrencyTable.class);
        Root<TypeTable> typeRoot = query.from(TypeTable.class);

        // Expressions
        final Expression<String> tableFDefinition = typeRoot.get(TABLE_FDEFINITION);
        final Expression<Number> principalAmount = depositRoot.get(REVALUED_PRINCIPAL_AMOUNT);
        final Expression<Number> interestAmount = depositRoot.get(REVALUED_INTEREST_AMOUNT);
        final Expression<Number> totalAmount = depositRoot.get(REVALUED_TOTAL_AMOUNT);
        final Expression<Number> interestRate = depositRoot.get(RATE);
        final Expression<String> currencyLocale = currencyRoot.get(LOCALITY);
        final Path<Object> depositGlCode = depositRoot.get(GL_CODE);
        final Path<Object> glCodeDefinition = typeRoot.get(TypeTable_.GL_CODE);
        final Path<Object> depositMonthOfStudy = depositRoot.get(MONTH_OF_STUDY);
        final Path<Object> depositCurrencyCode = depositRoot.get(DepositAccount_.CURRENCY_CODE);
        final Path<Object> currencyCodeDefinition = currencyRoot.get(CurrencyTable_.CURRENCY_CODE);

        Predicate currencyLocalePredicate = cb.equal(depositCurrencyCode, currencyCodeDefinition);
        Predicate typeDefPredicate = cb.equal(depositGlCode, glCodeDefinition);
        Predicate monthOfStudyPredicate = cb.equal(depositMonthOfStudy, reportParameter.getMonthOfStudy());
        Predicate nonTermDepositPredicate = cb.notEqual(tableFDefinition, "TERM DEPOSITS");
        Predicate noneZeroSumTotalPredicate = cb.notEqual(cb.sum(totalAmount), BigDecimal.ZERO);


        // Define the SELECT clause for non-term deposits
        query.multiselect(
            currencyLocale,
            tableFDefinition,
            cb.sum(principalAmount),
            cb.sum(interestAmount),
            cb.sum(totalAmount),
            cb.quot(cb.sum(cb.prod(interestRate, totalAmount)), cb.sum(totalAmount)))
             .groupBy(currencyLocale, tableFDefinition)
             .where(monthOfStudyPredicate, currencyLocalePredicate, typeDefPredicate)
             .having(noneZeroSumTotalPredicate, nonTermDepositPredicate);

        return sessionFactory.createEntityManager().createQuery(query).getResultList();
    }

    private List<TableFNewSummary> getTableFNewSummariesTermDeposits(final TableFNewSummaryParameters reportParameter, final CriteriaBuilder cb, final CriteriaQuery<TableFNewSummary> query) {
        // @formatter:off

        // Define FROM clause For term deposits
        Root<DepositAccount> depositRoot = query.from(DepositAccount.class);
        Root<CurrencyTable> currencyRoot = query.from(CurrencyTable.class);
        Root<TypeTable> typeRoot = query.from(TypeTable.class);
        Root<TableFNewBucket> bucketRoot = query.from(TableFNewBucket.class);

        final Path<Object> depositCurrencyCode = depositRoot.get(DepositAccount_.CURRENCY_CODE);
        final Path<Object> tableFDefinition = typeRoot.get(TABLE_FDEFINITION);
        final Path<String> bucketName = bucketRoot.get(BUCKET);
        final Path<Integer> bucketNoOfDays = bucketRoot.get(NUMBER_OF_DAYS);
        final Path<Object> currencyLocale = currencyRoot.get(LOCALITY);
        final Path<Number> principalAmount = depositRoot.get(REVALUED_PRINCIPAL_AMOUNT);
        final Path<Number> interestAmount = depositRoot.get(REVALUED_INTEREST_AMOUNT);
        final Path<Number> totalAmount = depositRoot.get(REVALUED_TOTAL_AMOUNT);
        final Path<Number> interestRate = depositRoot.get(RATE);
        final Path<Object> glCode = depositRoot.get(GL_CODE);
        final Path<Object> currencyCodeDefinition = currencyRoot.get(CurrencyTable_.CURRENCY_CODE);
        final Path<Object> glCodeDefinition = typeRoot.get(TypeTable_.GL_CODE);
        final Path<Object> depositMonthOfStudy = depositRoot.get(MONTH_OF_STUDY);
        final Expression<Integer> maturityDate = depositRoot.get(ACCOUNT_MATURITY_DATE);
        final Expression<Integer> openingDate = depositRoot.get(ACCOUNT_OPENING_DATE);
        final Expression<Integer> noOfDays = cb.diff(maturityDate, openingDate);

        Predicate currencyLocalePredicate = cb.equal(depositCurrencyCode, currencyCodeDefinition);
        Predicate typeDefPredicate = cb.equal(glCode, glCodeDefinition);
        Predicate monthOfStudyPredicate = cb.equal(depositMonthOfStudy, reportParameter.getMonthOfStudy());
        Predicate termDepositPredicate = cb.equal(tableFDefinition, "TERM DEPOSITS");
        Predicate noneZeroSumTotalPredicate = cb.notEqual(cb.sum(totalAmount), BigDecimal.ZERO);
        Predicate bucketPredicate = cb.greaterThan(noOfDays,bucketNoOfDays);

         query.multiselect(
              currencyLocale,
              bucketName,
              cb.sum(principalAmount),
              cb.sum(interestAmount),
              cb.sum(totalAmount),
              cb.quot(cb.sum(cb.prod(interestRate, totalAmount)), cb.sum(totalAmount)))
         .groupBy(currencyLocale, tableFDefinition, bucketName)
         .where(monthOfStudyPredicate, bucketPredicate, currencyLocalePredicate, typeDefPredicate)
         .having(noneZeroSumTotalPredicate, termDepositPredicate);

        return sessionFactory.createEntityManager().createQuery(query).getResultList();
        // @formatter:on
    }

    // TODO REFACTOR THIS!!!

}
