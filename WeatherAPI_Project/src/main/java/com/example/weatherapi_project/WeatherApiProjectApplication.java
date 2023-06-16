package com.example.weatherapi_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

@SpringBootApplication
@RestController
public class WeatherApiProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherApiProjectApplication.class, args);
    }

    @GetMapping("/weather")
    public String weatherInLocation(@RequestParam(value = "city", defaultValue = "Wrocław") String city) {
        String apiKey = "001fe81a747f9ce8372d952add925bca";

        RestTemplate restTemplate = new RestTemplate();
        String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
        OpenWeatherMapResponse response = restTemplate.getForObject(apiUrl, OpenWeatherMapResponse.class);

        if (response != null && response.getWeather() != null && !response.getWeather().isEmpty()) {
            List<WeatherDescription> weatherDescriptions = response.getWeather();
            MainWeatherData mainWeatherData = response.getMain();
            String weather = response.getWeather().get(0).getDescription();
            double temperature = response.getMain().getTemp() - 273.15;
            temperature = Math.round(temperature * 100.0) / 100.0;
            int humidity = response.getMain().getHumidity();
            double pressure = mainWeatherData.getPressure();
            double windSpeed = response.getWind().getSpeed();
            RainfallData rainfallData = response.getRainfall();
            double rainfall = (rainfallData != null) ? rainfallData.getOneHour() : 0.0;

            return generateHtmlContent(city, weather, temperature, humidity, pressure, windSpeed, rainfall);
        } else {
            return "Error while retrieving weather information.";
        }
    }

    public String generateHtmlContent(String city, String weather, double temperature, int humidity, double pressure, double windSpeed, double rainfall) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Weather in " + city + "!</title>");
        sb.append("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css\">");
        sb.append("<style>    \n");
        sb.append("body {    \n");
        sb.append("  background-image: url(\"" + getWeatherBackground(weather) + "\");  \n");
        sb.append("  background-repeat: no-repeat;  \n");
        sb.append("  background-size: cover;  \n");
        sb.append("}  \n");
        sb.append("</style></head><body>");

        sb.append("<h1 style='text-align:center;'>Weather in " + city + "!</h1>");

        sb.append("<form method='GET' action='/weather'>");
        sb.append("<label for='cityInput'>Enter city name:</label>");
        sb.append("<input type='text' id='cityInput' name='city' value='" + city + "'>");
        sb.append("<input type='submit' value='Get Weather'>");
        sb.append("</form>");

        sb.append("<h2>Current weather conditions:</h2>");

        // Dodanie ikony pogodowej na podstawie warunków pogodowych
        sb.append("<p><i class='" + getWeatherIconClass(weather) + "'></i> Weather: " + weather + "</p>");

        sb.append("<p>Temperature: " + Math.round(temperature) + " °C</p>");
        sb.append("<p>Humidity: " + humidity + "%</p>");
        sb.append("<p>Pressure: " + pressure + " hPa</p>");
        sb.append("<p>Wind Speed: " + windSpeed + " m/s</p>");
        sb.append("<p>Rainfall (1 hour): " + rainfall + " mm</p>");
        sb.append("</body></html>");
        return sb.toString();
    }

    public String getWeatherIconClass(String weather) {
        // Mapowanie warunków pogodowych na odpowiednie klasy ikon z biblioteki Font Awesome
        switch (weather) {
            case "clear sky":
                return "fas fa-sun";

            case "few clouds":
            case "scattered clouds":
            case "broken clouds":
            case "overcast clouds":
                return "fas fa-cloud";

            case "moderate rain":
            case "heavy intensity rain":
            case "shower rain":
            case "rain":
            case "light rain":
            case "thunderstorm":
                return "fas fa-cloud-showers-heavy";

            case "snow":
                return "fas fa-snowflake";

            case "fog":
            case "mist":
            case "haze":
            case "smoke":
            default:
                return "fas fa-question";
        }
    }

    public String getWeatherBackground(String weather) {
        // Mapowanie warunków pogodowych na odpowiednie zdjecia tla
        switch (weather) {
            case "clear sky":
                return "https://images.pexels.com/photos/281260/pexels-photo-281260.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
            case "few clouds":
                return "https://images.pexels.com/photos/16226602/pexels-photo-16226602/free-photo-of-clouds-on-blue-sky-over-plains-with-forest.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
            case "scattered clouds":
                return "https://images.pexels.com/photos/8579644/pexels-photo-8579644.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
            case "broken clouds":
                return "https://images.pexels.com/photos/2886268/pexels-photo-2886268.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
            case "overcast clouds":
                return "https://images.pexels.com/photos/16222439/pexels-photo-16222439/free-photo-of-harvester-on-field-under-clouds.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
            case "moderate rain":
                return "https://images.pexels.com/photos/16118799/pexels-photo-16118799/free-photo-of-chodzenie-chodnik-deszcz-mokry.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
            case "heavy intensity rain":
                return "https://images.pexels.com/photos/1530423/pexels-photo-1530423.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
            case "shower rain":
                return "https://images.pexels.com/photos/1915182/pexels-photo-1915182.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
            case "rain":
                return "https://images.pexels.com/photos/1463530/pexels-photo-1463530.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
            case "light rain":
                return "https://images.pexels.com/photos/50677/rain-after-the-rain-a-drop-of-drop-of-rain-50677.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
            case "fog":
                return "https://images.pexels.com/photos/167699/pexels-photo-167699.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
            case "thunderstorm":
                return "https://images.pexels.com/photos/1162251/pexels-photo-1162251.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
            case "mist":
                return "https://images.pexels.com/photos/167699/pexels-photo-167699.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
            case "haze":
                return "https://images.pexels.com/photos/2529973/pexels-photo-2529973.jpeg?auto=compress&cs=tinysrgb&w=1600";
            case "snow":
                return "https://images.pexels.com/photos/3334585/pexels-photo-3334585.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1";
            case "smoke":
                return "";
            default:
                return "https://images.pexels.com/photos/1431822/pexels-photo-1431822.jpeg?auto=compress&cs=tinysrgb&w=1260&h=750&dpr=1"; // Domyślne tlo dla nieznanych warunków
        }
    }
}


