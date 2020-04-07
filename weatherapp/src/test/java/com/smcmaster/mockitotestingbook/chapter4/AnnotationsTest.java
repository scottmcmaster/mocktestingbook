package com.smcmaster.mockitotestingbook.chapter4;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.resources.WeatherResourceV2;
import com.smcmaster.weatherapp.services.WeatherService;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AnnotationsTest {
  
  @Mock
  private WeatherService mock;  
  
  private String json;
  private WeatherResourceV2 resource;
  
  @Before
  public void setUp() throws IOException {
    json = IOUtils.toString(
        getClass().getResourceAsStream("/beijing_owm.json"),
        "UTF-8");
    resource = new WeatherResourceV2(mock);
  }
  
  @Test
  public void testGetWeatherForCity_Valid() throws Exception {
    // Set up expectations.
    when(mock.getWeatherForCity("cn", "beijing"))
      .thenReturn(json);
    
    // Replay and test.
    Weather result = resource.getWeatherForCity("cn", "beijing");
    
    // Verify.
    validateWeather(result);
  }

  private void validateWeather(Weather result) {
    assertEquals(result.getCityName(), "beijing");
    assertEquals("10", result.getTemperature());
    assertEquals("1000", result.getPressure());
    assertEquals("2@300", result.getWind());
    assertEquals("27", result.getHumidity());
  }
}
