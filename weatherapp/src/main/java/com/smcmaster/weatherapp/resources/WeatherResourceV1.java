package com.smcmaster.weatherapp.resources;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.apache.commons.io.IOUtils;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.openweathermap.OpenWeatherMap;

@Path("/weatherv1")
public class WeatherResourceV1 {

  private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
  
  private WeatherResourceUtil util = new WeatherResourceUtil();
  
  @GET
  @Produces("application/json")
  @Path("/{country}/{city}")
  public Weather getWeatherForCity(
		  @PathParam("country") String country,
		  @PathParam("city") String city) throws Exception {
    if (city == null || city.length() == 0) {
      throw new BadRequestException("City is required");
    }
    if (country == null || country.length() == 0) {
      throw new BadRequestException("Country is required");
    }

    String urlStr = BASE_URL + "q="
    	+ URLEncoder.encode(city, "utf-8") + ","
        + URLEncoder.encode(country, "utf-8") + "&units=metric&appid="
        + OpenWeatherMap.getApiKey();
    URL url = new URL(urlStr);
    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
    int statusCode = conn.getResponseCode();
    if (statusCode != 200) {
      throw new NotFoundException();
    }

    String result = IOUtils.toString((InputStream) conn.getContent(), Charset.defaultCharset());
    return util.createWeatherFromJSON(result);
  }
}
