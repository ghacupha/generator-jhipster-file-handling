package io.github.deposits.app.model;

import io.github.deposits.domain.enumeration.CurrencyLocality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * This is a summary for Table F that also contains bucketed periods where we would
 * simply use the type term deposits
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
@XmlRootElement
public class TableFNewSummary implements Serializable {
    private static final long serialVersionUID = 6799952824624541340L;
    /**
     * Whether the row relates to local of foreign currency
     */
    private CurrencyLocality currencyLocality;

    /**
     * The name of the type of deposit
     */
    private String depositType;

    /**
     * Total i.e. principal of the deposit type
     */
    private BigDecimal totalPrincipalAmount;

    /**
     * Total i.e. interest of the deposit type
     */
    private BigDecimal totalInterestAmount;

    /**
     * Total i.e. principal + interest of the deposit type
     */
    private BigDecimal totalRevaluedAmount;

    /**
     * Average wighted rate of the deposit type
     */
    private double weightedRate;
}
