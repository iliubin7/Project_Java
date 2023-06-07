package com.example.weatherapi_project;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MainWeatherData {

    @JsonProperty("temp")
    private double temp;

    @JsonProperty("humidity")
    private int humidity;

    @JsonProperty("pressure")
    private double pressure;

    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public double getPressure() {return pressure;}

    public void setPressure(double pressure) {this.pressure = pressure;}

}