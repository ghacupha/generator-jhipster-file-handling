package io.github.deposits.app.model;

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
public class SBUSummary implements Serializable {
    private static final long serialVersionUID = 481090532420998658L;
    private String sbuName;
    private BigDecimal principalAmount;
    private BigDecimal interestAmount;
    private BigDecimal totalAmount;
    private double lowestRate;
    private double highestRate;
    private double averageRate;
}
