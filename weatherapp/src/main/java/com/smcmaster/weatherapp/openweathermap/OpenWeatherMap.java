package com.smcmaster.weatherapp.openweathermap;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class OpenWeatherMap {

  private static String apiKey = "";

  static {
    String resourceName = "local.properties";
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    Properties props = new Properties();
    try (InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
      props.load(resourceStream);
    } catch (IOException e) {
      // We will fail later...
    }
    apiKey = props.getProperty("openweathermap.apikey");
  }
  
  public static String getApiKey() {
    return apiKey;
  }
}
