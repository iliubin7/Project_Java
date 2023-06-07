package com.example.weatherapi_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
            String weather = response.getWeather().get(0).getDescription();
            double temperature = response.getMain().getTemp();
            int humidity = response.getMain().getHumidity();

            String output = "Weather in " + city + ": " + weather + "\n";
            output += "Temperature: " + temperature + "°C\n";
            output += "Humidity: " + humidity + "%";

            System.out.println(output);
            return output;
        } else {
            return "Error while retrieving weather information.";
        }
    }
}
