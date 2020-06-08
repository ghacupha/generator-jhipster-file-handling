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
public class CurrencyTableEVM implements Serializable {
    private static final long serialVersionUID = 4731854415270415743L;

    @ExcelRow
    private int rowIndex;

    @ExcelCell(0)
    private String currencyCode;

    @ExcelCell(1)
    private String locality;

    @ExcelCell(2)
    private String currencyName;

    @ExcelCell(3)
    private String country;
}
