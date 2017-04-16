package de.fisp.skp.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Datom {

    @EmbeddedId
    private DatomId datomId;

    @Column
    private String value;

    public DatomId getDatomId() {
        return datomId;
    }

    public void setDatomId(DatomId datomId) {
        this.datomId = datomId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Datom{" +
                "datomId=" + datomId +
                ", value='" + value + '\'' +
                '}';
    }
}
