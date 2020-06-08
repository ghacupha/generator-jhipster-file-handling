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
public class BranchSummary implements Serializable {
    private static final long serialVersionUID = -5475810106637946078L;
    private String nameOfBranch;
    private String kbaBranchCode;
    private String county;
    private String town;
    private int yearOpened;
    private long numberOfAccounts;
    private BigDecimal depositVolume;
}
