package de.fisp.skp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
public class Reservation {

    @Id
    @JsonProperty
    private String darlehen;

    @Column
    @JsonProperty
    private String name;

    public String getDarlehen() {
        return darlehen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "darlehen='" + darlehen + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
