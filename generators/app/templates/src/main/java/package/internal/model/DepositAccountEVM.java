package io.github.deposits.app.model;

import java.io.Serializable;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelRow;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class DepositAccountEVM implements Serializable{

    @ExcelRow
    private int rowIndex;

    @ExcelCell(0)
    private String sbuCode;

    @ExcelCell(1)
    private String rmCode;

    @ExcelCell(2)
    private String schemeCode;

    @ExcelCell(3)
    private String glCode;

    @ExcelCell(4)
    private String currencyCode;

    @ExcelCell(5)
    private String customerId;

    @ExcelCell(6)
    private String accountNumber;

    @ExcelCell(7)
    private String accountName;

    @ExcelCell(8)
    private double accountBalance;

    @ExcelCell(9)
    private String sector;

    @ExcelCell(10)
    private String subSector;

    @ExcelCell(11)
    private String accountOpeningDate;

    @ExcelCell(12)
    private String accountMaturityDate;

    @ExcelCell(13)
    private String accountStatus;

    @ExcelCell(14)
    private double rate;

    @ExcelCell(15)
    private double bookedInterestAmount;

    @ExcelCell(16)
    private double interestAmount;

    @ExcelCell(17)
    private double accruedInterestAmount;

    @ExcelCell(18)
    private String depositScheme;

    @ExcelCell(19)
    private double revaluedTotalAmount;

    @ExcelCell(20)
    private double revaluedPrincipalAmount;

    @ExcelCell(21)
    private double revaluedInterestAmount;

    @ExcelCell(22)
    private String monthOfStudy;
}
