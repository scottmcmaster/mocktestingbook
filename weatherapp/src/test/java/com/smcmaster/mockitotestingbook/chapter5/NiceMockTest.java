package com.smcmaster.mockitotestingbook.chapter5;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.resources.WeatherResourceV3;
import com.smcmaster.weatherapp.services.WeatherService;

import static org.mockito.Mockito.*;

public class NiceMockTest {

  private WeatherService mock;
  private WeatherResourceV3 resource;
    
  @Before
  public void setUp() throws IOException {
    mock = mock(WeatherService.class);
    resource = new WeatherResourceV3(mock);
  }
  
  @Test
  public void testGetWeatherForCity_NotFound() throws Exception {
    // No expectations on the nice mock.
    
    // Replay and test.
    Weather result = resource.getWeatherForCity("cn", "beijing");
    
    // Verify. The city should be filled-in, everything else null.
    assertEquals("beijing", result.getCityName());
    assertNull(result.getTemperature());
    assertNull(result.getPressure());
    assertNull(result.getWind());
    assertNull(result.getHumidity());
  }
}
