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
public class TableFSummaryParameters implements Serializable {
    private static final long serialVersionUID = -6309614689517057504L;
    private LocalDate monthOfStudy;
}
