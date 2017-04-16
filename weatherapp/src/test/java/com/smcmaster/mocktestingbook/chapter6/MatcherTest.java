package com.smcmaster.mocktestingbook.chapter6;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.IOUtils;
import org.easymock.IArgumentMatcher;
import org.easymock.LogicalOperator;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.resources.WeatherResourceV3;
import com.smcmaster.weatherapp.services.WeatherQuery;
import com.smcmaster.weatherapp.services.WeatherService;

import static org.easymock.EasyMock.*;

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
    expect(mock.getWeatherForCity(anyString(), anyString()))
      .andReturn(json);
    
    // Replay and test.
    replay(mock);
    Weather result = resource.getWeatherForCity("not a real country",
        "beijing");
    
    // Verify.
    assertEquals(result.getCityName(), "Beijing");
    verify(mock);
  }

  @Test
  public void testGetWeatherForCity_PartialMatch() throws Exception {
    // Set up expectations.
    expect(mock.getWeatherForCity(anyString(), eq("beijing")))
      .andReturn(json);
    
    // Replay and test.
    replay(mock);
    Weather result = resource.getWeatherForCity("not a real country",
        "beijing");
    
    // Verify.
    assertEquals(result.getCityName(), "Beijing");
    verify(mock);
  }

  @Test
  public void testGetWeatherForCities_OrMatcher() throws Exception {
    // Set up expectations.
    expect(mock.getWeatherForCity(
        eq("cn"),
        or(eq("beijing"), eq("shanghai"))))
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
  
  @Ignore
  @Test
  public void testGetWeatherForCity_Object_Fails() throws Exception {
    // Set up expectations.
    expect(mock.getWeatherForCityQuery(
        new WeatherQuery("cn", "beijing")))
      .andReturn(json);
    
    // Replay and test.
    replay(mock);
    
    // Test the alternative method that uses the query
    // under the hood.
    Weather result = resource.getWeatherForCityUsingQuery(
        "cn", "beijing");
    
    // Verify.
    assertEquals(result.getCityName(), "beijing");
    verify(mock);
  }

  @Test
  public void testGetWeatherForCity_Object_Comparator()
      throws Exception {
    // Set up expectations.
    expect(mock.getWeatherForCityQuery(
        cmp(new WeatherQuery("cn", "beijing"),
            new Comparator<WeatherQuery>() {
              @Override
              public int compare(WeatherQuery q1, WeatherQuery q2) {
                return Objects.equals(q1.getCity(), q2.getCity()) &&
                    Objects.equals(q1.getCountry(), q2.getCountry())
                    ? 0 : 1;
              }},
            LogicalOperator.EQUAL)))
      .andReturn(json);
    
    // Replay and test.
    replay(mock);
    
    // Test the alternative method that uses the query
    // under the hood.
    Weather result = resource.getWeatherForCityUsingQuery(
        "cn", "beijing");
    
    // Verify.
    assertEquals(result.getCityName(), "Beijing");
    verify(mock);
  }
  
  @Test
  public void testGetWeatherForCity_Object_CustomMatcher()
      throws Exception {
    // Set up expectations.
    expect(mock.getWeatherForCityQuery(
        weatherQueryEq(new WeatherQuery("cn", "beijing"))))
    .andReturn(json);
    
    // Replay and test.
    replay(mock);
    
    // Test the alternative method that uses the query
    // under the hood.
    Weather result = resource.getWeatherForCityUsingQuery(
        "cn", "beijing");
    
    // Verify.
    assertEquals(result.getCityName(), "Beijing");
    verify(mock);
  }
  
  public static WeatherQuery weatherQueryEq(WeatherQuery expected) {
    reportMatcher(new WeatherQueryMatcher(expected));
    return null;
  }
  
  private static class WeatherQueryMatcher
      implements IArgumentMatcher {
    
    private final WeatherQuery expected;

    public WeatherQueryMatcher(WeatherQuery expected) {
      this.expected = expected;
    }
    
    @Override
    public void appendTo(StringBuffer message) {
      message.append("WeatherQuery: ");
      message.append(expected.getCountry());
      message.append(", ");
      message.append(expected.getCity());
    }

    @Override
    public boolean matches(Object obj) {
      if (!(obj instanceof WeatherQuery)) {
        return false;
      }
      WeatherQuery actual = (WeatherQuery) obj;
      return Objects.equals(actual.getCity(), expected.getCity()) &&
          Objects.equals(actual.getCountry(), expected.getCountry());
    }
  }
}
