package com.smcmaster.weatherapp.services;

public class WeatherQuery {
  private final String city;
  private final String country;
    
  public WeatherQuery(String country, String city) {
    this.country = country;
    this.city = city;
  }
  
  public String getCity() {
    return city;
  }
  public String getCountry() {
    return country;
  }
}
