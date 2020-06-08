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
public class CurrencySummary implements Serializable {
    private static final long serialVersionUID = -1045677876353598267L;
    private String currencyCode;
    private BigDecimal volume;
    private long numberOfAccounts;
}
