package com.apache.camel.weather.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Component
public class WeatherInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /*country name*/
    private String countryName;
    private Integer cityId;
    /*city name*/
    private String cityName;
    /*city weather description*/
    private String cityWeatherDesc;
    /*city minimum temperature*/
    private BigDecimal cityMinTemperature;
    /*city maximum temperature*/
    private BigDecimal cityMaxTemperature;
    private String lastUpdatedAt;
}
