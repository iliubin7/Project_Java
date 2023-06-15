package com.example.weatherapi_project;

import jakarta.annotation.Resource;
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

            String output = "Weather in " + city + ": " + weather + "\n";
            output += "Temperature: " + Math.round(temperature) + "°C\n";
            output += "Humidity: " + humidity + "%\n";
            output += "Pressure: " + pressure + " hPa\n";
            output += "Wind Speed: " + windSpeed + " m/s\n";
            output += "Rainfall (1 hour): " + rainfall + " mm";
            System.out.println(output);
            this.openWebpage(city, weather, temperature, humidity, pressure, windSpeed, rainfall);
            return output;

        } else {
            return "Error while retrieving weather information.";
        }
    }

    public void openWebpage(String city, String weather, double temperature, int humidity, double pressure, double windSpeed, double rainfall) {
        try {
            String htmlContent = generateHtmlContent(city, weather, temperature, humidity, pressure, windSpeed, rainfall);
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



    public String generateHtmlContent(String city, String weather, double temperature, int humidity, double pressure, double windSpeed, double rainfall) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Weather in " + city + "!</title>");
        sb.append("<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css\">");
        sb.append("<style>    \n");
        sb.append("body {    \n");
        sb.append("  background-image: url(\"" + getWeatherBackground(weather) +  "\");  \n");
        sb.append("  }  \n");
        sb.append("</style></head><body>");

        sb.append("<h1 style='text-align:center;'>Weather in " + city + "!</h1>");

        sb.append("<form method=\"POST\" action=\"#\" th:action=\"@{/weather}\">");  // # sluzy do sygnalizowania, ze po wykonaniu akcji zostajemy na tej stronie
        //sb.append("<action=\"#\" th:action=\"@{/weather}\" method=\"post\">");
        sb.append("<label for=\"cityInput\">Enter city name:</label>");
        sb.append("<input type=\"text\" id=\"cityInput\" name=\"city\">");
        sb.append("<input type=\"submit\" value=\"Get Weather\">");
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
            case "Clouds":
                return "fas fa-cloud";
            case "Rain":
                return "fas fa-cloud-showers-heavy";
            case "Snow":
                return "fas fa-snowflake";
            default:
                return "fas fa-question"; // Domyślna ikona dla nieznanych warunków
        }
    }

    public String getWeatherBackground(String weather)
    {
        // Mapowanie warunków pogodowych na odpowiednie zdjecia tla
        switch (weather) {
            case "clear sky":
                return "https://www.seekpng.com/png/full/2-22588_cloud-background-free-download-sky-background-transparent-png.png";
            case "Clouds":
                return "https://s.yimg.com/ny/api/res/1.2/dZnLSTPciIYSK2Mvl9yYZQ--/YXBwaWQ9aGlnaGxhbmRlcjt3PTcwNTtjZj13ZWJw/https://media.zenfs.com/en/hoodline_545/aeded8b6efde8a4e2b2fa15edf1c2a1b";
            case "Rain":
                return "https://imgsrv2.voi.id/8Mb6U1RIl6ROvmryQxHwZUHfQF01lH82kS4rE0T0Mis/auto/1200/675/sm/1/bG9jYWw6Ly8vcHVibGlzaGVycy8zNDA1Mi8yMDIxMDIxOTAxMDEtbWFpbi5jcm9wcGVkXzE2MTM2NzEyOTguanBn.jpg";
            case "Snow":
                return "https://www.gannett-cdn.com/-mm-/454a0d0a2013412d158f08ab812536087d93bea8/c=0-193-1988-1316/local/-/media/2015/12/04/CNYGroup/Binghamton/635848220175976573-ThinkstockPhotos-462866769.jpg?width=2560";
            default:
                return "https://www.google.com/url?sa=i&url=https%3A%2F%2Fnewsonair.gov.in%2FNews%3Ftitle%3DWeather-forecast-for-Monday%26id%3D441932&psig=AOvVaw2Nff4Yln9CJbnEbqCP2YxS&ust=1686950156818000&source=images&cd=vfe&ved=0CBEQjRxqFwoTCJiGzZiZxv8CFQAAAAAdAAAAABAE"; // Domyślne tlo dla nieznanych warunków
        }

    }



}
