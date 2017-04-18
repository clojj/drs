package de.fisp.skp.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Datom {

    @EmbeddedId
    private DatomId datomId;

    @Column
    private String valueString;

    @Column
    private Boolean valueBoolean;

    @Column
    private BigDecimal valueBigDecimal;

    @Column
    private Integer valueInteger;

    @Column
    private Date valueDate;

    public DatomId getDatomId() {
        return datomId;
    }

    public void setDatomId(DatomId datomId) {
        this.datomId = datomId;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String value) {
        this.valueString = value;
    }

    public Boolean getValueBoolean() {
        return valueBoolean;
    }

    public void setValueBoolean(Boolean valueBoolean) {
        this.valueBoolean = valueBoolean;
    }

    public BigDecimal getValueBigDecimal() {
        return valueBigDecimal;
    }

    public void setValueBigDecimal(BigDecimal valueBigDecimal) {
        this.valueBigDecimal = valueBigDecimal;
    }

    public Integer getValueInteger() {
        return valueInteger;
    }

    public void setValueInteger(Integer valueInteger) {
        this.valueInteger = valueInteger;
    }

    public Date getValueDate() {
        return valueDate;
    }

    public void setValueDate(Date valueDate) {
        this.valueDate = valueDate;
    }

    @Override
    public String toString() {
        return "Datom{" +
                "datomId=" + datomId +
                ", valueString='" + valueString + '\'' +
                ", valueBoolean=" + valueBoolean +
                ", valueBigDecimal=" + valueBigDecimal +
                ", valueInteger=" + valueInteger +
                ", valueDate=" + valueDate +
                '}';
    }
}
