package io.github.deposits.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * This is a projection on which we map the DPF summary report
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
@XmlRootElement
public class DPFSummary implements Serializable {

    private static final long serialVersionUID = 3097704708600531740L;
    private String depositRange;
    private String typeOfDeposit;
    private long numberOfDeposits;
    private BigDecimal totalAmount;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private double averageRate;
}
