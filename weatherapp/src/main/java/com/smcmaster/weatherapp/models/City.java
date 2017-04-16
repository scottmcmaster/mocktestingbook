package com.smcmaster.weatherapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class City {

  @JsonProperty
  String cityName;
  
  @JsonProperty
  String countryName;

  public City() {
    
  }
  
  public City(String cityName, String countryName) {
    this.cityName = cityName;
    this.countryName = countryName;
  }

}
