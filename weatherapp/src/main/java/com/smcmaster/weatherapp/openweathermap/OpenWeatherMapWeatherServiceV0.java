package com.smcmaster.weatherapp.openweathermap;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import javax.ws.rs.NotFoundException;

import org.apache.commons.io.IOUtils;

import com.google.common.annotations.VisibleForTesting;
import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.resources.WeatherResourceUtil;

public class OpenWeatherMapWeatherServiceV0 {

  private static final String BASE_URL =
      "http://api.openweathermap.org/data/2.5/weather?";
  
  private WeatherResourceUtil util;

  public OpenWeatherMapWeatherServiceV0() {
    util = new WeatherResourceUtil();
  }
  
  public Weather getWeatherForCity(String country, String city)
      throws Exception {
    String json = retrieveWeatherData(country, city);
    return parseWeatherData(json);
  }

  private Weather parseWeatherData(String json)
      throws Exception {
    // Translate JSON into a Weather plain-old-data instance.
    // ...
    return util.createWeatherFromJSON(json);
  }

  @VisibleForTesting
  public String retrieveWeatherData(String country, String city)
      throws Exception {
    // Get the weather data from the web service, return JSON.
    // ...
    String urlStr = BASE_URL + "q=" + URLEncoder.encode(city, "utf-8") + ","
        + URLEncoder.encode(country, "utf-8")
        + "&units=metric&appid="
        + OpenWeatherMap.getApiKey();
    URL url = new URL(urlStr);
    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    int statusCode = conn.getResponseCode();
    if (statusCode != 200) {
      throw new NotFoundException();
    }
    return IOUtils.toString((InputStream) conn.getContent(),
        Charset.defaultCharset());
  }
}
