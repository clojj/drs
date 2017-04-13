package de.fisp.skp.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Reservation {

    @JsonProperty
    private Integer darlehen;

    public Integer getDarlehen() {
        return darlehen;
    }

    public void setDarlehen(Integer darlehen) {
        this.darlehen = darlehen;
    }
}
