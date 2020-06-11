package <%= packageName %>.internal.model.sampleDataModel;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import java.io.Serializable;

/**
 * A CurrencyTable.
 */
@Entity
@Table(name = "currency_table")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@org.springframework.data.elasticsearch.annotations.Document(indexName = "currencytable")
public class CurrencyTable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Size(min = 3, max = 3)
    @Column(name = "currency_code", length = 3, unique = true)
    private String currencyCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "locality", nullable = false)
    private CurrencyLocality locality;

    @Column(name = "currency_name")
    private String currencyName;

    @Column(name = "country")
    private String country;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public CurrencyTable currencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
        return this;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public CurrencyLocality getLocality() {
        return locality;
    }

    public CurrencyTable locality(CurrencyLocality locality) {
        this.locality = locality;
        return this;
    }

    public void setLocality(CurrencyLocality locality) {
        this.locality = locality;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public CurrencyTable currencyName(String currencyName) {
        this.currencyName = currencyName;
        return this;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCountry() {
        return country;
    }

    public CurrencyTable country(String country) {
        this.country = country;
        return this;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CurrencyTable)) {
            return false;
        }
        return id != null && id.equals(((CurrencyTable) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CurrencyTable{" +
            "id=" + getId() +
            ", currencyCode='" + getCurrencyCode() + "'" +
            ", locality='" + getLocality() + "'" +
            ", currencyName='" + getCurrencyName() + "'" +
            ", country='" + getCountry() + "'" +
            "}";
    }
}
