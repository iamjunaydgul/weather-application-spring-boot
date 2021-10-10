package com.apache.camel.weather.rest.controller;

import com.apache.camel.weather.domain.WeatherInfo;
import com.apache.camel.weather.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@Controller
public class WeatherController {

    @Autowired
    WeatherService weatherService;

    /**
     * This method is called when application starts up
     * @param model
     * @return data and view to display the weather information to user
     */
    @GetMapping("/")
    public ModelAndView getWeatherData(Model model) {
        /*getting weather data from db*/
        List<WeatherInfo> weatherList = weatherService.findAll();

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        modelAndView.addObject("weatherList", weatherList);
        /*returning data and view to display the data*/
        return modelAndView;
    }
}
