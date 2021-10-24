package com.smcmaster.mockitotestingbook.chapter9;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.resources.WeatherResourceV3;
import com.smcmaster.weatherapp.services.WeatherService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NoVerifyTest {

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
  public void testGetWeatherForCity_DontVerify() throws Exception {
    // Set up expectations.
    when(mock.getWeatherForCity("cn", "Beijing"))
      .thenReturn(json);
    
    // Replay and test.
    Weather result = resource.getWeatherForCity("cn", "Beijing");
    
    validateWeather(result);
    // DON'T Verify.
  }
  
  @Test
  @Ignore
  public void testGetWeatherForCity_UnusedExpectation()
      throws Exception {
    // Set up expectations.
    when(mock.getWeatherForCity("cn", "beijing"))
        .thenReturn(json);
    
    // Oops, we forgot to actually DO anything to the
    // class under test.
    
    // Replay and test.
    
    // DON'T Verify.
  }

  
  @Test
  public void testGetWeatherForCity_ForgotVerify()
      throws Exception {
    // Set up expectations.
    when(mock.getWeatherForCity(anyString(), anyString()))
      .thenReturn(json);
    
    // Replay and test.
    List<String> cities = new ArrayList<>();
    cities.add("Beijing");
    cities.add("Shanghai");
    resource.getWeatherForCities("cn", cities);
    
    // Only verify ONE of two expectations.
    verify(mock).getWeatherForCity("cn", "Beijing");
  }

  private void validateWeather(Weather result) {
    assertEquals("Beijing", result.getCityName());
    assertEquals("10", result.getTemperature());
    assertEquals("1000", result.getPressure());
    assertEquals("2@300", result.getWind());
    assertEquals("27", result.getHumidity());
  }

}
