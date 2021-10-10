package com.apache.camel.weather.service;

import com.apache.camel.weather.domain.WeatherInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherService extends JpaRepository<WeatherInfo, Long> {
}
