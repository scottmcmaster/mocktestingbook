package com.smcmaster.mocktestingbook.chapter4;

import static org.junit.Assert.*;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.easymock.EasyMockRunner;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.resources.WeatherResourceV2;
import com.smcmaster.weatherapp.services.WeatherService;

import static org.easymock.EasyMock.*;

@RunWith(EasyMockRunner.class)
public class BaseClassTest extends EasyMockSupport {
  
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
    expect(mock.getWeatherForCity("cn", "beijing"))
      .andReturn(json);
    
    // Replay and test.
    replayAll();
    Weather result = resource.getWeatherForCity("cn", "beijing");
    
    // Verify.
    validateWeather(result);
    verifyAll();
  }

  private void validateWeather(Weather result) {
    assertEquals(result.getCityName(), "beijing");
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
    replayAll();
    
    // Verify.
    verifyAll();
  }

}
