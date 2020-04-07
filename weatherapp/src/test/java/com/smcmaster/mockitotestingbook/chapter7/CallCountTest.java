package com.smcmaster.mockitotestingbook.chapter7;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.resources.WeatherResourceV3;
import com.smcmaster.weatherapp.services.WeatherService;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
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
    when(mock.getWeatherForCity(eq("cn"), anyString()))
      .thenReturn(json);

    // Replay and test.
    List<String> cities = new ArrayList<>();
    cities.add("beijing");
    cities.add("shanghai");
    ArrayList<Weather> result =
        resource.getWeatherForCities("cn", cities);
    
    // Verify.
    assertEquals(2, result.size());
  }

  @Test
  public void testGetWeatherForCities_Range()
      throws Exception {
    // Set up expectations.
    when(mock.getWeatherForCity(eq("cn"), anyString()))
      .thenReturn(json);
    
    // Replay and test.
    List<String> cities = new ArrayList<>();
    cities.add("beijing");
    cities.add("shanghai");
    ArrayList<Weather> result =
        resource.getWeatherForCities("cn", cities);
    
    // Verify.
    assertEquals(2, result.size());
    verify(mock, atLeast(1)).getWeatherForCity(eq("cn"), anyString());
    verify(mock, atMost(2)).getWeatherForCity(eq("cn"), anyString());
  }

  @Test
  public void testGetWeatherForCities_Exact()
      throws Exception {
    // Set up expectations.
    when(mock.getWeatherForCity(eq("cn"), anyString()))
      .thenReturn(json);
    
    // Replay and test.
    List<String> cities = new ArrayList<>();
    cities.add("beijing");
    cities.add("shanghai");
    ArrayList<Weather> result =
        resource.getWeatherForCities("cn", cities);
    
    // Verify.
    assertEquals(2, result.size());
    
    verify(mock, times(2)).getWeatherForCity(eq("cn"), anyString());
  }

  @Test
  public void testGetWeatherForCities_NoInteract()
      throws Exception {
    // Set up expectations.
    // Should be no interactions.
    
    // Replay and test.
    try {
      resource.getWeatherForCity("cn", null);
    } catch (IllegalArgumentException ex) {
      // Good, this is what we want.
    }
    
    // Verify.
    verifyNoMoreInteractions(mock);
  }
}
