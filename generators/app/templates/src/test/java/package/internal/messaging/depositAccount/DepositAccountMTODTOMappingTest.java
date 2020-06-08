package io.github.deposits.app.messaging.depositAccount;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.github.deposits.app.AppConstants.DATETIME_FORMATTER;
import static org.assertj.core.api.Assertions.assertThat;

public class DepositAccountMTODTOMappingTest {

    private DepositAccountMTODTOMapping depositAccountMTODTOMapping;

    private DepositAccountMTO depositAccountMTO;

    @BeforeEach
    void setUp() {
        depositAccountMTODTOMapping = new DepositAccountMTODTOMappingImpl();

        depositAccountMTO = DepositAccountMTO.builder()
                                             .timestamp(1L)
                                             .messageToken("02620kljl")
                                             .description("Test MTO")
                                             .revaluedTotalAmount(300.00)
                                             .revaluedPrincipalAmount(200.00)
                                             .revaluedInterestAmount(100.00)
                                             .accountOpeningDate("2019-01-01")
                                             .accountMaturityDate("2019-12-31")
                                             .monthOfStudy("2019-09-01")
                                             .build();
    }

    @Test
    public void testFieldsWithCustomConversions() {
        assertThat(depositAccountMTODTOMapping.toValue2(depositAccountMTO).getRevaluedInterestAmount()).isEqualTo(BigDecimal.valueOf(100.00));
        assertThat(depositAccountMTODTOMapping.toValue2(depositAccountMTO).getRevaluedPrincipalAmount()).isEqualTo(BigDecimal.valueOf(200.00));
        assertThat(depositAccountMTODTOMapping.toValue2(depositAccountMTO).getRevaluedTotalAmount()).isEqualTo(BigDecimal.valueOf(300.00));
        assertThat(depositAccountMTODTOMapping.toValue2(depositAccountMTO).getMonthOfStudy()).isEqualTo(LocalDate.parse("2019/09/01", DATETIME_FORMATTER));
        assertThat(depositAccountMTODTOMapping.toValue2(depositAccountMTO).getAccountOpeningDate()).isEqualTo(LocalDate.parse("2019/01/01", DATETIME_FORMATTER));
        assertThat(depositAccountMTODTOMapping.toValue2(depositAccountMTO).getAccountMaturityDate()).isEqualTo(LocalDate.parse("2019/12/31", DATETIME_FORMATTER));
    }
}
