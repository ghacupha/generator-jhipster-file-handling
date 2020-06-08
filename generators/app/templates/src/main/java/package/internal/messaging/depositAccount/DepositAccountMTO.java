package io.github.deposits.app.messaging.depositAccount;

import io.github.deposits.app.messaging.platform.TokenizableMessage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * This object is used to ferry data across the message-channel and upon reaching the
 * listener it is deserialized back to domain DTO
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class DepositAccountMTO implements Serializable, TokenizableMessage<String> {

    // Tokenizable Properties
    private long timestamp;
    private String messageToken;
    private String description;

    // Deposit Properties

    private String sbuCode;

    private String rmCode;

    private String schemeCode;

    private String glCode;

    private String currencyCode;

    private String customerId;

    private String accountNumber;

    private String accountName;

    private double accountBalance;

    private String sector;

    private String subSector;

    private String accountOpeningDate;

    private String accountMaturityDate;

    private String accountStatus;

    private double rate;

    private double bookedInterestAmount;

    private double interestAmount;

    private double accruedInterestAmount;

    private String depositScheme;

    private double revaluedTotalAmount;

    private double revaluedPrincipalAmount;

    private double revaluedInterestAmount;

    private String monthOfStudy;
}
