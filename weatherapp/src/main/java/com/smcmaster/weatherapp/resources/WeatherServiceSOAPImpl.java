package com.smcmaster.weatherapp.resources;

import java.util.HashMap;
import java.util.Map;

import javax.jws.WebService;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Metered;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.openweathermap.OpenWeatherMapWeatherService;
import com.smcmaster.weatherapp.services.WeatherService;
import com.smcmaster.weatherapp.services.WeatherServiceSOAP;

@WebService(name="WeatherServiceSOAP",
  targetNamespace="http://com.smcmaster.weatherapp/WeatherServiceSOAP")
public class WeatherServiceSOAPImpl implements WeatherServiceSOAP {

  private final WeatherService weatherService;
  private ObjectMapper mapper = new ObjectMapper();

  public WeatherServiceSOAPImpl() {
    this.weatherService = new OpenWeatherMapWeatherService();;
  }

  @SuppressWarnings("unchecked")
@Override
  @ExceptionMetered
  @Metered
  public Weather getWeather(String country, String city) throws Exception {
    Weather weather = new Weather();
    weather.setCityName(city);

    String result = weatherService.getWeatherForCity(country, city);
    
    if (result == null) {
      return weather;
    }
    
    Map<String, Object> map = new HashMap<>();
    map = mapper.readValue(result, new TypeReference<Map<String, Object>>(){});

    if (map.containsKey("main")) {
      Map<String, Object> mainData = (Map<String, Object>) map.get("main");
      if (mainData.containsKey("temp")) {
        weather.setTemperature(mainData.get("temp").toString());
      }
      if (mainData.containsKey("pressure")) {
        weather.setPressure(mainData.get("pressure").toString());
      }
      if (mainData.containsKey("humidity")) {
        weather.setHumidity(mainData.get("humidity").toString());
      }
    }
    
    if (map.containsKey("wind")) {
      Map<String, Object> windData = (Map<String, Object>) map.get("wind");
      if (windData.containsKey("speed")) {
        String windStr = windData.get("speed").toString();
        if (windData.containsKey("deg")) {
          windStr += "@";
          windStr += windData.get("deg").toString();
        }
        weather.setWind(windStr);
      }
    }
    return weather;
  }

}
