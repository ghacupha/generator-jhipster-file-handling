package io.github.deposits.app.report;

import io.github.deposits.app.model.DPFSummary;
import io.github.deposits.app.report.params.DPFSummaryParameters;
import io.github.deposits.domain.DPFBucket;
import io.github.deposits.domain.DPFBucket_;
import io.github.deposits.domain.DepositAccount;
import io.github.deposits.domain.TypeTable;
import io.github.deposits.domain.TypeTable_;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
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

import static io.github.deposits.domain.DepositAccount_.ACCOUNT_NUMBER;
import static io.github.deposits.domain.DepositAccount_.GL_CODE;
import static io.github.deposits.domain.DepositAccount_.MONTH_OF_STUDY;
import static io.github.deposits.domain.DepositAccount_.RATE;
import static io.github.deposits.domain.DepositAccount_.REVALUED_INTEREST_AMOUNT;
import static io.github.deposits.domain.DepositAccount_.REVALUED_PRINCIPAL_AMOUNT;
import static io.github.deposits.domain.DepositAccount_.REVALUED_TOTAL_AMOUNT;
import static io.github.deposits.domain.TypeTable_.DPF_DEFINITION;

@Slf4j
@Service("dpfSummaryReportService")
public class DPFSummaryReportService implements Report<List<DPFSummary>, DPFSummaryParameters>  {


    private final SessionFactory sessionFactory;

    public DPFSummaryReportService(EntityManagerFactory entityManager) {
        this.sessionFactory = entityManager.unwrap(SessionFactory.class);
    }

    @Override
    public List<DPFSummary> createReport(final DPFSummaryParameters reportParameter) {
        long requestStart = System.currentTimeMillis();
        CriteriaBuilder cb = sessionFactory.getCriteriaBuilder();
        List<DPFSummary> resultList = getSummary(reportParameter, cb, cb.createQuery(DPFSummary.class));

        resultList.sort(Comparator.comparing(DPFSummary::getDepositRange).thenComparing(DPFSummary::getTypeOfDeposit));

        log.info("Table F summary list with {} items, generated in {} ms", resultList.size(), System.currentTimeMillis() - requestStart);

        return resultList;
    }

    private List<DPFSummary> getSummary(final DPFSummaryParameters reportParameter, final CriteriaBuilder cb, final CriteriaQuery<DPFSummary> query) {

        // Define FROM clause
        Root<DepositAccount> depositRoot = query.from(DepositAccount.class);
        Root<TypeTable> typeRoot = query.from(TypeTable.class);
        Root<DPFBucket> dpfBucketRoot = query.from(DPFBucket.class);

        // Expressions
        final Expression<String> dpfDefinition = typeRoot.get(DPF_DEFINITION);
        final Expression<String> dpfRangeDefinition = dpfBucketRoot.get(DPFBucket_.BUCKET);
        final Expression<Number> principalAmount = depositRoot.get(REVALUED_PRINCIPAL_AMOUNT);
        final Expression<Number> interestAmount = depositRoot.get(REVALUED_INTEREST_AMOUNT);
        final Expression<BigDecimal> totalAmount = depositRoot.get(REVALUED_TOTAL_AMOUNT);
        final Path<BigDecimal> dpfBucketAmount = dpfBucketRoot.get(DPFBucket_.DEPOSIT_AMOUNT);
        final Expression<Number> interestRate = depositRoot.get(RATE);
        final Path<Object> depositAccount = depositRoot.get(ACCOUNT_NUMBER);
        final Path<Object> depositGlCode = depositRoot.get(GL_CODE);
        final Path<Object> glCodeDefinition = typeRoot.get(TypeTable_.GL_CODE);
        final Path<Object> depositMonthOfStudy = depositRoot.get(MONTH_OF_STUDY);

        Predicate typeDefPredicate = cb.equal(depositGlCode, glCodeDefinition);
        Predicate monthOfStudyPredicate = cb.equal(depositMonthOfStudy, reportParameter.getMonthOfStudy());
        Predicate noneZeroSumTotalPredicate = cb.notEqual(cb.sum(totalAmount), BigDecimal.ZERO);
        Predicate depositAmountPredicate = cb.greaterThan(totalAmount, dpfBucketAmount);


        // Define the SELECT clause for non-term deposits
        query.multiselect(
            dpfRangeDefinition,
            dpfDefinition,
            cb.count(totalAmount),
            cb.sum(totalAmount),
            cb.sum(principalAmount),
            cb.sum(interestAmount),
            cb.quot(cb.sum(cb.prod(interestRate, totalAmount)), cb.sum(totalAmount)))
         .groupBy(dpfRangeDefinition, dpfDefinition)
         .where(monthOfStudyPredicate, depositAmountPredicate, typeDefPredicate)
         .having(noneZeroSumTotalPredicate);

        // TODO create matching dpf-summary projection

        return sessionFactory.createEntityManager().createQuery(query).getResultList();
    }
}
