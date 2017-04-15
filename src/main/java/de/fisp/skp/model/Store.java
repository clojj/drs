package de.fisp.skp.model;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

@Entity
public class Store {

    @EmbeddedId
    private StoreId storeId;

    @Column
    private String value;

    protected Store() {
    }

    public StoreId getStoreId() {
        return storeId;
    }

    public void setStoreId(StoreId storeId) {
        this.storeId = storeId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Store{" +
                "storeId=" + storeId +
                ", value='" + value + '\'' +
                '}';
    }
}
