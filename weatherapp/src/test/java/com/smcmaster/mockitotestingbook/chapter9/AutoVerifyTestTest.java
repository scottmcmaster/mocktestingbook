package com.smcmaster.mockitotestingbook.chapter9;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.resources.WeatherResourceV3;
import com.smcmaster.weatherapp.services.WeatherService;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AutoVerifyTestTest extends AutoVerifyTest {

  private String json;
  private WeatherService mock;
  private WeatherResourceV3 resource;
  
  @Before
  public void setUp() throws IOException {
    json = IOUtils.toString(
        getClass().getResourceAsStream("/beijing_owm.json"),
        "UTF-8");
    mock = createMock(WeatherService.class); // returns a WeatherService
    resource = new WeatherResourceV3(mock);
  }
  
  @Test
  @Ignore
  public void testGetWeatherForCity_ForgotVerify()
      throws Exception {
    // Set up expectations.
    when(mock.getWeatherForCity(anyString(), anyString()))
      .thenReturn(json);
    
    // Replay and test.
    List<String> cities = new ArrayList<>();
    cities.add("Beijing");
    cities.add("Shanghai");
    resource.getWeatherForCities("cn", cities);
    
    // Only verify ONE of two expectations.
    verify(mock).getWeatherForCity("cn", "Beijing");
    
    // Add this line to fix the test:
    //verify(mock).getWeatherForCity("cn", "Shanghai");
  }

  private void validateWeather(Weather result) {
    assertEquals(result.getCityName(), "Beijing");
    assertEquals("10", result.getTemperature());
    assertEquals("1000", result.getPressure());
    assertEquals("2@300", result.getWind());
    assertEquals("27", result.getHumidity());
  }
}
