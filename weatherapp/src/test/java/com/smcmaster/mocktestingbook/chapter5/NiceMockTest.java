package com.smcmaster.mocktestingbook.chapter5;

import static org.easymock.EasyMock.niceMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.resources.WeatherResourceV3;
import com.smcmaster.weatherapp.services.WeatherService;

public class NiceMockTest {

  private WeatherService mock;
  private WeatherResourceV3 resource;
    
  @Before
  public void setUp() throws IOException {
    mock = niceMock(WeatherService.class);
    resource = new WeatherResourceV3(mock);
  }
  
  @Test
  public void testGetWeatherForCity_NotFound() throws Exception {
    // No expectations on the nice mock.
    
    // Replay and test.
    replay(mock);
    Weather result = resource.getWeatherForCity("cn", "beijing");
    
    // Verify. The city should be filled-in, everything else null.
    assertEquals("Beijing", result.getCityName());
    assertNull(result.getTemperature());
    assertNull(result.getPressure());
    assertNull(result.getWind());
    assertNull(result.getHumidity());
    verify(mock);
  }
}
