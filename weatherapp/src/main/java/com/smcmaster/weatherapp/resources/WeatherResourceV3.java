package com.smcmaster.weatherapp.resources;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.services.WeatherQuery;
import com.smcmaster.weatherapp.services.WeatherService;

@Path("/weatherv3")
public class WeatherResourceV3 {

  private final WeatherService weatherService;
  private WeatherResourceUtil util = new WeatherResourceUtil();

  @Inject
  public WeatherResourceV3(WeatherService weatherService) {
    this.weatherService = weatherService;
  }
  
  @GET
  @Produces("application/json")
  @Path("/{country}")
  public ArrayList<Weather> getWeatherForCities(
      @PathParam("country") String country,
      @QueryParam("city") final List<String> cities)
          throws Exception {
    // Should be at least one city.
    if (cities.size() < 1) {
      throw new BadRequestException("Must be at least one city");
    }
    
    ArrayList<Weather> result = new ArrayList<>();
    for (String city : cities) {
      String weatherData = weatherService.getWeatherForCity(country, city);
      if (weatherData == null) {
        // Skip cities that don't exist.
        continue;
      }
      
      result.add(util.createWeatherFromJSON(weatherData));
    }
    
    return result;
  }
  
  @GET
  @Produces("application/json")
  @Path("/{country}/{city}")
  public Weather getWeatherForCity(@PathParam("country") String country,
      @PathParam("city") String city) throws Exception {
    if (country == null || country.length() == 0) {
      throw new IllegalArgumentException("country required");
    }
    if (city == null || city.length() == 0) {
      throw new IllegalArgumentException("city required");
    }
    
    String result = weatherService.getWeatherForCity(country, city);
    if (result == null) {
      Weather weather = new Weather();
      weather.setCityName(city);
      return weather;
    }
    
    return util.createWeatherFromJSON(result);
  }
  
  @GET
  @Produces("text/plain")
  @Path("/health")
  public String checkHealth() {
    try {
      weatherService.ping();
    } catch (Exception ex) {
      return "DOWN";
    }
    return "OK";
  }
  
  public Weather getWeatherForCityUsingQuery(String country,
      String city) throws Exception {
    String result = weatherService.getWeatherForCityQuery(
        new WeatherQuery(country, city));
    if (result == null) {
      Weather weather = new Weather();
      weather.setCityName(city);
      return weather;
    }
    
    return util.createWeatherFromJSON(result);
  }
}
