package <%= packageName %>.internal.model.sampleDataModel;

import com.poiji.annotation.ExcelCell;
import com.poiji.annotation.ExcelRow;

import java.io.Serializable;

/**
 * This is a sample entity configured for reading data from an excel file whose
 * columns are as configured in the fields bellow cell by cell and by data-type.
 * This means there should exist a similar excel containing this 4 columns; representing
 * types or currencies their localities and country where they are used
 */
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

    public CurrencyTableEVM(int rowIndex, String currencyCode, String locality, String currencyName, String country) {
            this.rowIndex = rowIndex;
            this.currencyCode = currencyCode;
            this.locality = locality;
            this.currencyName = currencyName;
            this.country = country;
    }

    public CurrencyTableEVM() {
    }

    public static CurrencyTableEVMBuilder builder() {
        return new CurrencyTableEVMBuilder();
    }

    public int getRowIndex() {
        return this.rowIndex;
    }

    public String getCurrencyCode() {
        return this.currencyCode;
    }

    public String getLocality() {
        return this.locality;
    }

    public String getCurrencyName() {
        return this.currencyName;
    }

    public String getCountry() {
        return this.country;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof CurrencyTableEVM)) return false;
        final CurrencyTableEVM other = (CurrencyTableEVM) o;
        if (!other.canEqual((Object) this)) return false;
        if (this.getRowIndex() != other.getRowIndex()) return false;
        final Object this$currencyCode = this.getCurrencyCode();
        final Object other$currencyCode = other.getCurrencyCode();
        if (this$currencyCode == null ? other$currencyCode != null : !this$currencyCode.equals(other$currencyCode))
            return false;
        final Object this$locality = this.getLocality();
        final Object other$locality = other.getLocality();
        if (this$locality == null ? other$locality != null : !this$locality.equals(other$locality)) return false;
        final Object this$currencyName = this.getCurrencyName();
        final Object other$currencyName = other.getCurrencyName();
        if (this$currencyName == null ? other$currencyName != null : !this$currencyName.equals(other$currencyName))
            return false;
        final Object this$country = this.getCountry();
        final Object other$country = other.getCountry();
        if (this$country == null ? other$country != null : !this$country.equals(other$country)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof CurrencyTableEVM;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getRowIndex();
        final Object $currencyCode = this.getCurrencyCode();
        result = result * PRIME + ($currencyCode == null ? 43 : $currencyCode.hashCode());
        final Object $locality = this.getLocality();
        result = result * PRIME + ($locality == null ? 43 : $locality.hashCode());
        final Object $currencyName = this.getCurrencyName();
        result = result * PRIME + ($currencyName == null ? 43 : $currencyName.hashCode());
        final Object $country = this.getCountry();
        result = result * PRIME + ($country == null ? 43 : $country.hashCode());
        return result;
    }

    public String toString() {
        return "CurrencyTableEVM(rowIndex=" + this.getRowIndex() + ", currencyCode=" + this.getCurrencyCode() + ", locality=" + this.getLocality() + ", currencyName=" + this.getCurrencyName() + ", country=" + this.getCountry() + ")";
    }

    public static class CurrencyTableEVMBuilder {
        private int rowIndex;
        private String currencyCode;
        private String locality;
        private String currencyName;
        private String country;

        CurrencyTableEVMBuilder() {
        }

        public CurrencyTableEVM.CurrencyTableEVMBuilder rowIndex(int rowIndex) {
            this.rowIndex = rowIndex;
            return this;
        }

        public CurrencyTableEVM.CurrencyTableEVMBuilder currencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
            return this;
        }

        public CurrencyTableEVM.CurrencyTableEVMBuilder locality(String locality) {
            this.locality = locality;
            return this;
        }

        public CurrencyTableEVM.CurrencyTableEVMBuilder currencyName(String currencyName) {
            this.currencyName = currencyName;
            return this;
        }

        public CurrencyTableEVM.CurrencyTableEVMBuilder country(String country) {
            this.country = country;
            return this;
        }

        public CurrencyTableEVM build() {
            return new CurrencyTableEVM(rowIndex, currencyCode, locality, currencyName, country);
        }

        public String toString() {
            return "CurrencyTableEVM.CurrencyTableEVMBuilder(rowIndex=" + this.rowIndex + ", currencyCode=" + this.currencyCode + ", locality=" + this.locality + ", currencyName=" + this.currencyName + ", country=" + this.country + ")";
        }
    }
}
