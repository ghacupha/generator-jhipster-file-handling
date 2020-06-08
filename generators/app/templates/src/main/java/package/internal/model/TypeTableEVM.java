package io.github.deposits.app.model;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelRow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class TypeTableEVM implements Serializable {
    private static final long serialVersionUID = -3086137532568386516L;

    @ExcelRow
    private int rowIndex;

    @ExcelCell(0)
    private String glCode;

    @ExcelCell(1)
    private String typeDefinition;

    @ExcelCell(2)
    private String tableFDefinition;

    @ExcelCell(3)
    private String dpfDefinition;
}
