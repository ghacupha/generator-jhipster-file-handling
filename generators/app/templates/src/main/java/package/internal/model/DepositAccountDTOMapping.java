package io.github.deposits.app.model;

import io.github.deposits.app.Mapping;
import io.github.deposits.service.dto.DepositAccountDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static io.github.deposits.app.AppConstants.DATETIME_FORMATTER;
import static org.apache.commons.lang3.math.NumberUtils.toDouble;
import static org.apache.commons.lang3.math.NumberUtils.toScaledBigDecimal;

@Component("depositAccountDTOMapping")
public class DepositAccountDTOMapping implements Mapping<DepositAccountEVM, DepositAccountDTO> {

    @Override
    public DepositAccountEVM toValue1(final DepositAccountDTO vs) {
        return DepositAccountEVM.builder()
                                .sbuCode(vs.getSbuCode())
                                .rmCode(vs.getRmCode())
                                .schemeCode(vs.getSchemeCode())
                                .glCode(vs.getGlCode())
                                .currencyCode(vs.getCurrencyCode())
                                .customerId(vs.getCustomerId())
                                .accountNumber(vs.getAccountNumber())
                                .accountName(vs.getAccountName())
                                .accountBalance(toDouble(vs.getAccountBalance()))
                                .sector(vs.getSector())
                                .subSector(vs.getSubSector())
                                .accountOpeningDate(DATETIME_FORMATTER.format(vs.getAccountOpeningDate()))
                                .accountMaturityDate(DATETIME_FORMATTER.format(vs.getAccountMaturityDate()))
                                .accountStatus(vs.getAccountStatus())
                                .rate(vs.getRate())
                                .bookedInterestAmount(toDouble(vs.getBookedInterestAmount()))
                                .interestAmount(toDouble(vs.getInterestAmount()))
                                .accruedInterestAmount(toDouble(vs.getAccruedInterestAmount()))
                                .depositScheme(vs.getDepositScheme())
                                .revaluedTotalAmount(toDouble(vs.getRevaluedTotalAmount()))
                                .revaluedPrincipalAmount(toDouble(vs.getRevaluedPrincipalAmount()))
                                .revaluedInterestAmount(toDouble(vs.getRevaluedInterestAmount()))
                                .build();
    }


    @Override
    public DepositAccountDTO toValue2(final DepositAccountEVM vs) {
        DepositAccountDTO depositAccountDTO = new DepositAccountDTO();

        // depositAccountDTO.setId(vs.getId()); // Set by hibernate
        depositAccountDTO.setSbuCode(vs.getSbuCode());
        depositAccountDTO.setRmCode(vs.getRmCode());
        depositAccountDTO.setSchemeCode(vs.getSchemeCode());
        depositAccountDTO.setGlCode(vs.getGlCode());
        depositAccountDTO.setCurrencyCode(vs.getCurrencyCode());
        depositAccountDTO.setCustomerId(vs.getCustomerId());
        depositAccountDTO.setAccountNumber(vs.getAccountNumber());
        depositAccountDTO.setAccountName(vs.getAccountName());
        depositAccountDTO.setAccountBalance(toScaledBigDecimal(vs.getAccountBalance()));
        depositAccountDTO.setSector(vs.getSector());
        depositAccountDTO.setSubSector(vs.getSubSector());
        if (vs.getAccountOpeningDate() != null) {
            depositAccountDTO.setAccountOpeningDate(LocalDate.parse(vs.getAccountOpeningDate(), DATETIME_FORMATTER));
        }
        if (vs.getAccountMaturityDate() != null) {
            depositAccountDTO.setAccountMaturityDate(LocalDate.parse(vs.getAccountMaturityDate(), DATETIME_FORMATTER));
        }
        depositAccountDTO.setAccountStatus(vs.getAccountStatus());
        depositAccountDTO.setRate(vs.getRate());
        depositAccountDTO.setBookedInterestAmount(toScaledBigDecimal(vs.getBookedInterestAmount()));
        depositAccountDTO.setInterestAmount(toScaledBigDecimal(vs.getInterestAmount()));
        depositAccountDTO.setAccruedInterestAmount(toScaledBigDecimal(vs.getAccruedInterestAmount()));
        depositAccountDTO.setDepositScheme(vs.getDepositScheme());
        depositAccountDTO.setRevaluedTotalAmount(toScaledBigDecimal(vs.getRevaluedTotalAmount()));
        depositAccountDTO.setRevaluedPrincipalAmount(toScaledBigDecimal(vs.getRevaluedPrincipalAmount()));
        depositAccountDTO.setRevaluedInterestAmount(toScaledBigDecimal(vs.getRevaluedInterestAmount()));
        if (vs.getMonthOfStudy() != null) {
            depositAccountDTO.setMonthOfStudy(LocalDate.parse(vs.getMonthOfStudy(), DATETIME_FORMATTER));
        }

        return depositAccountDTO;
    }
}
