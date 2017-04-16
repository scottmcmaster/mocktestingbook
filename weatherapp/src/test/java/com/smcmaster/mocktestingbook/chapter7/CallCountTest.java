package com.smcmaster.mocktestingbook.chapter7;

import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
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

public class CallCountTest {

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
  public void testGetWeatherForCities_AnyTimes()
      throws Exception {
    // Set up expectations.
    expect(mock.getWeatherForCity(eq("cn"), anyString()))
      .andReturn(json)
      .anyTimes();
    
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

  @Test
  public void testGetWeatherForCities_Range()
      throws Exception {
    // Set up expectations.
    expect(mock.getWeatherForCity(eq("cn"), anyString()))
      .andReturn(json)
      .times(1, 2);
    
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

  @Test
  public void testGetWeatherForCities_Exact()
      throws Exception {
    // Set up expectations.
    expect(mock.getWeatherForCity(eq("cn"), anyString()))
      .andReturn(json)
      .times(2);
    
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

  @Test
  public void testGetWeatherForCities_NoInteract()
      throws Exception {
    // Set up expectations.
    // Should be no interactions.
    
    // Replay and test.
    replay(mock);
    try {
      resource.getWeatherForCity("cn", null);
    } catch (IllegalArgumentException ex) {
      // Good, this is what we want.
    }
    
    // Verify.
    verify(mock);
  }
}
