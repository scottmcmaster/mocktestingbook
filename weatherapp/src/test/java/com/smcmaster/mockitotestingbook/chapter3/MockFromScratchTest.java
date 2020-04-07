package com.smcmaster.mockitotestingbook.chapter3;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.smcmaster.mocktestingbook.chapter3.WeatherServiceMock;
import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.resources.WeatherResourceV2;

public class MockFromScratchTest {

  private String json;
  private WeatherServiceMock mock;
  private WeatherResourceV2 resource;
  
  @Before
  public void setUp() throws IOException {
    json = IOUtils.toString(
        getClass().getResourceAsStream("/beijing_owm.json"),
        "UTF-8");
    mock = new WeatherServiceMock();
    resource = new WeatherResourceV2(mock);
  }
  
  @Test
  public void testGetWeatherForCity_Valid() throws Exception {
    // Set up expectations.
    mock.getWeatherForCity("cn", "beijing");
    mock.getWeatherForCityThenReturn(json);
    
    // Replay and test.
    mock.replay();
    Weather result = resource.getWeatherForCity("cn", "beijing");
    
    // Verify.
    assertEquals(result.getCityName(), "beijing");
    assertEquals("10", result.getTemperature());
    assertEquals("1000", result.getPressure());
    assertEquals("2@300", result.getWind());
    assertEquals("27", result.getHumidity());
    mock.verify();
  }

  
  @Test
  @Ignore
  public void testGetWeatherForCity_WillFailVerify() throws Exception {
    // Set up expectations.
    mock.getWeatherForCity("cn", "beijing");
    mock.getWeatherForCityThenReturn(json);
    
    // Replay and test.
    mock.replay();
    
    // Verify.
    mock.verify();
  }

}
