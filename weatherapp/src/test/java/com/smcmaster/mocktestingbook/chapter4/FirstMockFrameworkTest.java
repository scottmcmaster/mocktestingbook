package com.smcmaster.mocktestingbook.chapter4;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.resources.WeatherResourceV2;
import com.smcmaster.weatherapp.services.WeatherService;

import static org.easymock.EasyMock.*;

public class FirstMockFrameworkTest {

  private String json;
  private WeatherService mock;
  private WeatherResourceV2 resource;
  
  @Before
  public void setUp() throws IOException {
    json = IOUtils.toString(
        getClass().getResourceAsStream("/beijing_owm.json"),
        "UTF-8");
    mock = mock(WeatherService.class); // returns a WeatherService
    resource = new WeatherResourceV2(mock);
  }
  
  @Test
  public void testGetWeatherForCity_Valid() throws Exception {
    // Set up expectations.
    expect(mock.getWeatherForCity("cn", "beijing"))
      .andReturn(json);
    
    // Replay and test.
    replay(mock);
    Weather result = resource.getWeatherForCity("cn", "beijing");
    
    // Verify.
    validateWeather(result);
    verify(mock);
  }

  private void validateWeather(Weather result) {
	assertEquals("Beijing", result.getCityName());
    assertEquals("10", result.getTemperature());
    assertEquals("1000", result.getPressure());
    assertEquals("2@300", result.getWind());
    assertEquals("27", result.getHumidity());
  }

  @Test
  @Ignore
  public void testGetWeatherForCity_WillFailVerify() throws Exception {
    // Set up expectations.
    expect(mock.getWeatherForCity("cn", "beijing"))
        .andReturn(json);
    
    // Replay and test.
    replay(mock);
    
    // Verify.
    verify(mock);
  }

}
