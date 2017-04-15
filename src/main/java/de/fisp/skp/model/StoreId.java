package de.fisp.skp.model;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.sql.Timestamp;

@Embeddable
public class StoreId implements Serializable {

    @Column
    private String darlehen;

    @Column
    private String fieldname;

    @Column(name = "valid_from")
    private Timestamp validFrom;

    public String getDarlehen() {
        return darlehen;
    }

    public void setDarlehen(String darlehen) {
        this.darlehen = darlehen;
    }

    public String getFieldname() {
        return fieldname;
    }

    public void setFieldname(String fieldname) {
        this.fieldname = fieldname;
    }

    public Timestamp getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Timestamp validFrom) {
        this.validFrom = validFrom;
    }

    @Override
    public String toString() {
        return "StoreId{" +
                "darlehen='" + darlehen + '\'' +
                ", fieldname='" + fieldname + '\'' +
                ", validFrom=" + validFrom +
                '}';
    }
}
