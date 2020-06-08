package io.github.deposits.app.report;

import com.google.common.collect.ImmutableList;
import io.github.deposits.app.model.BranchSummary;
import io.github.deposits.app.report.params.BranchSummaryParameters;
import io.github.deposits.domain.BranchTable;
import io.github.deposits.domain.BranchTable_;
import io.github.deposits.domain.DepositAccount;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.List;

import static io.github.deposits.domain.DepositAccount_.ACCOUNT_NUMBER;
import static io.github.deposits.domain.DepositAccount_.MONTH_OF_STUDY;
import static io.github.deposits.domain.DepositAccount_.REVALUED_TOTAL_AMOUNT;

/**
 * This class attempts to implement cross join between branch-table and deposit-account-table audaciously
 * correlating the two on the service-outlet-code of the branch which is merely but the substring of the
 * account number itself.
 * There being no expensive sub-select clauses the developer opted to add fields for towns, county and other
 * branch-table items making them all part of the group-by clause.
 * The expensiveness of this operation is known so the developer perhaps against better judgment has
 * co-opted the use of a cache. All APIs calling this object should be aware
 *
 */
@Slf4j
@Service("branchSummaryReportService")
public class BranchSummaryReportService implements Report<List<BranchSummary>, BranchSummaryParameters> {

    private final SessionFactory sessionFactory;

    public BranchSummaryReportService(EntityManagerFactory entityManager) {
        this.sessionFactory = entityManager.unwrap(SessionFactory.class);
    }

    @Override
    @Cacheable("branch-summary-report-cache")
    public List<BranchSummary> createReport(final BranchSummaryParameters reportParameter) {
        // @formatter:off

        long requestStart = System.currentTimeMillis();
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<BranchSummary> criteriaQuery = cb.createQuery(BranchSummary.class);

        Root<BranchTable> branchRoot = criteriaQuery.from(BranchTable.class);
        Root<DepositAccount> depositRoot = criteriaQuery.from(DepositAccount.class);

        // Predicates
        Expression<String> accountNumber = depositRoot.get(ACCOUNT_NUMBER);

        Predicate serviceOutletPredicate = cb.equal(branchRoot.get(BranchTable_.SERVICE_OUTLET_CODE), cb.substring(accountNumber, 1, 3));
        Predicate monthOfStudyPredicate = cb.equal(depositRoot.get(MONTH_OF_STUDY), reportParameter.getMonthOfStudy());
        Predicate noneZeroSumTotalPredicate = cb.notEqual(cb.sum(depositRoot.get(REVALUED_TOTAL_AMOUNT)), BigDecimal.ZERO);

        // GROUP BY clauses
        List<Expression<?>> groupByList =
            ImmutableList.of(
                branchRoot.get(BranchTable_.BRANCH_NAME),
                branchRoot.get(BranchTable_.SERVICE_OUTLET_CODE),
                branchRoot.get(BranchTable_.COUNTY),
                branchRoot.get(BranchTable_.TOWN),
                branchRoot.get(BranchTable_.YEAR_OPENED));

        // Define the SELECT clause
        criteriaQuery.multiselect(
            branchRoot.get(BranchTable_.BRANCH_NAME),
            branchRoot.get(BranchTable_.SERVICE_OUTLET_CODE),
            branchRoot.get(BranchTable_.COUNTY),
            branchRoot.get(BranchTable_.TOWN),
            branchRoot.get(BranchTable_.YEAR_OPENED),
            cb.count(depositRoot.get(ACCOUNT_NUMBER)),
            cb.sum(depositRoot.get(REVALUED_TOTAL_AMOUNT)))
        .groupBy(groupByList)
        .where(monthOfStudyPredicate,serviceOutletPredicate)
        .having(noneZeroSumTotalPredicate);

        List<BranchSummary> resultList = sessionFactory.createEntityManager().createQuery(criteriaQuery).getResultList();

        log.info("Branch list with {} items, generated in {} ms", resultList.size(), System.currentTimeMillis() - requestStart);

        return resultList;

        // @formatter:on
    }
}
