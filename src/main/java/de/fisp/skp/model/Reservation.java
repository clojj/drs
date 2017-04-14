package de.fisp.skp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Reservation {

    @JsonProperty
    private String darlehen;

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
}
