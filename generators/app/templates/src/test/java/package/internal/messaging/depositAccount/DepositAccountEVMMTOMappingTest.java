package io.github.deposits.app.messaging.depositAccount;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelRow;
import io.github.deposits.app.model.DepositAccountEVM;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class DepositAccountEVMMTOMappingTest {

    private DepositAccountEVMMTOMapping depositAccountEVMMTOMapping;

    private DepositAccountEVM depositAccountEVM;

    @BeforeEach
    void setUp() {
        depositAccountEVMMTOMapping = new DepositAccountEVMMTOMappingImpl();
        depositAccountEVM = DepositAccountEVM.builder()
                                             .revaluedTotalAmount(300.00)
                                             .revaluedPrincipalAmount(200.00)
                                             .revaluedInterestAmount(100.00)
                                             .accountOpeningDate("2019/01/01")
                                             .accountMaturityDate("2019/12/31")
                                             .monthOfStudy("2019/09/01")
                                             .build();
    }

    @Test
    void testMapping() {

        assertThat(depositAccountEVMMTOMapping.toValue2(depositAccountEVM).getRevaluedTotalAmount()).isEqualTo(300.00);
        assertThat(depositAccountEVMMTOMapping.toValue2(depositAccountEVM).getRevaluedPrincipalAmount()).isEqualTo(200.00);
        assertThat(depositAccountEVMMTOMapping.toValue2(depositAccountEVM).getRevaluedTotalAmount()).isEqualTo(300.00);
        assertThat(depositAccountEVMMTOMapping.toValue2(depositAccountEVM).getAccountOpeningDate()).isEqualTo("2019/01/01");
        assertThat(depositAccountEVMMTOMapping.toValue2(depositAccountEVM).getAccountMaturityDate()).isEqualTo("2019/12/31");
        assertThat(depositAccountEVMMTOMapping.toValue2(depositAccountEVM).getMonthOfStudy()).isEqualTo("2019/09/01");
    }
}
