package com.example.weatherapi_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.awt.Desktop;
import java.net.URI;
import java.util.List;

@SpringBootApplication
@RestController
public class WeatherApiProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherApiProjectApplication.class, args);
    }

    @GetMapping("/weather")
    public String Weatherinlocation(@RequestParam(value = "city", defaultValue = "Wroclaw") String city) {
        String apiKey = "001fe81a747f9ce8372d952add925bca";

        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
        OpenWeatherMapResponse response = restTemplate.getForObject(apiUrl, OpenWeatherMapResponse.class);

        if (response != null) {
            List<WeatherDescription> weatherDescriptions = response.getWeather();
            MainWeatherData mainWeatherData = response.getMain();
            String weather = response.getWeather().get(0).getDescription();
            double temperature = response.getMain().getTemp();
            int humidity = response.getMain().getHumidity();
            double pressure = mainWeatherData.getPressure();
            double windSpeed = response.getWind().getSpeed();
            double rainfall = response.getRainfall().getOneHour();

            String output = "Weather in " + city + ": " + weather + "\n";
            output += "Temperature: " + temperature + "°C\n";
            output += "Humidity: " + humidity + "%";
            output += "Pressure: " + pressure + " hPa\n";
            output += "Wind Speed: " + windSpeed + " m/s\n";
            output += "Rainfall (1 hour): " + rainfall + " mm";

            System.out.println(output);

            openWebpage(apiUrl); // Otwórz adres w przeglądarce

            return output;
        } else {
            return "Error while retrieving weather information.";
        }
    }
    public static void openWebpage(String url) {
        try {
            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
