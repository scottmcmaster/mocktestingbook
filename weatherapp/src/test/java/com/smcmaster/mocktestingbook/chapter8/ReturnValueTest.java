package com.smcmaster.mocktestingbook.chapter8;

import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.ws.rs.NotFoundException;

import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.easymock.IAnswer;
import org.junit.Before;
import org.junit.Test;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.resources.WeatherResourceV3;
import com.smcmaster.weatherapp.services.WeatherService;

public class ReturnValueTest {

  private String json;
  private String templateJson;
  private WeatherService mock;
  private WeatherResourceV3 resource;
  
  @Before
  public void setUp() throws IOException {
    json = IOUtils.toString(
        getClass().getResourceAsStream("/beijing_owm.json"),
        "UTF-8");
    templateJson = IOUtils.toString(
        getClass().getResourceAsStream("/template_owm.json"),
        "UTF-8");
    mock = mock(WeatherService.class); // returns a WeatherService
    resource = new WeatherResourceV3(mock);
  }

  @Test
  public void testGetWeatherForCities_Stub()
      throws Exception {
    // Set up expectations.
    expect(mock.getWeatherForCity(anyString(), anyString()))
      .andStubReturn(json);
    
    // Replay and test.
    replay(mock);
    String[] cities = new String[] {"beijing", "shanghai",
        "dalian", "suzhou", "hangzhou", "xian", "guangzhou"};
    ArrayList<Weather> result =
        resource.getWeatherForCities("cn", Arrays.asList(cities));
    
    // Verify.
    assertEquals(cities.length, result.size());
    verify(mock);
  }

  @Test
  public void testGetWeatherForCities_Exception()
      throws Exception {
    // Set up expectations.
    expect(mock.getWeatherForCity(anyString(), anyString()))
      .andThrow(new NotFoundException());
    
    // Replay and test.
    replay(mock);
    boolean caught = false;
    try {
      resource.getWeatherForCity("cn", "beijing");
    } catch (NotFoundException nfe) {
      // Expect the exception to propagate.
      caught = true;
    }
    
    // Verify.
    assertTrue(caught);
    verify(mock);
  }

  @Test
  public void testGetWeatherForCities_AndReturn()
      throws Exception {
    // Set up expectations.
    expect(mock.getWeatherForCity(anyString(), anyString()))
      .andAnswer(new IAnswer<String>() {
        @Override
        public String answer() throws Throwable {
          String city = (String) EasyMock.getCurrentArguments()[1];
          return templateJson.replace("__CITYNAME__", city);
        }       
      })
      .anyTimes();
    
    // Replay and test.
    replay(mock);
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
    verify(mock);
  }

  @Test
  public void testGetWeatherForCities_AndReturnLambda()
      throws Exception {
    // Set up expectations.
    expect(mock.getWeatherForCity(anyString(), anyString()))
      .andAnswer(() -> 
        templateJson.replace("__CITYNAME__",
            (String) EasyMock.getCurrentArguments()[1]))
      .anyTimes();
    
    // Replay and test.
    replay(mock);
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
    verify(mock);
  }

}
