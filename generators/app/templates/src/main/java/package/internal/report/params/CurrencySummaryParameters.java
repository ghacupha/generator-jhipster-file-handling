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
public class CurrencySummaryParameters implements Serializable {
    private static final long serialVersionUID = -3635891659977552835L;
    private LocalDate monthOfStudy;
}
