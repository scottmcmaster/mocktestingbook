package com.smcmaster.weatherapp.services;

public interface WeatherService {
  /**
   * Gets the weather JSON data for the given country/city.
   * The consumer of this class will be responsible for the parsing.
   */
  String getWeatherForCity(String country, String city)
      throws Exception;
  
  /**
   * Gets the weather JSON data for the given country/city.
   * The consumer of this class will be responsible for the parsing.
   */
  String getWeatherForCityQuery(WeatherQuery query)
      throws Exception;
  
  /**
   * Pings the underlying service, presumably to see if it's alive.
   * Should throw an exception if not.
   */
  void ping();
}
