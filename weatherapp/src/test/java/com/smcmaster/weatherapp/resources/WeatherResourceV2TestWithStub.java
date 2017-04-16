package com.smcmaster.weatherapp.resources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.testing.StubWeatherService;

public class WeatherResourceV2TestWithStub {

  private StubWeatherService weatherService;
  private WeatherResourceV2 resource;
  
  @Before
  public void setUp() {
    weatherService = new StubWeatherService();
    resource = new WeatherResourceV2(weatherService);
  }
  
  @Test
  public void testGetWeatherForCity_Valid() throws Exception {    
    Weather result = resource.getWeatherForCity("cn", "beijing");
    
    assertEquals(result.getCityName(), "beijing");
    assertEquals("10", result.getTemperature());
    assertEquals("1000", result.getPressure());
    assertEquals("2@300", result.getWind());
    assertEquals("27", result.getHumidity());
  }
  
  @Test
  public void testGetWeatherForCity_NotFound() throws Exception {
    Weather result = resource.getWeatherForCity("zz", "doesnotexist");
    
    assertEquals(result.getCityName(), "doesnotexist");
    assertNull(result.getTemperature());
    assertNull(result.getPressure());
    assertNull(result.getWind());
    assertNull(result.getHumidity());
  }
}
