package com.smcmaster.mocktestingbook.chapter11;

import static org.easymock.EasyMock.capture;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.easymock.Capture;
import org.easymock.CaptureType;
import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.resources.WeatherResourceV3;
import com.smcmaster.weatherapp.services.WeatherQuery;
import com.smcmaster.weatherapp.services.WeatherService;

public class CaptureTest {
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
  public void testGetWeatherForCity_Capture() throws Exception {
    // Set up expectations.
    Capture<String> countryCap = EasyMock.newCapture();
    Capture<String> cityCap = EasyMock.newCapture();
    expect(mock.getWeatherForCity(capture(countryCap), capture(cityCap)))
      .andReturn(json);
    
    // Replay and test.
    replay(mock);
    Weather result = resource.getWeatherForCity("cn", "Beijing");
    
    // Verify.
    validateWeather(result);
    assertEquals("cn", countryCap.getValue());
    assertEquals("Beijing", cityCap.getValue());
    verify(mock);
  }

  @Test
  public void testGetWeatherForCities_CaptureMultiple()
      throws Exception {
    // Set up expectations.
    Capture<String> cityCap = EasyMock.newCapture(CaptureType.ALL);
    expect(mock.getWeatherForCity(
        eq("cn"),
        capture(cityCap)))
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
    for (String expectedCity : cities) {
      assertTrue(cityCap.getValues().contains(expectedCity));
    }
    verify(mock);
  }
  
  @Test
  public void testGetWeatherForCity_CaptureObject()
      throws Exception {
    // Set up expectations.
    Capture<WeatherQuery> queryCap = EasyMock.newCapture();
    expect(mock.getWeatherForCityQuery(capture(queryCap)))
      .andReturn(json);
    
    // Replay and test.
    replay(mock);
    
    // Test the alternative method that uses the query
    // under the hood.
    Weather result = resource.getWeatherForCityUsingQuery(
        "cn", "Beijing");
    
    // Verify.
    assertEquals("Beijing", result.getCityName());
    assertEquals(queryCap.getValue().getCity(), "Beijing");
    assertEquals(queryCap.getValue().getCountry(), "cn");
    verify(mock);
  }

  private void validateWeather(Weather result) {
	assertEquals("Beijing", result.getCityName());
    assertEquals("10", result.getTemperature());
    assertEquals("1000", result.getPressure());
    assertEquals("2@300", result.getWind());
    assertEquals("27", result.getHumidity());
  }

}
