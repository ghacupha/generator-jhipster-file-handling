package io.github.deposits.app.report.params;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class MoneyMarketSummaryParameters implements Serializable {
    private static final long serialVersionUID = 6610178437592378777L;
    private LocalDate monthOfStudy;
}
