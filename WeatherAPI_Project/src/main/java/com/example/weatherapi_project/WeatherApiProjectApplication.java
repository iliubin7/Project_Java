package com.example.weatherapi_project;

import com.sun.tools.javac.Main;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

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
            output += "Temperature: " + Math.round(temperature) + "°C\n";
            output += "Humidity: " + humidity + "%\n";
            output += "Pressure: " + pressure + " hPa\n";
            output += "Wind Speed: " + windSpeed + " m/s\n";
            output += "Rainfall (1 hour): " + rainfall + " mm";
            System.out.println(output);
            openWebpage(weather,temperature, humidity, pressure, windSpeed, rainfall);
            return output;

        } else {
            return "Error while retrieving weather information.";
        }
    }
    public void openWebpage(String weather,double temperature, int humidity, double pressure, double windSpeed, double rainfall ) {
        try {
            String htmlContent = generateHtmlContent(weather, temperature, humidity, pressure, windSpeed, rainfall);
            String tempFileName = "pogoda";

            Path tempFilePath = Files.createTempFile(tempFileName, ".html");
            List<String> lines = new ArrayList<>();
            lines.add(htmlContent);
            Files.write(tempFilePath, lines, StandardOpenOption.WRITE);

            String os = System.getProperty("os.name").toLowerCase();

            if (os.contains("win")) {
                Runtime.getRuntime().exec("cmd /c start " + tempFilePath.toAbsolutePath());
            } else if (os.contains("mac")) {
                Runtime.getRuntime().exec("open " + tempFilePath.toAbsolutePath());
            } else if (os.contains("nix") || os.contains("nux")) {
                Runtime.getRuntime().exec("xdg-open " + tempFilePath.toAbsolutePath());
            } else {
                System.out.println("Cannot open the webpage on this operating system.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String generateHtmlContent(String weather,double temperature, int humidity, double pressure, double windSpeed, double rainfall ) {
        StringBuilder sb = new StringBuilder();
        //String s = "res/clearsky.jpg";
        sb.append("<html><head><title>Pogoda we Wrocławiu!</title></head><style>    \n" +
                "body {    \n" +
                "  background-image: url(\"res/clearsky.jpg\");  \n" +
                "  }  \n" +
                "</style><body>");
        sb.append("<h1 style='text-align:center;'>Pogoda we Wrocławiu!</h1>");

        sb.append("<h2>Aktualne warunki pogodowe:</h2>");
        sb.append("<p>Temperatura: " + Math.round(temperature) + " °C</p>");
        sb.append("<p>Wilgotność: " + humidity + "%</p>");
        sb.append("<p>Ciśnienie: " + pressure + " hPa</p>");
        sb.append("<p>Prędkość wiatru: " + windSpeed + " m/s</p>");
        sb.append("<p>Opady deszczu: " + rainfall + " mm</p>");
        sb.append("<p>Opis pogody: " + weather + "</p>");
        sb.append("</body></html>");
        return sb.toString();
    }

}
