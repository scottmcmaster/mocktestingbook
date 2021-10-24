package com.smcmaster.mocktestingbook.chapter9;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.resources.WeatherResourceV3;
import com.smcmaster.weatherapp.services.WeatherService;

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
  public void testGetWeatherForCity_ForgotVerify() throws Exception {
    // Set up expectations.
    expect(mock.getWeatherForCity("cn", "Beijing"))
      .andReturn(json);
    
    // Replay and test.
    replay(mock);
    Weather result = resource.getWeatherForCity("cn", "Beijing");
    
    validateWeather(result);
    // DON'T Verify.
  }
  
  @Test
  public void testGetWeatherForCity_ForgotVerifyBad1()
      throws Exception {
    // Set up expectations.
    expect(mock.getWeatherForCity("cn", "beijing"))
        .andReturn(json);
    
    // Oops, we forgot to actually DO anything to the
    // class under test.
    
    // Replay and test.
    replay(mock);
    
    // DON'T Verify.
  }

  
  @Test
  public void testGetWeatherForCity_ForgotVerifyBad2()
      throws Exception {
    // Set up expectations.
    expect(mock.getWeatherForCity("cn", "Beijing"))
        .andReturn(json);
    expect(mock.getWeatherForCity("cn", "Shanghai"))
        .andReturn(json);
   
    // Replay and test.
    replay(mock);
    List<String> cities = new ArrayList<>();
    cities.add("Beijing");
    resource.getWeatherForCities("cn", cities);
    
    // DON'T Verify.
  }

  private void validateWeather(Weather result) {
	assertEquals("Beijing", result.getCityName());
    assertEquals("10", result.getTemperature());
    assertEquals("1000", result.getPressure());
    assertEquals("2@300", result.getWind());
    assertEquals("27", result.getHumidity());
  }

}
