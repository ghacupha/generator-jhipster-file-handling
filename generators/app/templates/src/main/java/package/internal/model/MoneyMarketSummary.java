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
public class MoneyMarketSummary implements Serializable {
    private static final long serialVersionUID = 481090532420998658L;
    private CurrencyLocality currencyDomicile;
    private String sbuName;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private BigDecimal totalAmount;
    private double lowestRate;
    private double highestRate;
    private double averageRate;
}
