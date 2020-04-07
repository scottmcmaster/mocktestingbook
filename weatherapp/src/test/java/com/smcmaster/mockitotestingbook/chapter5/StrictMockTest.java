package com.smcmaster.mockitotestingbook.chapter5;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.junit.MockitoJUnitRunner;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.resources.WeatherResourceV3;
import com.smcmaster.weatherapp.services.WeatherService;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StrictMockTest {

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
    mock = mock(WeatherService.class);
    resource = new WeatherResourceV3(mock);
  }
  
  @Test
  public void testGetWeatherForCities() throws Exception {
    // Set up expectations.
    InOrder inorder = inOrder(mock);
    when(mock.getWeatherForCity("cn", "beijing"))
      .thenReturn(bjJson);
    when(mock.getWeatherForCity("cn", "shanghai"))
      .thenReturn(shJson);
    
    // Replay and test.
    List<String> cities = new ArrayList<>();
    cities.add("beijing");
    cities.add("shanghai");
    ArrayList<Weather> result =
        resource.getWeatherForCities("cn", cities);
    
    // Verify.
    assertEquals(2, result.size());
    inorder.verify(mock).getWeatherForCity("cn", "beijing");
    inorder.verify(mock).getWeatherForCity("cn", "shanghai");
  }
  
  @Ignore
  @Test
  public void testGetWeatherForCities_Reordered() throws Exception {
    // Set up expectations in the wrong order.
    InOrder inorder = inOrder(mock);
    when(mock.getWeatherForCity("cn", "shanghai"))
      .thenReturn(shJson);
    when(mock.getWeatherForCity("cn", "beijing"))
      .thenReturn(bjJson);
    
    // Replay and test.
    List<String> cities = new ArrayList<>();
    cities.add("shanghai");
    cities.add("beijing");
    ArrayList<Weather> result =
        resource.getWeatherForCities("cn", cities);
    
    // Verify.
    assertEquals(2, result.size());
    inorder.verify(mock).getWeatherForCity("cn", "beijing");
    inorder.verify(mock).getWeatherForCity("cn", "shanghai");
  }
}
