package com.example.weatherapi_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class WeatherApiProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeatherApiProjectApplication.class, args);
    }

    @GetMapping("/weather")
    public String Weatherinlocation(@RequestParam(value = "myName", defaultValue = "Wroclaw") String name) {
        return String.format("Weather in %s is: !", name);
    }

}
