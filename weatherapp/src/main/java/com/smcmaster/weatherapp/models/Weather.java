package com.smcmaster.weatherapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;


public class Weather {
  @JsonProperty
  String cityName;
  @JsonProperty
  String temperature;
  @JsonProperty
  String wind;
  @JsonProperty
  String pressure;
  @JsonProperty
  String humidity;

  public Weather() {
  }

  public Weather(String cityName) {
	  this.cityName = cityName;
  }

  public Weather(String cityName, String temperature, String wind, String visibility,
      String pressure, String humidity) {
    this.cityName = cityName;
    this.temperature = temperature;
    this.wind = wind;
    this.pressure = pressure;
    this.humidity = humidity;
  }

  public String getCityName() {
    return cityName;
  }

  public void setCityName(String cityName) {
    this.cityName = cityName;
  }

  public String getTemperature() {
    return temperature;
  }

  public void setTemperature(String temperature) {
    this.temperature = temperature;
  }

  public String getWind() {
    return wind;
  }

  public void setWind(String wind) {
    this.wind = wind;
  }

  public String getPressure() {
    return pressure;
  }

  public void setPressure(String pressure) {
    this.pressure = pressure;
  }

  public String getHumidity() {
    return humidity;
  }

  public void setHumidity(String humidity) {
    this.humidity = humidity;
  }
}
