package com.smcmaster.mockitotestingbook.chapter11;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.MockitoJUnitRunner;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.resources.WeatherResourceV3;
import com.smcmaster.weatherapp.services.WeatherQuery;
import com.smcmaster.weatherapp.services.WeatherService;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CaptureTest {
  private String json;
  private WeatherService mock;
  private WeatherResourceV3 resource;
  
  @Before
  public void setUp() throws IOException {
    json = IOUtils.toString(
        getClass().getResourceAsStream("/beijing_owm.json"),
        "UTF-8");
    mock = mock(WeatherService.class); // returns a WeatherService
    resource = new WeatherResourceV3(mock);
  }
  
  @Test
  public void testGetWeatherForCity_Capture() throws Exception {
    // Set up expectations.
    ArgumentCaptor<String> countryCap = ArgumentCaptor.forClass(String.class);
    ArgumentCaptor<String> cityCap = ArgumentCaptor.forClass(String.class);
    when(mock.getWeatherForCity(countryCap.capture(), cityCap.capture()))
      .thenReturn(json);
    
    // Replay and test.
    Weather result = resource.getWeatherForCity("cn", "Beijing");
    
    // Verify.
    validateWeather(result);
    assertEquals("cn", countryCap.getValue());
    assertEquals("Beijing", cityCap.getValue());
  }

  @Test
  public void testGetWeatherForCities_CaptureMultiple()
      throws Exception {
    // Set up expectations.
    ArgumentCaptor<String> cityCap = ArgumentCaptor.forClass(String.class);
    when(mock.getWeatherForCity(
        eq("cn"),
        cityCap.capture()))
      .thenReturn(json);
    
    // Replay and test.
    List<String> cities = new ArrayList<>();
    cities.add("beijing");
    cities.add("shanghai");
    ArrayList<Weather> result =
        resource.getWeatherForCities("cn", cities);
    
    // Verify.
    assertEquals(2, result.size());
    List<String> capturedCities = cityCap.getAllValues();
    for (int i = 0; i < result.size(); i++) {
      String expectedCity = cities.get(i);
      assertTrue(capturedCities.get(i).contains(expectedCity));
    }
  }
  
  @Test
  public void testGetWeatherForCity_CaptureObject()
      throws Exception {
    // Set up expectations.
    ArgumentCaptor<WeatherQuery> queryCap = ArgumentCaptor.forClass(WeatherQuery.class);
    when(mock.getWeatherForCityQuery(queryCap.capture()))
      .thenReturn(json);
    
    // Replay and test.
    
    // Test the alternative method that uses the query
    // under the hood.
    Weather result = resource.getWeatherForCityUsingQuery(
        "cn", "Beijing");
    
    // Verify.
    assertEquals(result.getCityName(), "Beijing");
    assertEquals(queryCap.getValue().getCity(), "Beijing");
    assertEquals(queryCap.getValue().getCountry(), "cn");
  }

  private void validateWeather(Weather result) {
    assertEquals("Beijing", result.getCityName());
    assertEquals("10", result.getTemperature());
    assertEquals("1000", result.getPressure());
    assertEquals("2@300", result.getWind());
    assertEquals("27", result.getHumidity());
  }
}
