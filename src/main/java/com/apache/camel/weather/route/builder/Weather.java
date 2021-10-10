package com.apache.camel.weather.route.builder;

import com.apache.camel.weather.domain.WeatherInfo;
import com.apache.camel.weather.service.WeatherService;
import org.apache.camel.*;
import org.apache.camel.builder.RouteBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Iterator;

@Component
public class Weather extends RouteBuilder {

    @Autowired
    WeatherInfo weatherInfo;
    @Autowired
    WeatherService weatherService;

    @Override
    public void configure() throws Exception {
       //API-KEY: OpenWeatherMap -> 886705b4c1182eb1c69f28eb8c520e20
        /**
         * get weather data from api and after processing store in database for furthur use
         */
       from("weather:foo?location=Madrid,Spain&period=7days&appid=886705b4c1182eb1c69f28eb8c520e20")
               .process(new Processor() {
                   @Override
                   public void process(Exchange exchange) throws Exception {
                       String respBodyContent = exchange.getIn().getBody(String.class);
                       JSONObject jsonObject = new JSONObject(respBodyContent);
                       Iterator<String> keys = jsonObject.keys();
                       while(keys.hasNext()) {
                           String key = keys.next();
                           if(key.equals("city")){
                               JSONObject jsonCityObject= (JSONObject) jsonObject.get(key);
                               weatherInfo.setCountryName((String) jsonCityObject.get("country"));
                               weatherInfo.setCityName((String) jsonCityObject.get("name"));
                               weatherInfo.setCityId((Integer) jsonCityObject.get("id"));
                           }
                           if(key.equals("list")){
                               JSONArray jsonArray= (JSONArray) jsonObject.get("list");
                               JSONObject jsonWeatherDetail= (JSONObject) jsonArray.get(0);
                               JSONObject jsonTemperatureObject= (JSONObject) jsonWeatherDetail.get("temp");
                               weatherInfo.setCityMinTemperature((BigDecimal) jsonTemperatureObject.get("min"));
                               weatherInfo.setCityMaxTemperature((BigDecimal) jsonTemperatureObject.get("max"));
                               JSONArray jsonWeatherArray= (JSONArray) jsonWeatherDetail.get("weather");
                               JSONObject jsonWeatherDesc= (JSONObject) jsonWeatherArray.get(0);
                               weatherInfo.setCityWeatherDesc((String) jsonWeatherDesc.get("description"));
                               weatherInfo.setLastUpdatedAt(LocalDate.now());
                               weatherService.save(weatherInfo);
                           }
                       }
                   }
               })
               /*.log("${body}")*/
               .to("activemq:queue:weather");
    }
}
