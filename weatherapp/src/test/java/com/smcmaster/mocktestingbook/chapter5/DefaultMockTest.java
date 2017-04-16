package com.smcmaster.mocktestingbook.chapter5;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.resources.WeatherResourceV3;
import com.smcmaster.weatherapp.services.WeatherService;

import static org.easymock.EasyMock.*;

public class DefaultMockTest {

  private String bjJson;
  private String shJson;
  private WeatherService mock;
  private WeatherResourceV3 resource;
  
  @Before
  public void setUp() throws IOException {
    bjJson = IOUtils.toString(
        getClass().getResourceAsStream("/beijing_owm.json"),
        "UTF-8");
    shJson = IOUtils.toString(
        getClass().getResourceAsStream("/shanghai_owm.json"),
        "UTF-8");
    mock = mock(WeatherService.class); // returns a WeatherService
    resource = new WeatherResourceV3(mock);
  }
  
  @Test
  public void testGetWeatherForCities() throws Exception {
    // Set up expectations.
    expect(mock.getWeatherForCity("cn", "beijing"))
      .andReturn(bjJson);
    expect(mock.getWeatherForCity("cn", "shanghai"))
      .andReturn(shJson);
    
    // Replay and test.
    replay(mock);
    List<String> cities = new ArrayList<>();
    cities.add("beijing");
    cities.add("shanghai");
    ArrayList<Weather> result =
        resource.getWeatherForCities("cn", cities);
    
    // Verify.
    assertEquals(2, result.size());
    verify(mock);
  }
}
