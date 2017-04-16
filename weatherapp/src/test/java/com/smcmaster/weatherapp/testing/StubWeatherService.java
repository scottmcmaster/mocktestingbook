package com.smcmaster.weatherapp.testing;

import org.apache.commons.io.IOUtils;

import com.smcmaster.weatherapp.services.WeatherQuery;
import com.smcmaster.weatherapp.services.WeatherService;

public class StubWeatherService implements WeatherService {
  @Override
  public String getWeatherForCity(String country, String city)
      throws Exception {
    if ("beijing".equalsIgnoreCase(city)) {
      String json = IOUtils.toString(
          getClass().getResourceAsStream("/beijing_owm.json"), "UTF-8");
      return json;      
    }
    return null;
  }

  @Override
  public String getWeatherForCityQuery(WeatherQuery query) throws Exception {
    return getWeatherForCity(query.getCountry(), query.getCity());
  }

  @Override
  public void ping() {
    // Nothing to do.
  }
}
