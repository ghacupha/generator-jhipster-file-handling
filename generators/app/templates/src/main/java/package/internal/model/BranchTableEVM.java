package io.github.deposits.app.model;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelRow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BranchTableEVM implements Serializable {
    private static final long serialVersionUID = -4415428545780013781L;

    @ExcelRow
    private int rowIndex;

    @ExcelCell(0)
    private String branchName;

    @ExcelCell(1)
    private String serviceOutletCode;

    @ExcelCell(2)
    private String county;

    @ExcelCell(3)
    private String town;

    @ExcelCell(4)
    private int yearOpened;
}
