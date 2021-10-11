package com.apache.camel.weather.route.builder;

import com.apache.camel.weather.domain.WeatherInfo;
import com.apache.camel.weather.service.WeatherService;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component
public class Weather extends RouteBuilder {

    @Autowired
    WeatherInfo weatherInfo;
    @Autowired
    WeatherService weatherService;

    @Override
    public void configure() throws Exception {
        //API-KEY: OpenWeatherMap -> 886705b4c1182eb1c69f28eb8c520e20
        //GEO-LOCATION-ACCESS-KEY -> 1a96db350dfdcc61c5336be3dfa05d6d
        /**
         * get weather data from api and after processing store in database for furthur use
         */
        //use &period=7 if want to get 7 days weather information of current location
        from("weather:foo?appid=886705b4c1182eb1c69f28eb8c520e20&geolocationAccessKey=1a96db350dfdcc61c5336be3dfa05d6d&geolocationRequestHostIP=59.103.223.179")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        List<WeatherInfo> weatherInfoList = new ArrayList<>();
                        JSONObject jsonObject = null;
                        String respBodyContent = exchange.getIn().getBody(String.class);
                        JSONObject mainJSONObject = new JSONObject(respBodyContent);
                        /*country*/
                        jsonObject = (JSONObject) mainJSONObject.get("sys");
                        weatherInfo.setCountryName((String) jsonObject.get("country"));
                        /*city*/
                        weatherInfo.setCityName((String) mainJSONObject.get("name"));
                        /*cityId*/
                        weatherInfo.setCityId((Integer) mainJSONObject.get("id"));
                        /*temperature*/
                        jsonObject = (JSONObject) mainJSONObject.get("main");
                        weatherInfo.setCityMinTemperature((BigDecimal) jsonObject.get("temp_min"));
                        weatherInfo.setCityMaxTemperature((BigDecimal) jsonObject.get("temp_max"));
                        /*weather description*/
                        JSONArray weatherDetails = (JSONArray) mainJSONObject.get("weather");
                        jsonObject = (JSONObject) weatherDetails.get(0);
                        weatherInfo.setCityWeatherDesc((String) jsonObject.get("description"));
                        /*last updated on*/
                        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
                        LocalDateTime now = LocalDateTime.now();
                        weatherInfo.setLastUpdatedAt(dtf.format(now));
                        weatherInfoList.add(weatherInfo);
                        weatherService.saveAll(weatherInfoList);
                    }
                })
                /*.log("${body}")*/
                .to("activemq:queue:weather");
    }
}
