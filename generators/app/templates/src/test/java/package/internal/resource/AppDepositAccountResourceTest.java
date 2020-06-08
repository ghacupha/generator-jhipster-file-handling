package io.github.deposits.app.resource;

import io.github.deposits.DepositAnalysisMainApp;
import io.github.deposits.app.resource.decorator.IDepositAccountResource;
import io.github.deposits.domain.DepositAccount;
import io.github.deposits.repository.DepositAccountRepository;
import io.github.deposits.repository.search.DepositAccountSearchRepository;
import io.github.deposits.service.DepositAccountQueryService;
import io.github.deposits.service.DepositAccountService;
import io.github.deposits.service.dto.DepositAccountDTO;
import io.github.deposits.service.mapper.DepositAccountMapper;
import io.github.deposits.web.rest.TestUtil;
import io.github.deposits.web.rest.errors.ExceptionTranslator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static io.github.deposits.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {DepositAnalysisMainApp.class})
public class AppDepositAccountResourceTest {


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

    @Autowired
    private DepositAccountRepository depositAccountRepository;

    @Autowired
    private DepositAccountMapper depositAccountMapper;

    @Autowired
    private DepositAccountService depositAccountService;

    /**
     * This repository is mocked in the io.github.deposits.repository.search test package.
     *
     * @see io.github.deposits.repository.search.DepositAccountSearchRepositoryMockConfiguration
     */
    @Autowired
    private DepositAccountSearchRepository mockDepositAccountSearchRepository;

    @Autowired
    private DepositAccountQueryService depositAccountQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restDepositAccountMockMvc;

    private DepositAccount depositAccount;

    @Autowired
    private IDepositAccountResource depositAccountResourceDecorator;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final IDepositAccountResource depositAccountResource = new AppDepositAccountResource(depositAccountQueryService, depositAccountResourceDecorator);
        this.restDepositAccountMockMvc = MockMvcBuilders.standaloneSetup(depositAccountResource)
                                                        .setCustomArgumentResolvers(pageableArgumentResolver)
                                                        .setControllerAdvice(exceptionTranslator)
                                                        .setConversionService(createFormattingConversionService())
                                                        .setMessageConverters(jacksonMessageConverter)
                                                        .setValidator(validator)
                                                        .build();
    }

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
                                                            .revaluedInterestAmount(DEFAULT_REVALUED_INTEREST_AMOUNT);
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
                                                            .revaluedInterestAmount(UPDATED_REVALUED_INTEREST_AMOUNT);
        return depositAccount;
    }

    @BeforeEach
    public void initTest() {
        depositAccount = createEntity(em);
    }

    @Test
    @Transactional
    public void createDepositAccount() throws Exception {
        int databaseSizeBeforeCreate = depositAccountRepository.findAll().size();

        // Create the DepositAccount
        DepositAccountDTO depositAccountDTO = depositAccountMapper.toDto(depositAccount);
        restDepositAccountMockMvc.perform(post("/api/app/deposit-accounts").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(depositAccountDTO)))
                                 .andExpect(status().isCreated());

        // Validate the DepositAccount in the database
        List<DepositAccount> depositAccountList = depositAccountRepository.findAll();
        assertThat(depositAccountList).hasSize(databaseSizeBeforeCreate + 1);
        DepositAccount testDepositAccount = depositAccountList.get(depositAccountList.size() - 1);
        assertThat(testDepositAccount.getSbuCode()).isEqualTo(DEFAULT_SBU_CODE);
        assertThat(testDepositAccount.getRmCode()).isEqualTo(DEFAULT_RM_CODE);
        assertThat(testDepositAccount.getSchemeCode()).isEqualTo(DEFAULT_SCHEME_CODE);
        assertThat(testDepositAccount.getGlCode()).isEqualTo(DEFAULT_GL_CODE);
        assertThat(testDepositAccount.getCurrencyCode()).isEqualTo(DEFAULT_CURRENCY_CODE);
        assertThat(testDepositAccount.getCustomerId()).isEqualTo(DEFAULT_CUSTOMER_ID);
        assertThat(testDepositAccount.getAccountNumber()).isEqualTo(DEFAULT_ACCOUNT_NUMBER);
        assertThat(testDepositAccount.getAccountName()).isEqualTo(DEFAULT_ACCOUNT_NAME);
        assertThat(testDepositAccount.getAccountBalance()).isEqualTo(DEFAULT_ACCOUNT_BALANCE);
        assertThat(testDepositAccount.getSector()).isEqualTo(DEFAULT_SECTOR);
        assertThat(testDepositAccount.getSubSector()).isEqualTo(DEFAULT_SUB_SECTOR);
        assertThat(testDepositAccount.getAccountOpeningDate()).isEqualTo(DEFAULT_ACCOUNT_OPENING_DATE);
        assertThat(testDepositAccount.getAccountMaturityDate()).isEqualTo(DEFAULT_ACCOUNT_MATURITY_DATE);
        assertThat(testDepositAccount.getAccountStatus()).isEqualTo(DEFAULT_ACCOUNT_STATUS);
        assertThat(testDepositAccount.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testDepositAccount.getBookedInterestAmount()).isEqualTo(DEFAULT_BOOKED_INTEREST_AMOUNT);
        assertThat(testDepositAccount.getInterestAmount()).isEqualTo(DEFAULT_INTEREST_AMOUNT);
        assertThat(testDepositAccount.getAccruedInterestAmount()).isEqualTo(DEFAULT_ACCRUED_INTEREST_AMOUNT);
        assertThat(testDepositAccount.getDepositScheme()).isEqualTo(DEFAULT_DEPOSIT_SCHEME);
        assertThat(testDepositAccount.getRevaluedTotalAmount()).isEqualTo(DEFAULT_REVALUED_TOTAL_AMOUNT);
        assertThat(testDepositAccount.getRevaluedPrincipalAmount()).isEqualTo(DEFAULT_REVALUED_PRINCIPAL_AMOUNT);
        assertThat(testDepositAccount.getRevaluedInterestAmount()).isEqualTo(DEFAULT_REVALUED_INTEREST_AMOUNT);

        // Validate the DepositAccount in Elasticsearch
        verify(mockDepositAccountSearchRepository, times(1)).save(testDepositAccount);
    }

    @Test
    @Transactional
    public void createDepositAccountWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = depositAccountRepository.findAll().size();

        // Create the DepositAccount with an existing ID
        depositAccount.setId(1L);
        DepositAccountDTO depositAccountDTO = depositAccountMapper.toDto(depositAccount);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDepositAccountMockMvc.perform(post("/api/app/deposit-accounts").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(depositAccountDTO)))
                                 .andExpect(status().isBadRequest());

        // Validate the DepositAccount in the database
        List<DepositAccount> depositAccountList = depositAccountRepository.findAll();
        assertThat(depositAccountList).hasSize(databaseSizeBeforeCreate);

        // Validate the DepositAccount in Elasticsearch
        verify(mockDepositAccountSearchRepository, times(0)).save(depositAccount);
    }


    @Test
    @Transactional
    public void getAllDepositAccounts() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList
        restDepositAccountMockMvc.perform(get("/api/app/deposit-accounts?sort=id,desc"))
                                 .andExpect(status().isOk())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                                 .andExpect(jsonPath("$.[*].id").value(hasItem(depositAccount.getId().intValue())))
                                 .andExpect(jsonPath("$.[*].sbuCode").value(hasItem(DEFAULT_SBU_CODE)))
                                 .andExpect(jsonPath("$.[*].rmCode").value(hasItem(DEFAULT_RM_CODE)))
                                 .andExpect(jsonPath("$.[*].schemeCode").value(hasItem(DEFAULT_SCHEME_CODE)))
                                 .andExpect(jsonPath("$.[*].glCode").value(hasItem(DEFAULT_GL_CODE)))
                                 .andExpect(jsonPath("$.[*].currencyCode").value(hasItem(DEFAULT_CURRENCY_CODE)))
                                 .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
                                 .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
                                 .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
                                 .andExpect(jsonPath("$.[*].accountBalance").value(hasItem(DEFAULT_ACCOUNT_BALANCE.intValue())))
                                 .andExpect(jsonPath("$.[*].sector").value(hasItem(DEFAULT_SECTOR)))
                                 .andExpect(jsonPath("$.[*].subSector").value(hasItem(DEFAULT_SUB_SECTOR)))
                                 .andExpect(jsonPath("$.[*].accountOpeningDate").value(hasItem(DEFAULT_ACCOUNT_OPENING_DATE.toString())))
                                 .andExpect(jsonPath("$.[*].accountMaturityDate").value(hasItem(DEFAULT_ACCOUNT_MATURITY_DATE.toString())))
                                 .andExpect(jsonPath("$.[*].accountStatus").value(hasItem(DEFAULT_ACCOUNT_STATUS)))
                                 .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())))
                                 .andExpect(jsonPath("$.[*].bookedInterestAmount").value(hasItem(DEFAULT_BOOKED_INTEREST_AMOUNT.intValue())))
                                 .andExpect(jsonPath("$.[*].interestAmount").value(hasItem(DEFAULT_INTEREST_AMOUNT.intValue())))
                                 .andExpect(jsonPath("$.[*].accruedInterestAmount").value(hasItem(DEFAULT_ACCRUED_INTEREST_AMOUNT.intValue())))
                                 .andExpect(jsonPath("$.[*].depositScheme").value(hasItem(DEFAULT_DEPOSIT_SCHEME)))
                                 .andExpect(jsonPath("$.[*].revaluedTotalAmount").value(hasItem(DEFAULT_REVALUED_TOTAL_AMOUNT.intValue())))
                                 .andExpect(jsonPath("$.[*].revaluedPrincipalAmount").value(hasItem(DEFAULT_REVALUED_PRINCIPAL_AMOUNT.intValue())))
                                 .andExpect(jsonPath("$.[*].revaluedInterestAmount").value(hasItem(DEFAULT_REVALUED_INTEREST_AMOUNT.intValue())));
    }

    @Test
    @Transactional
    public void getDepositAccount() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get the depositAccount
        restDepositAccountMockMvc.perform(get("/api/app/deposit-accounts/{id}", depositAccount.getId()))
                                 .andExpect(status().isOk())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                                 .andExpect(jsonPath("$.id").value(depositAccount.getId().intValue()))
                                 .andExpect(jsonPath("$.sbuCode").value(DEFAULT_SBU_CODE))
                                 .andExpect(jsonPath("$.rmCode").value(DEFAULT_RM_CODE))
                                 .andExpect(jsonPath("$.schemeCode").value(DEFAULT_SCHEME_CODE))
                                 .andExpect(jsonPath("$.glCode").value(DEFAULT_GL_CODE))
                                 .andExpect(jsonPath("$.currencyCode").value(DEFAULT_CURRENCY_CODE))
                                 .andExpect(jsonPath("$.customerId").value(DEFAULT_CUSTOMER_ID))
                                 .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER))
                                 .andExpect(jsonPath("$.accountName").value(DEFAULT_ACCOUNT_NAME))
                                 .andExpect(jsonPath("$.accountBalance").value(DEFAULT_ACCOUNT_BALANCE.intValue()))
                                 .andExpect(jsonPath("$.sector").value(DEFAULT_SECTOR))
                                 .andExpect(jsonPath("$.subSector").value(DEFAULT_SUB_SECTOR))
                                 .andExpect(jsonPath("$.accountOpeningDate").value(DEFAULT_ACCOUNT_OPENING_DATE.toString()))
                                 .andExpect(jsonPath("$.accountMaturityDate").value(DEFAULT_ACCOUNT_MATURITY_DATE.toString()))
                                 .andExpect(jsonPath("$.accountStatus").value(DEFAULT_ACCOUNT_STATUS))
                                 .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.doubleValue()))
                                 .andExpect(jsonPath("$.bookedInterestAmount").value(DEFAULT_BOOKED_INTEREST_AMOUNT.intValue()))
                                 .andExpect(jsonPath("$.interestAmount").value(DEFAULT_INTEREST_AMOUNT.intValue()))
                                 .andExpect(jsonPath("$.accruedInterestAmount").value(DEFAULT_ACCRUED_INTEREST_AMOUNT.intValue()))
                                 .andExpect(jsonPath("$.depositScheme").value(DEFAULT_DEPOSIT_SCHEME))
                                 .andExpect(jsonPath("$.revaluedTotalAmount").value(DEFAULT_REVALUED_TOTAL_AMOUNT.intValue()))
                                 .andExpect(jsonPath("$.revaluedPrincipalAmount").value(DEFAULT_REVALUED_PRINCIPAL_AMOUNT.intValue()))
                                 .andExpect(jsonPath("$.revaluedInterestAmount").value(DEFAULT_REVALUED_INTEREST_AMOUNT.intValue()));
    }


    @Test
    @Transactional
    public void getDepositAccountsByIdFiltering() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        Long id = depositAccount.getId();

        defaultDepositAccountShouldBeFound("id.equals=" + id);
        defaultDepositAccountShouldNotBeFound("id.notEquals=" + id);

        defaultDepositAccountShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultDepositAccountShouldNotBeFound("id.greaterThan=" + id);

        defaultDepositAccountShouldBeFound("id.lessThanOrEqual=" + id);
        defaultDepositAccountShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsBySbuCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where sbuCode equals to DEFAULT_SBU_CODE
        defaultDepositAccountShouldBeFound("sbuCode.equals=" + DEFAULT_SBU_CODE);

        // Get all the depositAccountList where sbuCode equals to UPDATED_SBU_CODE
        defaultDepositAccountShouldNotBeFound("sbuCode.equals=" + UPDATED_SBU_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySbuCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where sbuCode not equals to DEFAULT_SBU_CODE
        defaultDepositAccountShouldNotBeFound("sbuCode.notEquals=" + DEFAULT_SBU_CODE);

        // Get all the depositAccountList where sbuCode not equals to UPDATED_SBU_CODE
        defaultDepositAccountShouldBeFound("sbuCode.notEquals=" + UPDATED_SBU_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySbuCodeIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where sbuCode in DEFAULT_SBU_CODE or UPDATED_SBU_CODE
        defaultDepositAccountShouldBeFound("sbuCode.in=" + DEFAULT_SBU_CODE + "," + UPDATED_SBU_CODE);

        // Get all the depositAccountList where sbuCode equals to UPDATED_SBU_CODE
        defaultDepositAccountShouldNotBeFound("sbuCode.in=" + UPDATED_SBU_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySbuCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where sbuCode is not null
        defaultDepositAccountShouldBeFound("sbuCode.specified=true");

        // Get all the depositAccountList where sbuCode is null
        defaultDepositAccountShouldNotBeFound("sbuCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySbuCodeContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where sbuCode contains DEFAULT_SBU_CODE
        defaultDepositAccountShouldBeFound("sbuCode.contains=" + DEFAULT_SBU_CODE);

        // Get all the depositAccountList where sbuCode contains UPDATED_SBU_CODE
        defaultDepositAccountShouldNotBeFound("sbuCode.contains=" + UPDATED_SBU_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySbuCodeNotContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where sbuCode does not contain DEFAULT_SBU_CODE
        defaultDepositAccountShouldNotBeFound("sbuCode.doesNotContain=" + DEFAULT_SBU_CODE);

        // Get all the depositAccountList where sbuCode does not contain UPDATED_SBU_CODE
        defaultDepositAccountShouldBeFound("sbuCode.doesNotContain=" + UPDATED_SBU_CODE);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsByRmCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where rmCode equals to DEFAULT_RM_CODE
        defaultDepositAccountShouldBeFound("rmCode.equals=" + DEFAULT_RM_CODE);

        // Get all the depositAccountList where rmCode equals to UPDATED_RM_CODE
        defaultDepositAccountShouldNotBeFound("rmCode.equals=" + UPDATED_RM_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRmCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where rmCode not equals to DEFAULT_RM_CODE
        defaultDepositAccountShouldNotBeFound("rmCode.notEquals=" + DEFAULT_RM_CODE);

        // Get all the depositAccountList where rmCode not equals to UPDATED_RM_CODE
        defaultDepositAccountShouldBeFound("rmCode.notEquals=" + UPDATED_RM_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRmCodeIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where rmCode in DEFAULT_RM_CODE or UPDATED_RM_CODE
        defaultDepositAccountShouldBeFound("rmCode.in=" + DEFAULT_RM_CODE + "," + UPDATED_RM_CODE);

        // Get all the depositAccountList where rmCode equals to UPDATED_RM_CODE
        defaultDepositAccountShouldNotBeFound("rmCode.in=" + UPDATED_RM_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRmCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where rmCode is not null
        defaultDepositAccountShouldBeFound("rmCode.specified=true");

        // Get all the depositAccountList where rmCode is null
        defaultDepositAccountShouldNotBeFound("rmCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRmCodeContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where rmCode contains DEFAULT_RM_CODE
        defaultDepositAccountShouldBeFound("rmCode.contains=" + DEFAULT_RM_CODE);

        // Get all the depositAccountList where rmCode contains UPDATED_RM_CODE
        defaultDepositAccountShouldNotBeFound("rmCode.contains=" + UPDATED_RM_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRmCodeNotContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where rmCode does not contain DEFAULT_RM_CODE
        defaultDepositAccountShouldNotBeFound("rmCode.doesNotContain=" + DEFAULT_RM_CODE);

        // Get all the depositAccountList where rmCode does not contain UPDATED_RM_CODE
        defaultDepositAccountShouldBeFound("rmCode.doesNotContain=" + UPDATED_RM_CODE);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsBySchemeCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where schemeCode equals to DEFAULT_SCHEME_CODE
        defaultDepositAccountShouldBeFound("schemeCode.equals=" + DEFAULT_SCHEME_CODE);

        // Get all the depositAccountList where schemeCode equals to UPDATED_SCHEME_CODE
        defaultDepositAccountShouldNotBeFound("schemeCode.equals=" + UPDATED_SCHEME_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySchemeCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where schemeCode not equals to DEFAULT_SCHEME_CODE
        defaultDepositAccountShouldNotBeFound("schemeCode.notEquals=" + DEFAULT_SCHEME_CODE);

        // Get all the depositAccountList where schemeCode not equals to UPDATED_SCHEME_CODE
        defaultDepositAccountShouldBeFound("schemeCode.notEquals=" + UPDATED_SCHEME_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySchemeCodeIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where schemeCode in DEFAULT_SCHEME_CODE or UPDATED_SCHEME_CODE
        defaultDepositAccountShouldBeFound("schemeCode.in=" + DEFAULT_SCHEME_CODE + "," + UPDATED_SCHEME_CODE);

        // Get all the depositAccountList where schemeCode equals to UPDATED_SCHEME_CODE
        defaultDepositAccountShouldNotBeFound("schemeCode.in=" + UPDATED_SCHEME_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySchemeCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where schemeCode is not null
        defaultDepositAccountShouldBeFound("schemeCode.specified=true");

        // Get all the depositAccountList where schemeCode is null
        defaultDepositAccountShouldNotBeFound("schemeCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySchemeCodeContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where schemeCode contains DEFAULT_SCHEME_CODE
        defaultDepositAccountShouldBeFound("schemeCode.contains=" + DEFAULT_SCHEME_CODE);

        // Get all the depositAccountList where schemeCode contains UPDATED_SCHEME_CODE
        defaultDepositAccountShouldNotBeFound("schemeCode.contains=" + UPDATED_SCHEME_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySchemeCodeNotContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where schemeCode does not contain DEFAULT_SCHEME_CODE
        defaultDepositAccountShouldNotBeFound("schemeCode.doesNotContain=" + DEFAULT_SCHEME_CODE);

        // Get all the depositAccountList where schemeCode does not contain UPDATED_SCHEME_CODE
        defaultDepositAccountShouldBeFound("schemeCode.doesNotContain=" + UPDATED_SCHEME_CODE);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsByGlCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where glCode equals to DEFAULT_GL_CODE
        defaultDepositAccountShouldBeFound("glCode.equals=" + DEFAULT_GL_CODE);

        // Get all the depositAccountList where glCode equals to UPDATED_GL_CODE
        defaultDepositAccountShouldNotBeFound("glCode.equals=" + UPDATED_GL_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByGlCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where glCode not equals to DEFAULT_GL_CODE
        defaultDepositAccountShouldNotBeFound("glCode.notEquals=" + DEFAULT_GL_CODE);

        // Get all the depositAccountList where glCode not equals to UPDATED_GL_CODE
        defaultDepositAccountShouldBeFound("glCode.notEquals=" + UPDATED_GL_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByGlCodeIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where glCode in DEFAULT_GL_CODE or UPDATED_GL_CODE
        defaultDepositAccountShouldBeFound("glCode.in=" + DEFAULT_GL_CODE + "," + UPDATED_GL_CODE);

        // Get all the depositAccountList where glCode equals to UPDATED_GL_CODE
        defaultDepositAccountShouldNotBeFound("glCode.in=" + UPDATED_GL_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByGlCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where glCode is not null
        defaultDepositAccountShouldBeFound("glCode.specified=true");

        // Get all the depositAccountList where glCode is null
        defaultDepositAccountShouldNotBeFound("glCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByGlCodeContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where glCode contains DEFAULT_GL_CODE
        defaultDepositAccountShouldBeFound("glCode.contains=" + DEFAULT_GL_CODE);

        // Get all the depositAccountList where glCode contains UPDATED_GL_CODE
        defaultDepositAccountShouldNotBeFound("glCode.contains=" + UPDATED_GL_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByGlCodeNotContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where glCode does not contain DEFAULT_GL_CODE
        defaultDepositAccountShouldNotBeFound("glCode.doesNotContain=" + DEFAULT_GL_CODE);

        // Get all the depositAccountList where glCode does not contain UPDATED_GL_CODE
        defaultDepositAccountShouldBeFound("glCode.doesNotContain=" + UPDATED_GL_CODE);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsByCurrencyCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where currencyCode equals to DEFAULT_CURRENCY_CODE
        defaultDepositAccountShouldBeFound("currencyCode.equals=" + DEFAULT_CURRENCY_CODE);

        // Get all the depositAccountList where currencyCode equals to UPDATED_CURRENCY_CODE
        defaultDepositAccountShouldNotBeFound("currencyCode.equals=" + UPDATED_CURRENCY_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByCurrencyCodeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where currencyCode not equals to DEFAULT_CURRENCY_CODE
        defaultDepositAccountShouldNotBeFound("currencyCode.notEquals=" + DEFAULT_CURRENCY_CODE);

        // Get all the depositAccountList where currencyCode not equals to UPDATED_CURRENCY_CODE
        defaultDepositAccountShouldBeFound("currencyCode.notEquals=" + UPDATED_CURRENCY_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByCurrencyCodeIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where currencyCode in DEFAULT_CURRENCY_CODE or UPDATED_CURRENCY_CODE
        defaultDepositAccountShouldBeFound("currencyCode.in=" + DEFAULT_CURRENCY_CODE + "," + UPDATED_CURRENCY_CODE);

        // Get all the depositAccountList where currencyCode equals to UPDATED_CURRENCY_CODE
        defaultDepositAccountShouldNotBeFound("currencyCode.in=" + UPDATED_CURRENCY_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByCurrencyCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where currencyCode is not null
        defaultDepositAccountShouldBeFound("currencyCode.specified=true");

        // Get all the depositAccountList where currencyCode is null
        defaultDepositAccountShouldNotBeFound("currencyCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByCurrencyCodeContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where currencyCode contains DEFAULT_CURRENCY_CODE
        defaultDepositAccountShouldBeFound("currencyCode.contains=" + DEFAULT_CURRENCY_CODE);

        // Get all the depositAccountList where currencyCode contains UPDATED_CURRENCY_CODE
        defaultDepositAccountShouldNotBeFound("currencyCode.contains=" + UPDATED_CURRENCY_CODE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByCurrencyCodeNotContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where currencyCode does not contain DEFAULT_CURRENCY_CODE
        defaultDepositAccountShouldNotBeFound("currencyCode.doesNotContain=" + DEFAULT_CURRENCY_CODE);

        // Get all the depositAccountList where currencyCode does not contain UPDATED_CURRENCY_CODE
        defaultDepositAccountShouldBeFound("currencyCode.doesNotContain=" + UPDATED_CURRENCY_CODE);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsByCustomerIdIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where customerId equals to DEFAULT_CUSTOMER_ID
        defaultDepositAccountShouldBeFound("customerId.equals=" + DEFAULT_CUSTOMER_ID);

        // Get all the depositAccountList where customerId equals to UPDATED_CUSTOMER_ID
        defaultDepositAccountShouldNotBeFound("customerId.equals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByCustomerIdIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where customerId not equals to DEFAULT_CUSTOMER_ID
        defaultDepositAccountShouldNotBeFound("customerId.notEquals=" + DEFAULT_CUSTOMER_ID);

        // Get all the depositAccountList where customerId not equals to UPDATED_CUSTOMER_ID
        defaultDepositAccountShouldBeFound("customerId.notEquals=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByCustomerIdIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where customerId in DEFAULT_CUSTOMER_ID or UPDATED_CUSTOMER_ID
        defaultDepositAccountShouldBeFound("customerId.in=" + DEFAULT_CUSTOMER_ID + "," + UPDATED_CUSTOMER_ID);

        // Get all the depositAccountList where customerId equals to UPDATED_CUSTOMER_ID
        defaultDepositAccountShouldNotBeFound("customerId.in=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByCustomerIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where customerId is not null
        defaultDepositAccountShouldBeFound("customerId.specified=true");

        // Get all the depositAccountList where customerId is null
        defaultDepositAccountShouldNotBeFound("customerId.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByCustomerIdContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where customerId contains DEFAULT_CUSTOMER_ID
        defaultDepositAccountShouldBeFound("customerId.contains=" + DEFAULT_CUSTOMER_ID);

        // Get all the depositAccountList where customerId contains UPDATED_CUSTOMER_ID
        defaultDepositAccountShouldNotBeFound("customerId.contains=" + UPDATED_CUSTOMER_ID);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByCustomerIdNotContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where customerId does not contain DEFAULT_CUSTOMER_ID
        defaultDepositAccountShouldNotBeFound("customerId.doesNotContain=" + DEFAULT_CUSTOMER_ID);

        // Get all the depositAccountList where customerId does not contain UPDATED_CUSTOMER_ID
        defaultDepositAccountShouldBeFound("customerId.doesNotContain=" + UPDATED_CUSTOMER_ID);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsByAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountNumber equals to DEFAULT_ACCOUNT_NUMBER
        defaultDepositAccountShouldBeFound("accountNumber.equals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the depositAccountList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultDepositAccountShouldNotBeFound("accountNumber.equals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountNumberIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountNumber not equals to DEFAULT_ACCOUNT_NUMBER
        defaultDepositAccountShouldNotBeFound("accountNumber.notEquals=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the depositAccountList where accountNumber not equals to UPDATED_ACCOUNT_NUMBER
        defaultDepositAccountShouldBeFound("accountNumber.notEquals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountNumber in DEFAULT_ACCOUNT_NUMBER or UPDATED_ACCOUNT_NUMBER
        defaultDepositAccountShouldBeFound("accountNumber.in=" + DEFAULT_ACCOUNT_NUMBER + "," + UPDATED_ACCOUNT_NUMBER);

        // Get all the depositAccountList where accountNumber equals to UPDATED_ACCOUNT_NUMBER
        defaultDepositAccountShouldNotBeFound("accountNumber.in=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountNumber is not null
        defaultDepositAccountShouldBeFound("accountNumber.specified=true");

        // Get all the depositAccountList where accountNumber is null
        defaultDepositAccountShouldNotBeFound("accountNumber.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountNumber contains DEFAULT_ACCOUNT_NUMBER
        defaultDepositAccountShouldBeFound("accountNumber.contains=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the depositAccountList where accountNumber contains UPDATED_ACCOUNT_NUMBER
        defaultDepositAccountShouldNotBeFound("accountNumber.contains=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountNumber does not contain DEFAULT_ACCOUNT_NUMBER
        defaultDepositAccountShouldNotBeFound("accountNumber.doesNotContain=" + DEFAULT_ACCOUNT_NUMBER);

        // Get all the depositAccountList where accountNumber does not contain UPDATED_ACCOUNT_NUMBER
        defaultDepositAccountShouldBeFound("accountNumber.doesNotContain=" + UPDATED_ACCOUNT_NUMBER);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsByAccountNameIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountName equals to DEFAULT_ACCOUNT_NAME
        defaultDepositAccountShouldBeFound("accountName.equals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the depositAccountList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultDepositAccountShouldNotBeFound("accountName.equals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountName not equals to DEFAULT_ACCOUNT_NAME
        defaultDepositAccountShouldNotBeFound("accountName.notEquals=" + DEFAULT_ACCOUNT_NAME);

        // Get all the depositAccountList where accountName not equals to UPDATED_ACCOUNT_NAME
        defaultDepositAccountShouldBeFound("accountName.notEquals=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountNameIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountName in DEFAULT_ACCOUNT_NAME or UPDATED_ACCOUNT_NAME
        defaultDepositAccountShouldBeFound("accountName.in=" + DEFAULT_ACCOUNT_NAME + "," + UPDATED_ACCOUNT_NAME);

        // Get all the depositAccountList where accountName equals to UPDATED_ACCOUNT_NAME
        defaultDepositAccountShouldNotBeFound("accountName.in=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountName is not null
        defaultDepositAccountShouldBeFound("accountName.specified=true");

        // Get all the depositAccountList where accountName is null
        defaultDepositAccountShouldNotBeFound("accountName.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountNameContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountName contains DEFAULT_ACCOUNT_NAME
        defaultDepositAccountShouldBeFound("accountName.contains=" + DEFAULT_ACCOUNT_NAME);

        // Get all the depositAccountList where accountName contains UPDATED_ACCOUNT_NAME
        defaultDepositAccountShouldNotBeFound("accountName.contains=" + UPDATED_ACCOUNT_NAME);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountNameNotContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountName does not contain DEFAULT_ACCOUNT_NAME
        defaultDepositAccountShouldNotBeFound("accountName.doesNotContain=" + DEFAULT_ACCOUNT_NAME);

        // Get all the depositAccountList where accountName does not contain UPDATED_ACCOUNT_NAME
        defaultDepositAccountShouldBeFound("accountName.doesNotContain=" + UPDATED_ACCOUNT_NAME);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsByAccountBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountBalance equals to DEFAULT_ACCOUNT_BALANCE
        defaultDepositAccountShouldBeFound("accountBalance.equals=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the depositAccountList where accountBalance equals to UPDATED_ACCOUNT_BALANCE
        defaultDepositAccountShouldNotBeFound("accountBalance.equals=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountBalanceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountBalance not equals to DEFAULT_ACCOUNT_BALANCE
        defaultDepositAccountShouldNotBeFound("accountBalance.notEquals=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the depositAccountList where accountBalance not equals to UPDATED_ACCOUNT_BALANCE
        defaultDepositAccountShouldBeFound("accountBalance.notEquals=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountBalance in DEFAULT_ACCOUNT_BALANCE or UPDATED_ACCOUNT_BALANCE
        defaultDepositAccountShouldBeFound("accountBalance.in=" + DEFAULT_ACCOUNT_BALANCE + "," + UPDATED_ACCOUNT_BALANCE);

        // Get all the depositAccountList where accountBalance equals to UPDATED_ACCOUNT_BALANCE
        defaultDepositAccountShouldNotBeFound("accountBalance.in=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountBalance is not null
        defaultDepositAccountShouldBeFound("accountBalance.specified=true");

        // Get all the depositAccountList where accountBalance is null
        defaultDepositAccountShouldNotBeFound("accountBalance.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountBalance is greater than or equal to DEFAULT_ACCOUNT_BALANCE
        defaultDepositAccountShouldBeFound("accountBalance.greaterThanOrEqual=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the depositAccountList where accountBalance is greater than or equal to UPDATED_ACCOUNT_BALANCE
        defaultDepositAccountShouldNotBeFound("accountBalance.greaterThanOrEqual=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountBalance is less than or equal to DEFAULT_ACCOUNT_BALANCE
        defaultDepositAccountShouldBeFound("accountBalance.lessThanOrEqual=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the depositAccountList where accountBalance is less than or equal to SMALLER_ACCOUNT_BALANCE
        defaultDepositAccountShouldNotBeFound("accountBalance.lessThanOrEqual=" + SMALLER_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountBalance is less than DEFAULT_ACCOUNT_BALANCE
        defaultDepositAccountShouldNotBeFound("accountBalance.lessThan=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the depositAccountList where accountBalance is less than UPDATED_ACCOUNT_BALANCE
        defaultDepositAccountShouldBeFound("accountBalance.lessThan=" + UPDATED_ACCOUNT_BALANCE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountBalance is greater than DEFAULT_ACCOUNT_BALANCE
        defaultDepositAccountShouldNotBeFound("accountBalance.greaterThan=" + DEFAULT_ACCOUNT_BALANCE);

        // Get all the depositAccountList where accountBalance is greater than SMALLER_ACCOUNT_BALANCE
        defaultDepositAccountShouldBeFound("accountBalance.greaterThan=" + SMALLER_ACCOUNT_BALANCE);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsBySectorIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where sector equals to DEFAULT_SECTOR
        defaultDepositAccountShouldBeFound("sector.equals=" + DEFAULT_SECTOR);

        // Get all the depositAccountList where sector equals to UPDATED_SECTOR
        defaultDepositAccountShouldNotBeFound("sector.equals=" + UPDATED_SECTOR);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySectorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where sector not equals to DEFAULT_SECTOR
        defaultDepositAccountShouldNotBeFound("sector.notEquals=" + DEFAULT_SECTOR);

        // Get all the depositAccountList where sector not equals to UPDATED_SECTOR
        defaultDepositAccountShouldBeFound("sector.notEquals=" + UPDATED_SECTOR);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySectorIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where sector in DEFAULT_SECTOR or UPDATED_SECTOR
        defaultDepositAccountShouldBeFound("sector.in=" + DEFAULT_SECTOR + "," + UPDATED_SECTOR);

        // Get all the depositAccountList where sector equals to UPDATED_SECTOR
        defaultDepositAccountShouldNotBeFound("sector.in=" + UPDATED_SECTOR);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySectorIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where sector is not null
        defaultDepositAccountShouldBeFound("sector.specified=true");

        // Get all the depositAccountList where sector is null
        defaultDepositAccountShouldNotBeFound("sector.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySectorContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where sector contains DEFAULT_SECTOR
        defaultDepositAccountShouldBeFound("sector.contains=" + DEFAULT_SECTOR);

        // Get all the depositAccountList where sector contains UPDATED_SECTOR
        defaultDepositAccountShouldNotBeFound("sector.contains=" + UPDATED_SECTOR);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySectorNotContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where sector does not contain DEFAULT_SECTOR
        defaultDepositAccountShouldNotBeFound("sector.doesNotContain=" + DEFAULT_SECTOR);

        // Get all the depositAccountList where sector does not contain UPDATED_SECTOR
        defaultDepositAccountShouldBeFound("sector.doesNotContain=" + UPDATED_SECTOR);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsBySubSectorIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where subSector equals to DEFAULT_SUB_SECTOR
        defaultDepositAccountShouldBeFound("subSector.equals=" + DEFAULT_SUB_SECTOR);

        // Get all the depositAccountList where subSector equals to UPDATED_SUB_SECTOR
        defaultDepositAccountShouldNotBeFound("subSector.equals=" + UPDATED_SUB_SECTOR);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySubSectorIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where subSector not equals to DEFAULT_SUB_SECTOR
        defaultDepositAccountShouldNotBeFound("subSector.notEquals=" + DEFAULT_SUB_SECTOR);

        // Get all the depositAccountList where subSector not equals to UPDATED_SUB_SECTOR
        defaultDepositAccountShouldBeFound("subSector.notEquals=" + UPDATED_SUB_SECTOR);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySubSectorIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where subSector in DEFAULT_SUB_SECTOR or UPDATED_SUB_SECTOR
        defaultDepositAccountShouldBeFound("subSector.in=" + DEFAULT_SUB_SECTOR + "," + UPDATED_SUB_SECTOR);

        // Get all the depositAccountList where subSector equals to UPDATED_SUB_SECTOR
        defaultDepositAccountShouldNotBeFound("subSector.in=" + UPDATED_SUB_SECTOR);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySubSectorIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where subSector is not null
        defaultDepositAccountShouldBeFound("subSector.specified=true");

        // Get all the depositAccountList where subSector is null
        defaultDepositAccountShouldNotBeFound("subSector.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySubSectorContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where subSector contains DEFAULT_SUB_SECTOR
        defaultDepositAccountShouldBeFound("subSector.contains=" + DEFAULT_SUB_SECTOR);

        // Get all the depositAccountList where subSector contains UPDATED_SUB_SECTOR
        defaultDepositAccountShouldNotBeFound("subSector.contains=" + UPDATED_SUB_SECTOR);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsBySubSectorNotContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where subSector does not contain DEFAULT_SUB_SECTOR
        defaultDepositAccountShouldNotBeFound("subSector.doesNotContain=" + DEFAULT_SUB_SECTOR);

        // Get all the depositAccountList where subSector does not contain UPDATED_SUB_SECTOR
        defaultDepositAccountShouldBeFound("subSector.doesNotContain=" + UPDATED_SUB_SECTOR);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsByAccountOpeningDateIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountOpeningDate equals to DEFAULT_ACCOUNT_OPENING_DATE
        defaultDepositAccountShouldBeFound("accountOpeningDate.equals=" + DEFAULT_ACCOUNT_OPENING_DATE);

        // Get all the depositAccountList where accountOpeningDate equals to UPDATED_ACCOUNT_OPENING_DATE
        defaultDepositAccountShouldNotBeFound("accountOpeningDate.equals=" + UPDATED_ACCOUNT_OPENING_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountOpeningDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountOpeningDate not equals to DEFAULT_ACCOUNT_OPENING_DATE
        defaultDepositAccountShouldNotBeFound("accountOpeningDate.notEquals=" + DEFAULT_ACCOUNT_OPENING_DATE);

        // Get all the depositAccountList where accountOpeningDate not equals to UPDATED_ACCOUNT_OPENING_DATE
        defaultDepositAccountShouldBeFound("accountOpeningDate.notEquals=" + UPDATED_ACCOUNT_OPENING_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountOpeningDateIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountOpeningDate in DEFAULT_ACCOUNT_OPENING_DATE or UPDATED_ACCOUNT_OPENING_DATE
        defaultDepositAccountShouldBeFound("accountOpeningDate.in=" + DEFAULT_ACCOUNT_OPENING_DATE + "," + UPDATED_ACCOUNT_OPENING_DATE);

        // Get all the depositAccountList where accountOpeningDate equals to UPDATED_ACCOUNT_OPENING_DATE
        defaultDepositAccountShouldNotBeFound("accountOpeningDate.in=" + UPDATED_ACCOUNT_OPENING_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountOpeningDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountOpeningDate is not null
        defaultDepositAccountShouldBeFound("accountOpeningDate.specified=true");

        // Get all the depositAccountList where accountOpeningDate is null
        defaultDepositAccountShouldNotBeFound("accountOpeningDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountOpeningDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountOpeningDate is greater than or equal to DEFAULT_ACCOUNT_OPENING_DATE
        defaultDepositAccountShouldBeFound("accountOpeningDate.greaterThanOrEqual=" + DEFAULT_ACCOUNT_OPENING_DATE);

        // Get all the depositAccountList where accountOpeningDate is greater than or equal to UPDATED_ACCOUNT_OPENING_DATE
        defaultDepositAccountShouldNotBeFound("accountOpeningDate.greaterThanOrEqual=" + UPDATED_ACCOUNT_OPENING_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountOpeningDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountOpeningDate is less than or equal to DEFAULT_ACCOUNT_OPENING_DATE
        defaultDepositAccountShouldBeFound("accountOpeningDate.lessThanOrEqual=" + DEFAULT_ACCOUNT_OPENING_DATE);

        // Get all the depositAccountList where accountOpeningDate is less than or equal to SMALLER_ACCOUNT_OPENING_DATE
        defaultDepositAccountShouldNotBeFound("accountOpeningDate.lessThanOrEqual=" + SMALLER_ACCOUNT_OPENING_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountOpeningDateIsLessThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountOpeningDate is less than DEFAULT_ACCOUNT_OPENING_DATE
        defaultDepositAccountShouldNotBeFound("accountOpeningDate.lessThan=" + DEFAULT_ACCOUNT_OPENING_DATE);

        // Get all the depositAccountList where accountOpeningDate is less than UPDATED_ACCOUNT_OPENING_DATE
        defaultDepositAccountShouldBeFound("accountOpeningDate.lessThan=" + UPDATED_ACCOUNT_OPENING_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountOpeningDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountOpeningDate is greater than DEFAULT_ACCOUNT_OPENING_DATE
        defaultDepositAccountShouldNotBeFound("accountOpeningDate.greaterThan=" + DEFAULT_ACCOUNT_OPENING_DATE);

        // Get all the depositAccountList where accountOpeningDate is greater than SMALLER_ACCOUNT_OPENING_DATE
        defaultDepositAccountShouldBeFound("accountOpeningDate.greaterThan=" + SMALLER_ACCOUNT_OPENING_DATE);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsByAccountMaturityDateIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountMaturityDate equals to DEFAULT_ACCOUNT_MATURITY_DATE
        defaultDepositAccountShouldBeFound("accountMaturityDate.equals=" + DEFAULT_ACCOUNT_MATURITY_DATE);

        // Get all the depositAccountList where accountMaturityDate equals to UPDATED_ACCOUNT_MATURITY_DATE
        defaultDepositAccountShouldNotBeFound("accountMaturityDate.equals=" + UPDATED_ACCOUNT_MATURITY_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountMaturityDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountMaturityDate not equals to DEFAULT_ACCOUNT_MATURITY_DATE
        defaultDepositAccountShouldNotBeFound("accountMaturityDate.notEquals=" + DEFAULT_ACCOUNT_MATURITY_DATE);

        // Get all the depositAccountList where accountMaturityDate not equals to UPDATED_ACCOUNT_MATURITY_DATE
        defaultDepositAccountShouldBeFound("accountMaturityDate.notEquals=" + UPDATED_ACCOUNT_MATURITY_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountMaturityDateIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountMaturityDate in DEFAULT_ACCOUNT_MATURITY_DATE or UPDATED_ACCOUNT_MATURITY_DATE
        defaultDepositAccountShouldBeFound("accountMaturityDate.in=" + DEFAULT_ACCOUNT_MATURITY_DATE + "," + UPDATED_ACCOUNT_MATURITY_DATE);

        // Get all the depositAccountList where accountMaturityDate equals to UPDATED_ACCOUNT_MATURITY_DATE
        defaultDepositAccountShouldNotBeFound("accountMaturityDate.in=" + UPDATED_ACCOUNT_MATURITY_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountMaturityDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountMaturityDate is not null
        defaultDepositAccountShouldBeFound("accountMaturityDate.specified=true");

        // Get all the depositAccountList where accountMaturityDate is null
        defaultDepositAccountShouldNotBeFound("accountMaturityDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountMaturityDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountMaturityDate is greater than or equal to DEFAULT_ACCOUNT_MATURITY_DATE
        defaultDepositAccountShouldBeFound("accountMaturityDate.greaterThanOrEqual=" + DEFAULT_ACCOUNT_MATURITY_DATE);

        // Get all the depositAccountList where accountMaturityDate is greater than or equal to UPDATED_ACCOUNT_MATURITY_DATE
        defaultDepositAccountShouldNotBeFound("accountMaturityDate.greaterThanOrEqual=" + UPDATED_ACCOUNT_MATURITY_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountMaturityDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountMaturityDate is less than or equal to DEFAULT_ACCOUNT_MATURITY_DATE
        defaultDepositAccountShouldBeFound("accountMaturityDate.lessThanOrEqual=" + DEFAULT_ACCOUNT_MATURITY_DATE);

        // Get all the depositAccountList where accountMaturityDate is less than or equal to SMALLER_ACCOUNT_MATURITY_DATE
        defaultDepositAccountShouldNotBeFound("accountMaturityDate.lessThanOrEqual=" + SMALLER_ACCOUNT_MATURITY_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountMaturityDateIsLessThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountMaturityDate is less than DEFAULT_ACCOUNT_MATURITY_DATE
        defaultDepositAccountShouldNotBeFound("accountMaturityDate.lessThan=" + DEFAULT_ACCOUNT_MATURITY_DATE);

        // Get all the depositAccountList where accountMaturityDate is less than UPDATED_ACCOUNT_MATURITY_DATE
        defaultDepositAccountShouldBeFound("accountMaturityDate.lessThan=" + UPDATED_ACCOUNT_MATURITY_DATE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountMaturityDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountMaturityDate is greater than DEFAULT_ACCOUNT_MATURITY_DATE
        defaultDepositAccountShouldNotBeFound("accountMaturityDate.greaterThan=" + DEFAULT_ACCOUNT_MATURITY_DATE);

        // Get all the depositAccountList where accountMaturityDate is greater than SMALLER_ACCOUNT_MATURITY_DATE
        defaultDepositAccountShouldBeFound("accountMaturityDate.greaterThan=" + SMALLER_ACCOUNT_MATURITY_DATE);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsByAccountStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountStatus equals to DEFAULT_ACCOUNT_STATUS
        defaultDepositAccountShouldBeFound("accountStatus.equals=" + DEFAULT_ACCOUNT_STATUS);

        // Get all the depositAccountList where accountStatus equals to UPDATED_ACCOUNT_STATUS
        defaultDepositAccountShouldNotBeFound("accountStatus.equals=" + UPDATED_ACCOUNT_STATUS);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountStatusIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountStatus not equals to DEFAULT_ACCOUNT_STATUS
        defaultDepositAccountShouldNotBeFound("accountStatus.notEquals=" + DEFAULT_ACCOUNT_STATUS);

        // Get all the depositAccountList where accountStatus not equals to UPDATED_ACCOUNT_STATUS
        defaultDepositAccountShouldBeFound("accountStatus.notEquals=" + UPDATED_ACCOUNT_STATUS);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountStatusIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountStatus in DEFAULT_ACCOUNT_STATUS or UPDATED_ACCOUNT_STATUS
        defaultDepositAccountShouldBeFound("accountStatus.in=" + DEFAULT_ACCOUNT_STATUS + "," + UPDATED_ACCOUNT_STATUS);

        // Get all the depositAccountList where accountStatus equals to UPDATED_ACCOUNT_STATUS
        defaultDepositAccountShouldNotBeFound("accountStatus.in=" + UPDATED_ACCOUNT_STATUS);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountStatus is not null
        defaultDepositAccountShouldBeFound("accountStatus.specified=true");

        // Get all the depositAccountList where accountStatus is null
        defaultDepositAccountShouldNotBeFound("accountStatus.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountStatusContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountStatus contains DEFAULT_ACCOUNT_STATUS
        defaultDepositAccountShouldBeFound("accountStatus.contains=" + DEFAULT_ACCOUNT_STATUS);

        // Get all the depositAccountList where accountStatus contains UPDATED_ACCOUNT_STATUS
        defaultDepositAccountShouldNotBeFound("accountStatus.contains=" + UPDATED_ACCOUNT_STATUS);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccountStatusNotContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accountStatus does not contain DEFAULT_ACCOUNT_STATUS
        defaultDepositAccountShouldNotBeFound("accountStatus.doesNotContain=" + DEFAULT_ACCOUNT_STATUS);

        // Get all the depositAccountList where accountStatus does not contain UPDATED_ACCOUNT_STATUS
        defaultDepositAccountShouldBeFound("accountStatus.doesNotContain=" + UPDATED_ACCOUNT_STATUS);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsByRateIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where rate equals to DEFAULT_RATE
        defaultDepositAccountShouldBeFound("rate.equals=" + DEFAULT_RATE);

        // Get all the depositAccountList where rate equals to UPDATED_RATE
        defaultDepositAccountShouldNotBeFound("rate.equals=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where rate not equals to DEFAULT_RATE
        defaultDepositAccountShouldNotBeFound("rate.notEquals=" + DEFAULT_RATE);

        // Get all the depositAccountList where rate not equals to UPDATED_RATE
        defaultDepositAccountShouldBeFound("rate.notEquals=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRateIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where rate in DEFAULT_RATE or UPDATED_RATE
        defaultDepositAccountShouldBeFound("rate.in=" + DEFAULT_RATE + "," + UPDATED_RATE);

        // Get all the depositAccountList where rate equals to UPDATED_RATE
        defaultDepositAccountShouldNotBeFound("rate.in=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where rate is not null
        defaultDepositAccountShouldBeFound("rate.specified=true");

        // Get all the depositAccountList where rate is null
        defaultDepositAccountShouldNotBeFound("rate.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where rate is greater than or equal to DEFAULT_RATE
        defaultDepositAccountShouldBeFound("rate.greaterThanOrEqual=" + DEFAULT_RATE);

        // Get all the depositAccountList where rate is greater than or equal to UPDATED_RATE
        defaultDepositAccountShouldNotBeFound("rate.greaterThanOrEqual=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where rate is less than or equal to DEFAULT_RATE
        defaultDepositAccountShouldBeFound("rate.lessThanOrEqual=" + DEFAULT_RATE);

        // Get all the depositAccountList where rate is less than or equal to SMALLER_RATE
        defaultDepositAccountShouldNotBeFound("rate.lessThanOrEqual=" + SMALLER_RATE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRateIsLessThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where rate is less than DEFAULT_RATE
        defaultDepositAccountShouldNotBeFound("rate.lessThan=" + DEFAULT_RATE);

        // Get all the depositAccountList where rate is less than UPDATED_RATE
        defaultDepositAccountShouldBeFound("rate.lessThan=" + UPDATED_RATE);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where rate is greater than DEFAULT_RATE
        defaultDepositAccountShouldNotBeFound("rate.greaterThan=" + DEFAULT_RATE);

        // Get all the depositAccountList where rate is greater than SMALLER_RATE
        defaultDepositAccountShouldBeFound("rate.greaterThan=" + SMALLER_RATE);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsByBookedInterestAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where bookedInterestAmount equals to DEFAULT_BOOKED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("bookedInterestAmount.equals=" + DEFAULT_BOOKED_INTEREST_AMOUNT);

        // Get all the depositAccountList where bookedInterestAmount equals to UPDATED_BOOKED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("bookedInterestAmount.equals=" + UPDATED_BOOKED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByBookedInterestAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where bookedInterestAmount not equals to DEFAULT_BOOKED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("bookedInterestAmount.notEquals=" + DEFAULT_BOOKED_INTEREST_AMOUNT);

        // Get all the depositAccountList where bookedInterestAmount not equals to UPDATED_BOOKED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("bookedInterestAmount.notEquals=" + UPDATED_BOOKED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByBookedInterestAmountIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where bookedInterestAmount in DEFAULT_BOOKED_INTEREST_AMOUNT or UPDATED_BOOKED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("bookedInterestAmount.in=" + DEFAULT_BOOKED_INTEREST_AMOUNT + "," + UPDATED_BOOKED_INTEREST_AMOUNT);

        // Get all the depositAccountList where bookedInterestAmount equals to UPDATED_BOOKED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("bookedInterestAmount.in=" + UPDATED_BOOKED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByBookedInterestAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where bookedInterestAmount is not null
        defaultDepositAccountShouldBeFound("bookedInterestAmount.specified=true");

        // Get all the depositAccountList where bookedInterestAmount is null
        defaultDepositAccountShouldNotBeFound("bookedInterestAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByBookedInterestAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where bookedInterestAmount is greater than or equal to DEFAULT_BOOKED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("bookedInterestAmount.greaterThanOrEqual=" + DEFAULT_BOOKED_INTEREST_AMOUNT);

        // Get all the depositAccountList where bookedInterestAmount is greater than or equal to UPDATED_BOOKED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("bookedInterestAmount.greaterThanOrEqual=" + UPDATED_BOOKED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByBookedInterestAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where bookedInterestAmount is less than or equal to DEFAULT_BOOKED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("bookedInterestAmount.lessThanOrEqual=" + DEFAULT_BOOKED_INTEREST_AMOUNT);

        // Get all the depositAccountList where bookedInterestAmount is less than or equal to SMALLER_BOOKED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("bookedInterestAmount.lessThanOrEqual=" + SMALLER_BOOKED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByBookedInterestAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where bookedInterestAmount is less than DEFAULT_BOOKED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("bookedInterestAmount.lessThan=" + DEFAULT_BOOKED_INTEREST_AMOUNT);

        // Get all the depositAccountList where bookedInterestAmount is less than UPDATED_BOOKED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("bookedInterestAmount.lessThan=" + UPDATED_BOOKED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByBookedInterestAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where bookedInterestAmount is greater than DEFAULT_BOOKED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("bookedInterestAmount.greaterThan=" + DEFAULT_BOOKED_INTEREST_AMOUNT);

        // Get all the depositAccountList where bookedInterestAmount is greater than SMALLER_BOOKED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("bookedInterestAmount.greaterThan=" + SMALLER_BOOKED_INTEREST_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsByInterestAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where interestAmount equals to DEFAULT_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("interestAmount.equals=" + DEFAULT_INTEREST_AMOUNT);

        // Get all the depositAccountList where interestAmount equals to UPDATED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("interestAmount.equals=" + UPDATED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByInterestAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where interestAmount not equals to DEFAULT_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("interestAmount.notEquals=" + DEFAULT_INTEREST_AMOUNT);

        // Get all the depositAccountList where interestAmount not equals to UPDATED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("interestAmount.notEquals=" + UPDATED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByInterestAmountIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where interestAmount in DEFAULT_INTEREST_AMOUNT or UPDATED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("interestAmount.in=" + DEFAULT_INTEREST_AMOUNT + "," + UPDATED_INTEREST_AMOUNT);

        // Get all the depositAccountList where interestAmount equals to UPDATED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("interestAmount.in=" + UPDATED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByInterestAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where interestAmount is not null
        defaultDepositAccountShouldBeFound("interestAmount.specified=true");

        // Get all the depositAccountList where interestAmount is null
        defaultDepositAccountShouldNotBeFound("interestAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByInterestAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where interestAmount is greater than or equal to DEFAULT_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("interestAmount.greaterThanOrEqual=" + DEFAULT_INTEREST_AMOUNT);

        // Get all the depositAccountList where interestAmount is greater than or equal to UPDATED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("interestAmount.greaterThanOrEqual=" + UPDATED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByInterestAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where interestAmount is less than or equal to DEFAULT_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("interestAmount.lessThanOrEqual=" + DEFAULT_INTEREST_AMOUNT);

        // Get all the depositAccountList where interestAmount is less than or equal to SMALLER_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("interestAmount.lessThanOrEqual=" + SMALLER_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByInterestAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where interestAmount is less than DEFAULT_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("interestAmount.lessThan=" + DEFAULT_INTEREST_AMOUNT);

        // Get all the depositAccountList where interestAmount is less than UPDATED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("interestAmount.lessThan=" + UPDATED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByInterestAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where interestAmount is greater than DEFAULT_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("interestAmount.greaterThan=" + DEFAULT_INTEREST_AMOUNT);

        // Get all the depositAccountList where interestAmount is greater than SMALLER_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("interestAmount.greaterThan=" + SMALLER_INTEREST_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsByAccruedInterestAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accruedInterestAmount equals to DEFAULT_ACCRUED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("accruedInterestAmount.equals=" + DEFAULT_ACCRUED_INTEREST_AMOUNT);

        // Get all the depositAccountList where accruedInterestAmount equals to UPDATED_ACCRUED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("accruedInterestAmount.equals=" + UPDATED_ACCRUED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccruedInterestAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accruedInterestAmount not equals to DEFAULT_ACCRUED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("accruedInterestAmount.notEquals=" + DEFAULT_ACCRUED_INTEREST_AMOUNT);

        // Get all the depositAccountList where accruedInterestAmount not equals to UPDATED_ACCRUED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("accruedInterestAmount.notEquals=" + UPDATED_ACCRUED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccruedInterestAmountIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accruedInterestAmount in DEFAULT_ACCRUED_INTEREST_AMOUNT or UPDATED_ACCRUED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("accruedInterestAmount.in=" + DEFAULT_ACCRUED_INTEREST_AMOUNT + "," + UPDATED_ACCRUED_INTEREST_AMOUNT);

        // Get all the depositAccountList where accruedInterestAmount equals to UPDATED_ACCRUED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("accruedInterestAmount.in=" + UPDATED_ACCRUED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccruedInterestAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accruedInterestAmount is not null
        defaultDepositAccountShouldBeFound("accruedInterestAmount.specified=true");

        // Get all the depositAccountList where accruedInterestAmount is null
        defaultDepositAccountShouldNotBeFound("accruedInterestAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccruedInterestAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accruedInterestAmount is greater than or equal to DEFAULT_ACCRUED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("accruedInterestAmount.greaterThanOrEqual=" + DEFAULT_ACCRUED_INTEREST_AMOUNT);

        // Get all the depositAccountList where accruedInterestAmount is greater than or equal to UPDATED_ACCRUED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("accruedInterestAmount.greaterThanOrEqual=" + UPDATED_ACCRUED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccruedInterestAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accruedInterestAmount is less than or equal to DEFAULT_ACCRUED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("accruedInterestAmount.lessThanOrEqual=" + DEFAULT_ACCRUED_INTEREST_AMOUNT);

        // Get all the depositAccountList where accruedInterestAmount is less than or equal to SMALLER_ACCRUED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("accruedInterestAmount.lessThanOrEqual=" + SMALLER_ACCRUED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccruedInterestAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accruedInterestAmount is less than DEFAULT_ACCRUED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("accruedInterestAmount.lessThan=" + DEFAULT_ACCRUED_INTEREST_AMOUNT);

        // Get all the depositAccountList where accruedInterestAmount is less than UPDATED_ACCRUED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("accruedInterestAmount.lessThan=" + UPDATED_ACCRUED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByAccruedInterestAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where accruedInterestAmount is greater than DEFAULT_ACCRUED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("accruedInterestAmount.greaterThan=" + DEFAULT_ACCRUED_INTEREST_AMOUNT);

        // Get all the depositAccountList where accruedInterestAmount is greater than SMALLER_ACCRUED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("accruedInterestAmount.greaterThan=" + SMALLER_ACCRUED_INTEREST_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsByDepositSchemeIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where depositScheme equals to DEFAULT_DEPOSIT_SCHEME
        defaultDepositAccountShouldBeFound("depositScheme.equals=" + DEFAULT_DEPOSIT_SCHEME);

        // Get all the depositAccountList where depositScheme equals to UPDATED_DEPOSIT_SCHEME
        defaultDepositAccountShouldNotBeFound("depositScheme.equals=" + UPDATED_DEPOSIT_SCHEME);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByDepositSchemeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where depositScheme not equals to DEFAULT_DEPOSIT_SCHEME
        defaultDepositAccountShouldNotBeFound("depositScheme.notEquals=" + DEFAULT_DEPOSIT_SCHEME);

        // Get all the depositAccountList where depositScheme not equals to UPDATED_DEPOSIT_SCHEME
        defaultDepositAccountShouldBeFound("depositScheme.notEquals=" + UPDATED_DEPOSIT_SCHEME);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByDepositSchemeIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where depositScheme in DEFAULT_DEPOSIT_SCHEME or UPDATED_DEPOSIT_SCHEME
        defaultDepositAccountShouldBeFound("depositScheme.in=" + DEFAULT_DEPOSIT_SCHEME + "," + UPDATED_DEPOSIT_SCHEME);

        // Get all the depositAccountList where depositScheme equals to UPDATED_DEPOSIT_SCHEME
        defaultDepositAccountShouldNotBeFound("depositScheme.in=" + UPDATED_DEPOSIT_SCHEME);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByDepositSchemeIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where depositScheme is not null
        defaultDepositAccountShouldBeFound("depositScheme.specified=true");

        // Get all the depositAccountList where depositScheme is null
        defaultDepositAccountShouldNotBeFound("depositScheme.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByDepositSchemeContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where depositScheme contains DEFAULT_DEPOSIT_SCHEME
        defaultDepositAccountShouldBeFound("depositScheme.contains=" + DEFAULT_DEPOSIT_SCHEME);

        // Get all the depositAccountList where depositScheme contains UPDATED_DEPOSIT_SCHEME
        defaultDepositAccountShouldNotBeFound("depositScheme.contains=" + UPDATED_DEPOSIT_SCHEME);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByDepositSchemeNotContainsSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where depositScheme does not contain DEFAULT_DEPOSIT_SCHEME
        defaultDepositAccountShouldNotBeFound("depositScheme.doesNotContain=" + DEFAULT_DEPOSIT_SCHEME);

        // Get all the depositAccountList where depositScheme does not contain UPDATED_DEPOSIT_SCHEME
        defaultDepositAccountShouldBeFound("depositScheme.doesNotContain=" + UPDATED_DEPOSIT_SCHEME);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedTotalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedTotalAmount equals to DEFAULT_REVALUED_TOTAL_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedTotalAmount.equals=" + DEFAULT_REVALUED_TOTAL_AMOUNT);

        // Get all the depositAccountList where revaluedTotalAmount equals to UPDATED_REVALUED_TOTAL_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedTotalAmount.equals=" + UPDATED_REVALUED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedTotalAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedTotalAmount not equals to DEFAULT_REVALUED_TOTAL_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedTotalAmount.notEquals=" + DEFAULT_REVALUED_TOTAL_AMOUNT);

        // Get all the depositAccountList where revaluedTotalAmount not equals to UPDATED_REVALUED_TOTAL_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedTotalAmount.notEquals=" + UPDATED_REVALUED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedTotalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedTotalAmount in DEFAULT_REVALUED_TOTAL_AMOUNT or UPDATED_REVALUED_TOTAL_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedTotalAmount.in=" + DEFAULT_REVALUED_TOTAL_AMOUNT + "," + UPDATED_REVALUED_TOTAL_AMOUNT);

        // Get all the depositAccountList where revaluedTotalAmount equals to UPDATED_REVALUED_TOTAL_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedTotalAmount.in=" + UPDATED_REVALUED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedTotalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedTotalAmount is not null
        defaultDepositAccountShouldBeFound("revaluedTotalAmount.specified=true");

        // Get all the depositAccountList where revaluedTotalAmount is null
        defaultDepositAccountShouldNotBeFound("revaluedTotalAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedTotalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedTotalAmount is greater than or equal to DEFAULT_REVALUED_TOTAL_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedTotalAmount.greaterThanOrEqual=" + DEFAULT_REVALUED_TOTAL_AMOUNT);

        // Get all the depositAccountList where revaluedTotalAmount is greater than or equal to UPDATED_REVALUED_TOTAL_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedTotalAmount.greaterThanOrEqual=" + UPDATED_REVALUED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedTotalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedTotalAmount is less than or equal to DEFAULT_REVALUED_TOTAL_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedTotalAmount.lessThanOrEqual=" + DEFAULT_REVALUED_TOTAL_AMOUNT);

        // Get all the depositAccountList where revaluedTotalAmount is less than or equal to SMALLER_REVALUED_TOTAL_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedTotalAmount.lessThanOrEqual=" + SMALLER_REVALUED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedTotalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedTotalAmount is less than DEFAULT_REVALUED_TOTAL_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedTotalAmount.lessThan=" + DEFAULT_REVALUED_TOTAL_AMOUNT);

        // Get all the depositAccountList where revaluedTotalAmount is less than UPDATED_REVALUED_TOTAL_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedTotalAmount.lessThan=" + UPDATED_REVALUED_TOTAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedTotalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedTotalAmount is greater than DEFAULT_REVALUED_TOTAL_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedTotalAmount.greaterThan=" + DEFAULT_REVALUED_TOTAL_AMOUNT);

        // Get all the depositAccountList where revaluedTotalAmount is greater than SMALLER_REVALUED_TOTAL_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedTotalAmount.greaterThan=" + SMALLER_REVALUED_TOTAL_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedPrincipalAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedPrincipalAmount equals to DEFAULT_REVALUED_PRINCIPAL_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedPrincipalAmount.equals=" + DEFAULT_REVALUED_PRINCIPAL_AMOUNT);

        // Get all the depositAccountList where revaluedPrincipalAmount equals to UPDATED_REVALUED_PRINCIPAL_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedPrincipalAmount.equals=" + UPDATED_REVALUED_PRINCIPAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedPrincipalAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedPrincipalAmount not equals to DEFAULT_REVALUED_PRINCIPAL_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedPrincipalAmount.notEquals=" + DEFAULT_REVALUED_PRINCIPAL_AMOUNT);

        // Get all the depositAccountList where revaluedPrincipalAmount not equals to UPDATED_REVALUED_PRINCIPAL_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedPrincipalAmount.notEquals=" + UPDATED_REVALUED_PRINCIPAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedPrincipalAmountIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedPrincipalAmount in DEFAULT_REVALUED_PRINCIPAL_AMOUNT or UPDATED_REVALUED_PRINCIPAL_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedPrincipalAmount.in=" + DEFAULT_REVALUED_PRINCIPAL_AMOUNT + "," + UPDATED_REVALUED_PRINCIPAL_AMOUNT);

        // Get all the depositAccountList where revaluedPrincipalAmount equals to UPDATED_REVALUED_PRINCIPAL_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedPrincipalAmount.in=" + UPDATED_REVALUED_PRINCIPAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedPrincipalAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedPrincipalAmount is not null
        defaultDepositAccountShouldBeFound("revaluedPrincipalAmount.specified=true");

        // Get all the depositAccountList where revaluedPrincipalAmount is null
        defaultDepositAccountShouldNotBeFound("revaluedPrincipalAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedPrincipalAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedPrincipalAmount is greater than or equal to DEFAULT_REVALUED_PRINCIPAL_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedPrincipalAmount.greaterThanOrEqual=" + DEFAULT_REVALUED_PRINCIPAL_AMOUNT);

        // Get all the depositAccountList where revaluedPrincipalAmount is greater than or equal to UPDATED_REVALUED_PRINCIPAL_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedPrincipalAmount.greaterThanOrEqual=" + UPDATED_REVALUED_PRINCIPAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedPrincipalAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedPrincipalAmount is less than or equal to DEFAULT_REVALUED_PRINCIPAL_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedPrincipalAmount.lessThanOrEqual=" + DEFAULT_REVALUED_PRINCIPAL_AMOUNT);

        // Get all the depositAccountList where revaluedPrincipalAmount is less than or equal to SMALLER_REVALUED_PRINCIPAL_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedPrincipalAmount.lessThanOrEqual=" + SMALLER_REVALUED_PRINCIPAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedPrincipalAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedPrincipalAmount is less than DEFAULT_REVALUED_PRINCIPAL_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedPrincipalAmount.lessThan=" + DEFAULT_REVALUED_PRINCIPAL_AMOUNT);

        // Get all the depositAccountList where revaluedPrincipalAmount is less than UPDATED_REVALUED_PRINCIPAL_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedPrincipalAmount.lessThan=" + UPDATED_REVALUED_PRINCIPAL_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedPrincipalAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedPrincipalAmount is greater than DEFAULT_REVALUED_PRINCIPAL_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedPrincipalAmount.greaterThan=" + DEFAULT_REVALUED_PRINCIPAL_AMOUNT);

        // Get all the depositAccountList where revaluedPrincipalAmount is greater than SMALLER_REVALUED_PRINCIPAL_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedPrincipalAmount.greaterThan=" + SMALLER_REVALUED_PRINCIPAL_AMOUNT);
    }


    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedInterestAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedInterestAmount equals to DEFAULT_REVALUED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedInterestAmount.equals=" + DEFAULT_REVALUED_INTEREST_AMOUNT);

        // Get all the depositAccountList where revaluedInterestAmount equals to UPDATED_REVALUED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedInterestAmount.equals=" + UPDATED_REVALUED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedInterestAmountIsNotEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedInterestAmount not equals to DEFAULT_REVALUED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedInterestAmount.notEquals=" + DEFAULT_REVALUED_INTEREST_AMOUNT);

        // Get all the depositAccountList where revaluedInterestAmount not equals to UPDATED_REVALUED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedInterestAmount.notEquals=" + UPDATED_REVALUED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedInterestAmountIsInShouldWork() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedInterestAmount in DEFAULT_REVALUED_INTEREST_AMOUNT or UPDATED_REVALUED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedInterestAmount.in=" + DEFAULT_REVALUED_INTEREST_AMOUNT + "," + UPDATED_REVALUED_INTEREST_AMOUNT);

        // Get all the depositAccountList where revaluedInterestAmount equals to UPDATED_REVALUED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedInterestAmount.in=" + UPDATED_REVALUED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedInterestAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedInterestAmount is not null
        defaultDepositAccountShouldBeFound("revaluedInterestAmount.specified=true");

        // Get all the depositAccountList where revaluedInterestAmount is null
        defaultDepositAccountShouldNotBeFound("revaluedInterestAmount.specified=false");
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedInterestAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedInterestAmount is greater than or equal to DEFAULT_REVALUED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedInterestAmount.greaterThanOrEqual=" + DEFAULT_REVALUED_INTEREST_AMOUNT);

        // Get all the depositAccountList where revaluedInterestAmount is greater than or equal to UPDATED_REVALUED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedInterestAmount.greaterThanOrEqual=" + UPDATED_REVALUED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedInterestAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedInterestAmount is less than or equal to DEFAULT_REVALUED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedInterestAmount.lessThanOrEqual=" + DEFAULT_REVALUED_INTEREST_AMOUNT);

        // Get all the depositAccountList where revaluedInterestAmount is less than or equal to SMALLER_REVALUED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedInterestAmount.lessThanOrEqual=" + SMALLER_REVALUED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedInterestAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedInterestAmount is less than DEFAULT_REVALUED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedInterestAmount.lessThan=" + DEFAULT_REVALUED_INTEREST_AMOUNT);

        // Get all the depositAccountList where revaluedInterestAmount is less than UPDATED_REVALUED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedInterestAmount.lessThan=" + UPDATED_REVALUED_INTEREST_AMOUNT);
    }

    @Test
    @Transactional
    public void getAllDepositAccountsByRevaluedInterestAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        // Get all the depositAccountList where revaluedInterestAmount is greater than DEFAULT_REVALUED_INTEREST_AMOUNT
        defaultDepositAccountShouldNotBeFound("revaluedInterestAmount.greaterThan=" + DEFAULT_REVALUED_INTEREST_AMOUNT);

        // Get all the depositAccountList where revaluedInterestAmount is greater than SMALLER_REVALUED_INTEREST_AMOUNT
        defaultDepositAccountShouldBeFound("revaluedInterestAmount.greaterThan=" + SMALLER_REVALUED_INTEREST_AMOUNT);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDepositAccountShouldBeFound(String filter) throws Exception {
        restDepositAccountMockMvc.perform(get("/api/app/deposit-accounts?sort=id,desc&" + filter))
                                 .andExpect(status().isOk())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                                 .andExpect(jsonPath("$.[*].id").value(hasItem(depositAccount.getId().intValue())))
                                 .andExpect(jsonPath("$.[*].sbuCode").value(hasItem(DEFAULT_SBU_CODE)))
                                 .andExpect(jsonPath("$.[*].rmCode").value(hasItem(DEFAULT_RM_CODE)))
                                 .andExpect(jsonPath("$.[*].schemeCode").value(hasItem(DEFAULT_SCHEME_CODE)))
                                 .andExpect(jsonPath("$.[*].glCode").value(hasItem(DEFAULT_GL_CODE)))
                                 .andExpect(jsonPath("$.[*].currencyCode").value(hasItem(DEFAULT_CURRENCY_CODE)))
                                 .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
                                 .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
                                 .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
                                 .andExpect(jsonPath("$.[*].accountBalance").value(hasItem(DEFAULT_ACCOUNT_BALANCE.intValue())))
                                 .andExpect(jsonPath("$.[*].sector").value(hasItem(DEFAULT_SECTOR)))
                                 .andExpect(jsonPath("$.[*].subSector").value(hasItem(DEFAULT_SUB_SECTOR)))
                                 .andExpect(jsonPath("$.[*].accountOpeningDate").value(hasItem(DEFAULT_ACCOUNT_OPENING_DATE.toString())))
                                 .andExpect(jsonPath("$.[*].accountMaturityDate").value(hasItem(DEFAULT_ACCOUNT_MATURITY_DATE.toString())))
                                 .andExpect(jsonPath("$.[*].accountStatus").value(hasItem(DEFAULT_ACCOUNT_STATUS)))
                                 .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())))
                                 .andExpect(jsonPath("$.[*].bookedInterestAmount").value(hasItem(DEFAULT_BOOKED_INTEREST_AMOUNT.intValue())))
                                 .andExpect(jsonPath("$.[*].interestAmount").value(hasItem(DEFAULT_INTEREST_AMOUNT.intValue())))
                                 .andExpect(jsonPath("$.[*].accruedInterestAmount").value(hasItem(DEFAULT_ACCRUED_INTEREST_AMOUNT.intValue())))
                                 .andExpect(jsonPath("$.[*].depositScheme").value(hasItem(DEFAULT_DEPOSIT_SCHEME)))
                                 .andExpect(jsonPath("$.[*].revaluedTotalAmount").value(hasItem(DEFAULT_REVALUED_TOTAL_AMOUNT.intValue())))
                                 .andExpect(jsonPath("$.[*].revaluedPrincipalAmount").value(hasItem(DEFAULT_REVALUED_PRINCIPAL_AMOUNT.intValue())))
                                 .andExpect(jsonPath("$.[*].revaluedInterestAmount").value(hasItem(DEFAULT_REVALUED_INTEREST_AMOUNT.intValue())));

        // Check, that the count call also returns 1
        restDepositAccountMockMvc.perform(get("/api/app/deposit-accounts/count?sort=id,desc&" + filter))
                                 .andExpect(status().isOk())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                                 .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDepositAccountShouldNotBeFound(String filter) throws Exception {
        restDepositAccountMockMvc.perform(get("/api/app/deposit-accounts?sort=id,desc&" + filter))
                                 .andExpect(status().isOk())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                                 .andExpect(jsonPath("$").isArray())
                                 .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDepositAccountMockMvc.perform(get("/api/app/deposit-accounts/count?sort=id,desc&" + filter))
                                 .andExpect(status().isOk())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                                 .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingDepositAccount() throws Exception {
        // Get the depositAccount
        restDepositAccountMockMvc.perform(get("/api/app/deposit-accounts/{id}", Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDepositAccount() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        int databaseSizeBeforeUpdate = depositAccountRepository.findAll().size();

        // Update the depositAccount
        DepositAccount updatedDepositAccount = depositAccountRepository.findById(depositAccount.getId()).get();
        // Disconnect from session so that the updates on updatedDepositAccount are not directly saved in db
        em.detach(updatedDepositAccount);
        updatedDepositAccount.sbuCode(UPDATED_SBU_CODE)
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
                             .revaluedInterestAmount(UPDATED_REVALUED_INTEREST_AMOUNT);
        DepositAccountDTO depositAccountDTO = depositAccountMapper.toDto(updatedDepositAccount);

        restDepositAccountMockMvc.perform(put("/api/app/deposit-accounts").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(depositAccountDTO)))
                                 .andExpect(status().isOk());

        // Validate the DepositAccount in the database
        List<DepositAccount> depositAccountList = depositAccountRepository.findAll();
        assertThat(depositAccountList).hasSize(databaseSizeBeforeUpdate);
        DepositAccount testDepositAccount = depositAccountList.get(depositAccountList.size() - 1);
        assertThat(testDepositAccount.getSbuCode()).isEqualTo(UPDATED_SBU_CODE);
        assertThat(testDepositAccount.getRmCode()).isEqualTo(UPDATED_RM_CODE);
        assertThat(testDepositAccount.getSchemeCode()).isEqualTo(UPDATED_SCHEME_CODE);
        assertThat(testDepositAccount.getGlCode()).isEqualTo(UPDATED_GL_CODE);
        assertThat(testDepositAccount.getCurrencyCode()).isEqualTo(UPDATED_CURRENCY_CODE);
        assertThat(testDepositAccount.getCustomerId()).isEqualTo(UPDATED_CUSTOMER_ID);
        assertThat(testDepositAccount.getAccountNumber()).isEqualTo(UPDATED_ACCOUNT_NUMBER);
        assertThat(testDepositAccount.getAccountName()).isEqualTo(UPDATED_ACCOUNT_NAME);
        assertThat(testDepositAccount.getAccountBalance()).isEqualTo(UPDATED_ACCOUNT_BALANCE);
        assertThat(testDepositAccount.getSector()).isEqualTo(UPDATED_SECTOR);
        assertThat(testDepositAccount.getSubSector()).isEqualTo(UPDATED_SUB_SECTOR);
        assertThat(testDepositAccount.getAccountOpeningDate()).isEqualTo(UPDATED_ACCOUNT_OPENING_DATE);
        assertThat(testDepositAccount.getAccountMaturityDate()).isEqualTo(UPDATED_ACCOUNT_MATURITY_DATE);
        assertThat(testDepositAccount.getAccountStatus()).isEqualTo(UPDATED_ACCOUNT_STATUS);
        assertThat(testDepositAccount.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testDepositAccount.getBookedInterestAmount()).isEqualTo(UPDATED_BOOKED_INTEREST_AMOUNT);
        assertThat(testDepositAccount.getInterestAmount()).isEqualTo(UPDATED_INTEREST_AMOUNT);
        assertThat(testDepositAccount.getAccruedInterestAmount()).isEqualTo(UPDATED_ACCRUED_INTEREST_AMOUNT);
        assertThat(testDepositAccount.getDepositScheme()).isEqualTo(UPDATED_DEPOSIT_SCHEME);
        assertThat(testDepositAccount.getRevaluedTotalAmount()).isEqualTo(UPDATED_REVALUED_TOTAL_AMOUNT);
        assertThat(testDepositAccount.getRevaluedPrincipalAmount()).isEqualTo(UPDATED_REVALUED_PRINCIPAL_AMOUNT);
        assertThat(testDepositAccount.getRevaluedInterestAmount()).isEqualTo(UPDATED_REVALUED_INTEREST_AMOUNT);

        // Validate the DepositAccount in Elasticsearch
        verify(mockDepositAccountSearchRepository, times(1)).save(testDepositAccount);
    }

    @Test
    @Transactional
    public void updateNonExistingDepositAccount() throws Exception {
        int databaseSizeBeforeUpdate = depositAccountRepository.findAll().size();

        // Create the DepositAccount
        DepositAccountDTO depositAccountDTO = depositAccountMapper.toDto(depositAccount);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDepositAccountMockMvc.perform(put("/api/app/deposit-accounts").contentType(TestUtil.APPLICATION_JSON_UTF8).content(TestUtil.convertObjectToJsonBytes(depositAccountDTO)))
                                 .andExpect(status().isBadRequest());

        // Validate the DepositAccount in the database
        List<DepositAccount> depositAccountList = depositAccountRepository.findAll();
        assertThat(depositAccountList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DepositAccount in Elasticsearch
        verify(mockDepositAccountSearchRepository, times(0)).save(depositAccount);
    }

    @Test
    @Transactional
    public void deleteDepositAccount() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);

        int databaseSizeBeforeDelete = depositAccountRepository.findAll().size();

        // Delete the depositAccount
        restDepositAccountMockMvc.perform(delete("/api/app/deposit-accounts/{id}", depositAccount.getId()).accept(TestUtil.APPLICATION_JSON_UTF8)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DepositAccount> depositAccountList = depositAccountRepository.findAll();
        assertThat(depositAccountList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DepositAccount in Elasticsearch
        verify(mockDepositAccountSearchRepository, times(1)).deleteById(depositAccount.getId());
    }

    @Test
    @Transactional
    public void searchDepositAccount() throws Exception {
        // Initialize the database
        depositAccountRepository.saveAndFlush(depositAccount);
        when(mockDepositAccountSearchRepository.search(queryStringQuery("id:" + depositAccount.getId()), PageRequest.of(0, 20))).thenReturn(
            new PageImpl<>(Collections.singletonList(depositAccount), PageRequest.of(0, 1), 1));
        // Search the depositAccount
        restDepositAccountMockMvc.perform(get("/api/app/_search/deposit-accounts?query=id:" + depositAccount.getId()))
                                 .andExpect(status().isOk())
                                 .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                                 .andExpect(jsonPath("$.[*].id").value(hasItem(depositAccount.getId().intValue())))
                                 .andExpect(jsonPath("$.[*].sbuCode").value(hasItem(DEFAULT_SBU_CODE)))
                                 .andExpect(jsonPath("$.[*].rmCode").value(hasItem(DEFAULT_RM_CODE)))
                                 .andExpect(jsonPath("$.[*].schemeCode").value(hasItem(DEFAULT_SCHEME_CODE)))
                                 .andExpect(jsonPath("$.[*].glCode").value(hasItem(DEFAULT_GL_CODE)))
                                 .andExpect(jsonPath("$.[*].currencyCode").value(hasItem(DEFAULT_CURRENCY_CODE)))
                                 .andExpect(jsonPath("$.[*].customerId").value(hasItem(DEFAULT_CUSTOMER_ID)))
                                 .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
                                 .andExpect(jsonPath("$.[*].accountName").value(hasItem(DEFAULT_ACCOUNT_NAME)))
                                 .andExpect(jsonPath("$.[*].accountBalance").value(hasItem(DEFAULT_ACCOUNT_BALANCE.intValue())))
                                 .andExpect(jsonPath("$.[*].sector").value(hasItem(DEFAULT_SECTOR)))
                                 .andExpect(jsonPath("$.[*].subSector").value(hasItem(DEFAULT_SUB_SECTOR)))
                                 .andExpect(jsonPath("$.[*].accountOpeningDate").value(hasItem(DEFAULT_ACCOUNT_OPENING_DATE.toString())))
                                 .andExpect(jsonPath("$.[*].accountMaturityDate").value(hasItem(DEFAULT_ACCOUNT_MATURITY_DATE.toString())))
                                 .andExpect(jsonPath("$.[*].accountStatus").value(hasItem(DEFAULT_ACCOUNT_STATUS)))
                                 .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.doubleValue())))
                                 .andExpect(jsonPath("$.[*].bookedInterestAmount").value(hasItem(DEFAULT_BOOKED_INTEREST_AMOUNT.intValue())))
                                 .andExpect(jsonPath("$.[*].interestAmount").value(hasItem(DEFAULT_INTEREST_AMOUNT.intValue())))
                                 .andExpect(jsonPath("$.[*].accruedInterestAmount").value(hasItem(DEFAULT_ACCRUED_INTEREST_AMOUNT.intValue())))
                                 .andExpect(jsonPath("$.[*].depositScheme").value(hasItem(DEFAULT_DEPOSIT_SCHEME)))
                                 .andExpect(jsonPath("$.[*].revaluedTotalAmount").value(hasItem(DEFAULT_REVALUED_TOTAL_AMOUNT.intValue())))
                                 .andExpect(jsonPath("$.[*].revaluedPrincipalAmount").value(hasItem(DEFAULT_REVALUED_PRINCIPAL_AMOUNT.intValue())))
                                 .andExpect(jsonPath("$.[*].revaluedInterestAmount").value(hasItem(DEFAULT_REVALUED_INTEREST_AMOUNT.intValue())));
    }
}
