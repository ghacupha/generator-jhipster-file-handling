package io.github.deposits.app.report;

import io.github.deposits.DepositAnalysisMainApp;
import io.github.deposits.app.model.SBUSummary;
import io.github.deposits.app.report.params.SBUSummaryParameters;
import io.github.deposits.app.resource.decorator.IDepositAccountResource;
import io.github.deposits.domain.DepositAccount;
import io.github.deposits.repository.DepositAccountRepository;
import io.github.deposits.repository.search.DepositAccountSearchRepository;
import io.github.deposits.service.DepositAccountService;
import io.github.deposits.service.dto.DepositAccountDTO;
import io.github.deposits.service.mapper.DepositAccountMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {DepositAnalysisMainApp.class})
public class SBUSummaryReportIT {

    private static final String DEFAULT_SBU_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SBU_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_RM_CODE = "AAAAAAAAAA";
    private static final String UPDATED_RM_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SCHEME_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SCHEME_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_GL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_GL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CUSTOMER_ID = "AAAAAAAAAA";
    private static final String UPDATED_CUSTOMER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_ACCOUNT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_ACCOUNT_BALANCE = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCOUNT_BALANCE = new BigDecimal(2);
    private static final BigDecimal SMALLER_ACCOUNT_BALANCE = new BigDecimal(1 - 1);

    private static final String DEFAULT_SECTOR = "AAAAAAAAAA";
    private static final String UPDATED_SECTOR = "BBBBBBBBBB";

    private static final String DEFAULT_SUB_SECTOR = "AAAAAAAAAA";
    private static final String UPDATED_SUB_SECTOR = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_ACCOUNT_OPENING_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACCOUNT_OPENING_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ACCOUNT_OPENING_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_ACCOUNT_MATURITY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACCOUNT_MATURITY_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_ACCOUNT_MATURITY_DATE = LocalDate.ofEpochDay(-1L);

    private static final String DEFAULT_ACCOUNT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_STATUS = "BBBBBBBBBB";

    private static final Double DEFAULT_RATE = 1D;
    private static final Double UPDATED_RATE = 2D;
    private static final Double SMALLER_RATE = 1D - 1D;

    private static final BigDecimal DEFAULT_BOOKED_INTEREST_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_BOOKED_INTEREST_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_BOOKED_INTEREST_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_INTEREST_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_INTEREST_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_INTEREST_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_ACCRUED_INTEREST_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACCRUED_INTEREST_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_ACCRUED_INTEREST_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_DEPOSIT_SCHEME = "AAAAAAAAAA";
    private static final String UPDATED_DEPOSIT_SCHEME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_REVALUED_TOTAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_REVALUED_TOTAL_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_REVALUED_TOTAL_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_REVALUED_PRINCIPAL_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_REVALUED_PRINCIPAL_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_REVALUED_PRINCIPAL_AMOUNT = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_REVALUED_INTEREST_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_REVALUED_INTEREST_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_REVALUED_INTEREST_AMOUNT = new BigDecimal(1 - 1);

    private static final LocalDate DEFAULT_MONTH_OF_STUDY = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_MONTH_OF_STUDY = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_MONTH_OF_STUDY = LocalDate.ofEpochDay(-1L);

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it, if they test an entity which requires the current entity.
     */
    public static DepositAccount createEntity(EntityManager em) {
        DepositAccount depositAccount = new DepositAccount().sbuCode(DEFAULT_SBU_CODE)
                                                            .rmCode(DEFAULT_RM_CODE)
                                                            .schemeCode(DEFAULT_SCHEME_CODE)
                                                            .glCode(DEFAULT_GL_CODE)
                                                            .currencyCode(DEFAULT_CURRENCY_CODE)
                                                            .customerId(DEFAULT_CUSTOMER_ID)
                                                            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
                                                            .accountName(DEFAULT_ACCOUNT_NAME)
                                                            .accountBalance(DEFAULT_ACCOUNT_BALANCE)
                                                            .sector(DEFAULT_SECTOR)
                                                            .subSector(DEFAULT_SUB_SECTOR)
                                                            .accountOpeningDate(DEFAULT_ACCOUNT_OPENING_DATE)
                                                            .accountMaturityDate(DEFAULT_ACCOUNT_MATURITY_DATE)
                                                            .accountStatus(DEFAULT_ACCOUNT_STATUS)
                                                            .rate(DEFAULT_RATE)
                                                            .bookedInterestAmount(DEFAULT_BOOKED_INTEREST_AMOUNT)
                                                            .interestAmount(DEFAULT_INTEREST_AMOUNT)
                                                            .accruedInterestAmount(DEFAULT_ACCRUED_INTEREST_AMOUNT)
                                                            .depositScheme(DEFAULT_DEPOSIT_SCHEME)
                                                            .revaluedTotalAmount(DEFAULT_REVALUED_TOTAL_AMOUNT)
                                                            .revaluedPrincipalAmount(DEFAULT_REVALUED_PRINCIPAL_AMOUNT)
                                                            .revaluedInterestAmount(DEFAULT_REVALUED_INTEREST_AMOUNT)
                                                            .monthOfStudy(DEFAULT_MONTH_OF_STUDY);
        return depositAccount;
    }

    /**
     * Create an updated entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it, if they test an entity which requires the current entity.
     */
    public static DepositAccount createUpdatedEntity(EntityManager em) {
        DepositAccount depositAccount = new DepositAccount().sbuCode(UPDATED_SBU_CODE)
                                                            .rmCode(UPDATED_RM_CODE)
                                                            .schemeCode(UPDATED_SCHEME_CODE)
                                                            .glCode(UPDATED_GL_CODE)
                                                            .currencyCode(UPDATED_CURRENCY_CODE)
                                                            .customerId(UPDATED_CUSTOMER_ID)
                                                            .accountNumber(UPDATED_ACCOUNT_NUMBER)
                                                            .accountName(UPDATED_ACCOUNT_NAME)
                                                            .accountBalance(UPDATED_ACCOUNT_BALANCE)
                                                            .sector(UPDATED_SECTOR)
                                                            .subSector(UPDATED_SUB_SECTOR)
                                                            .accountOpeningDate(UPDATED_ACCOUNT_OPENING_DATE)
                                                            .accountMaturityDate(UPDATED_ACCOUNT_MATURITY_DATE)
                                                            .accountStatus(UPDATED_ACCOUNT_STATUS)
                                                            .rate(UPDATED_RATE)
                                                            .bookedInterestAmount(UPDATED_BOOKED_INTEREST_AMOUNT)
                                                            .interestAmount(UPDATED_INTEREST_AMOUNT)
                                                            .accruedInterestAmount(UPDATED_ACCRUED_INTEREST_AMOUNT)
                                                            .depositScheme(UPDATED_DEPOSIT_SCHEME)
                                                            .revaluedTotalAmount(UPDATED_REVALUED_TOTAL_AMOUNT)
                                                            .revaluedPrincipalAmount(UPDATED_REVALUED_PRINCIPAL_AMOUNT)
                                                            .revaluedInterestAmount(UPDATED_REVALUED_INTEREST_AMOUNT)
                                                            .monthOfStudy(UPDATED_MONTH_OF_STUDY);
        return depositAccount;
    }

    private DepositAccount depositAccount;
    private DepositAccount depositAccountUpdate;

    /**
     * This repository is mocked in the io.github.deposits.repository.search test package.
     *
     * @see io.github.deposits.repository.search.DepositAccountSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepositAccountSearchRepository mockDepositAccountSearchRepository;

    private MockMvc restDepositAccountMockMvc;

    @Autowired
    private IDepositAccountResource depositAccountResourceDecorator;


    @Autowired
    private EntityManagerFactory entityManager;

    @Autowired
    private EntityManager em;

    @Autowired
    private DepositAccountRepository depositAccountRepository;

    @Autowired
    private DepositAccountMapper depositAccountMapper;

    @Autowired
    private DepositAccountService depositAccountService;

    @Autowired
    private Report<List<SBUSummary>, SBUSummaryParameters> sbuSummaryReportService;

    @BeforeEach
    public void initTest() {
        depositAccount = createEntity(em);
        depositAccountUpdate = createUpdatedEntity(em);
    }

    @Test
    public void sessionFactoryIsInjected() {

        assertThat(entityManager).isNotNull();
        assertThat(sbuSummaryReportService).isNotNull();
    }

    @Test
    @Transactional
    public void createReport() throws Exception {
        int databaseSizeBeforeCreate = depositAccountRepository.findAll().size();

        // Create the DepositAccount
        DepositAccountDTO depositAccountDTO = depositAccountMapper.toDto(depositAccount);
        DepositAccountDTO depositAccountDTOUpdate = depositAccountMapper.toDto(depositAccountUpdate);

        depositAccountRepository.saveAndFlush(depositAccount);
        depositAccountRepository.saveAndFlush(depositAccountUpdate);

        // Validate the DepositAccount in the database
        List<DepositAccount> depositAccountList = depositAccountRepository.findAll();
        assertThat(depositAccountList).hasSize(databaseSizeBeforeCreate + 2);

        List<SBUSummary> sbuSummaries = sbuSummaryReportService.createReport(new SBUSummaryParameters(LocalDate.of(2018, 9, 1)));

        // ! Fix this!
        assertThat(sbuSummaries.size()).isEqualTo(0);
    }
}
