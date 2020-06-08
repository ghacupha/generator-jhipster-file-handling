package io.github.deposits.app.model;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelRow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class SBUTableEVM implements Serializable {
    private static final long serialVersionUID = -8455171278497220063L;
    @ExcelRow
    private int rowIndex;
    @ExcelCell(0)
    private String sbuCode;
    @ExcelCell(1)
    private String sbuName;
}
