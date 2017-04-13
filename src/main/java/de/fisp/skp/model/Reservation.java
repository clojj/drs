package de.fisp.skp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Reservation {

    @JsonProperty
    private Integer darlehen;

    @JsonProperty
    private String name;

    public Integer getDarlehen() {
        return darlehen;
    }

    public void setDarlehen(Integer darlehen) {
        this.darlehen = darlehen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
