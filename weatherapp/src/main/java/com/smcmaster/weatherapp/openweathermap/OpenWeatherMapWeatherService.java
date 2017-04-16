package com.smcmaster.weatherapp.openweathermap;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import javax.ws.rs.NotFoundException;

import org.apache.commons.io.IOUtils;

import com.smcmaster.weatherapp.services.WeatherQuery;
import com.smcmaster.weatherapp.services.WeatherService;

public class OpenWeatherMapWeatherService implements WeatherService {

  private static final String BASE_URL =
      "http://api.openweathermap.org/data/2.5/weather?";

  @Override
  public String getWeatherForCity(String country, String city)
      throws Exception {
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

  @Override
  public String getWeatherForCityQuery(WeatherQuery query) throws Exception {
    return getWeatherForCity(query.getCountry(), query.getCity());
  }
  
  @Override
  public void ping() {
    // Nothing to do.
  }
}
