package com.example.weatherapi_project;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherMapResponse {

    @JsonProperty("weather")
    private List<WeatherDescription> weather;

    @JsonProperty("main")
    private MainWeatherData main;

    @JsonProperty("wind")
    private WindData wind;

    @JsonProperty("rain")
    private RainfallData rainfall;

    public List<WeatherDescription> getWeather() {return weather;}

    public void setWeather(List<WeatherDescription> weather) {this.weather = weather;}

    public MainWeatherData getMain() {return main;}

    public void setMain(MainWeatherData main) {this.main = main;}

    public WindData getWind() {return wind;}

    public void setWind(WindData wind) {
        this.wind = wind;
    }

    public RainfallData getRainfall() {return rainfall;}

    public void setRainfall(RainfallData rainfall) {this.rainfall = rainfall;}
}