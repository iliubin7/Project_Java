package com.example.weatherapi_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
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
            double temperature = response.getMain().getTemp() - 273.15;
            temperature = Math.round(temperature * 100.0)/100.0;
            int humidity = response.getMain().getHumidity();
            double pressure = mainWeatherData.getPressure();
            double windSpeed = response.getWind().getSpeed();
            RainfallData rainfallData = response.getRainfall();
            double rainfall = (rainfallData != null) ? rainfallData.getOneHour() : 0.0;


            String output = "Weather in " + city + ": " + weather + "\n";
            output += "Temperature: " + temperature + "°C\n";
            output += "Humidity: " + humidity + "%\n";
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
            if (GraphicsEnvironment.isHeadless()) {
                System.out.println("Running in headless mode. Unable to open webpage.");
                return;
            }

            String htmlContent = "<html><head><title>Pogoda we Wrocławiu!</title></head><body><h1 style='text-align:center;'>Pogoda we Wrocławiu!</h1><p>API pogodowe: " + url + "</p></body></html>";
            String tempFileName = "temp.html";
            BufferedWriter writer = new BufferedWriter(new FileWriter(tempFileName));
            writer.write(htmlContent);
            writer.close();

            Desktop desktop = Desktop.getDesktop();
            desktop.browse(new URI("file://" + tempFileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
