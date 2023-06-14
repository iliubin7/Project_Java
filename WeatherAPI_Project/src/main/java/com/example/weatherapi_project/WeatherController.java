package com.example.weatherapi_project;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class WeatherController
{
    @GetMapping("/greeting")
    public String greetingForm(Model model) {
        model.addAttribute("greeting", new WeatherModel());
        return "greeting";
    }

    @PostMapping("/greeting")
    public String greetingSubmit(@ModelAttribute WeatherModel greeting, Model model) {
        model.addAttribute("greeting", greeting);
        return "result";
    }
}
