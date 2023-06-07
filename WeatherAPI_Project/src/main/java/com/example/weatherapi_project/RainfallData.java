package com.example.weatherapi_project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RainfallData {

    @JsonProperty("1h")
    private double oneHour;

    public double getOneHour() {
        return oneHour;
    }

    public void setOneHour(double oneHour) {
        this.oneHour = oneHour;
    }
}