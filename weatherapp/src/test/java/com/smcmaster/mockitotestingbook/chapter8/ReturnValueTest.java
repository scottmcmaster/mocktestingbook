package com.smcmaster.mockitotestingbook.chapter8;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.NotFoundException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.resources.WeatherResourceV3;
import com.smcmaster.weatherapp.services.WeatherService;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReturnValueTest {

  private String templateJson;
  private WeatherService mock;
  private WeatherResourceV3 resource;
  
  @Before
  public void setUp() throws IOException {
    templateJson = IOUtils.toString(
        getClass().getResourceAsStream("/template_owm.json"),
        "UTF-8");
    mock = mock(WeatherService.class); // returns a WeatherService
    resource = new WeatherResourceV3(mock);
  }

  @Test
  public void testGetWeatherForCities_Exception()
      throws Exception {
    // Set up expectations.
    when(mock.getWeatherForCity(anyString(), anyString()))
      .thenThrow(new NotFoundException());
    
    // Replay and test.
    boolean caught = false;
    try {
      resource.getWeatherForCity("cn", "beijing");
    } catch (NotFoundException nfe) {
      // Expect the exception to propagate.
      caught = true;
    }
    
    // Verify.
    assertTrue(caught);
  }

  @Test
  public void testGetWeatherForCities_AndReturn()
      throws Exception {
    // Set up expectations.
    when(mock.getWeatherForCity(anyString(), anyString()))
      .thenAnswer(new Answer<String>() {
        @Override
        public String answer(InvocationOnMock invocation) {
          String city = (String) invocation.getArguments()[1];
          return templateJson.replace("__CITYNAME__", city);
        }       
      });
    
    // Replay and test.
    String[] cities = new String[] {"beijing", "shanghai",
        "dalian", "suzhou", "hangzhou", "xian", "guangzhou"};
    ArrayList<Weather> result =
        resource.getWeatherForCities("cn", Arrays.asList(cities));
    
    // Verify.
    assertEquals(cities.length, result.size());
    // Should be one Weather per input city.
    for (String city : cities) {
      assertTrue(result.stream()
          .anyMatch(item -> city.equals(item.getCityName())));
    }
  }

  @Test
  public void testGetWeatherForCities_AndReturnLambda()
      throws Exception {
    // Set up expectations.
    when(mock.getWeatherForCity(anyString(), anyString()))
      .thenAnswer((InvocationOnMock invocation) -> 
        templateJson.replace("__CITYNAME__",
            (String) invocation.getArguments()[1]));
    
    // Replay and test.
    String[] cities = new String[] {"beijing", "shanghai",
        "dalian", "suzhou", "hangzhou", "xian", "guangzhou"};
    ArrayList<Weather> result =
        resource.getWeatherForCities("cn", Arrays.asList(cities));
    
    // Verify.
    assertEquals(cities.length, result.size());
    // Should be one Weather per input city.
    for (String city : cities) {
      assertTrue(result.stream()
          .anyMatch(item -> city.equals(item.getCityName())));
    }
  }

}
