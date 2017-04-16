package com.smcmaster.weatherapp;

import javax.inject.Singleton;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.smcmaster.weatherapp.openweathermap.OpenWeatherMapWeatherService;
import com.smcmaster.weatherapp.services.WeatherService;

public class WeatherApplicationModule implements Module {
  @Override
  public void configure(Binder binder) {
    binder
      .bind(WeatherService.class)
      .to(OpenWeatherMapWeatherService.class)
      .in(Singleton.class);
  }
}
