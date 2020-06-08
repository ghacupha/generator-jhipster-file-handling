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
public class DPFSummaryParameters implements Serializable {

    private static final long serialVersionUID = 2296737070752484665L;
    private LocalDate monthOfStudy;
}
