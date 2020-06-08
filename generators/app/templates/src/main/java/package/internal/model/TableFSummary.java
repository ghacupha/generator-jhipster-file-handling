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

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
@XmlRootElement
public class TableFSummary implements Serializable {
    private static final long serialVersionUID = 7046301177611442410L;

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
