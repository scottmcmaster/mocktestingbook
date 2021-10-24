package com.smcmaster.mockitotestingbook.chapter6;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.junit.MockitoJUnitRunner;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.resources.WeatherResourceV3;
import com.smcmaster.weatherapp.services.WeatherQuery;
import com.smcmaster.weatherapp.services.WeatherService;

import static org.mockito.Mockito.*;
import static org.mockito.AdditionalMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public class MatcherTest {

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
  public void testGetWeatherForCity_Valid() throws Exception {
    // Set up expectations.
    when(mock.getWeatherForCity(anyString(), anyString()))
      .thenReturn(json);
    
    // Replay and test.
    Weather result = resource.getWeatherForCity("not a real country",
        "beijing");
    
    // Verify.
    assertEquals("Beijing", result.getCityName());
  }

  @Test
  public void testGetWeatherForCity_PartialMatch() throws Exception {
    // Set up expectations.
    when(mock.getWeatherForCity(anyString(), eq("beijing")))
      .thenReturn(json);
    
    // Replay and test.
    Weather result = resource.getWeatherForCity("not a real country",
        "beijing");
    
    // Verify.
    assertEquals("Beijing", result.getCityName());
  }

  @Test
  public void testGetWeatherForCities_OrMatcher() throws Exception {
    // Set up expectations.
    when(mock.getWeatherForCity(
        eq("cn"),
        or(eq("beijing"), eq("shanghai"))))
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
  
  @Ignore
  @Test
  public void testGetWeatherForCity_Object_Fails() throws Exception {
    // Set up expectations.
    when(mock.getWeatherForCityQuery(
        new WeatherQuery("cn", "beijing")))
      .thenReturn(json);
    
    // Replay and test.
    
    // Test the alternative method that uses the query
    // under the hood.
    Weather result = resource.getWeatherForCityUsingQuery(
        "cn", "beijing");
    
    // Verify.
    assertEquals("Beijing", result.getCityName());
  }

  @Test
  public void testGetWeatherForCity_Object_RefEq()
      throws Exception {
    // Set up expectations.
    when(mock.getWeatherForCityQuery(
        refEq(new WeatherQuery("cn", "beijing"))))
      .thenReturn(json);
    
    // Replay and test.
    
    // Test the alternative method that uses the query
    // under the hood.
    Weather result = resource.getWeatherForCityUsingQuery(
        "cn", "beijing");
    assertNotNull(result);
    
    // Verify.
    assertEquals("Beijing", result.getCityName());
  }
  
  @Test
  public void testGetWeatherForCity_Object_CustomMatcher()
      throws Exception {
    // Set up expectations.
    when(mock.getWeatherForCityQuery(
        weatherQueryEq(new WeatherQuery("cn", "beijing"))))
    .thenReturn(json);
    
    // Replay and test.
    
    // Test the alternative method that uses the query
    // under the hood.
    Weather result = resource.getWeatherForCityUsingQuery(
        "cn", "beijing");
    
    // Verify.
    assertEquals("Beijing", result.getCityName());
  }
  
  public static WeatherQuery weatherQueryEq(WeatherQuery expected) {
    return argThat(new WeatherQueryMatcher(expected));
  }
  
  private static class WeatherQueryMatcher
      implements ArgumentMatcher<WeatherQuery> {
    
    private final WeatherQuery expected;

    public WeatherQueryMatcher(WeatherQuery expected) {
      this.expected = expected;
    }
    
    @Override
    public boolean matches(WeatherQuery actual) {
      return Objects.equals(actual.getCity(), expected.getCity()) &&
          Objects.equals(actual.getCountry(), expected.getCountry());
    }
  }
}
