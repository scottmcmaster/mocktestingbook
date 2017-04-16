package com.smcmaster.weatherapp.resources;

import static org.junit.Assert.*;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.services.WeatherService;

@RunWith(MockitoJUnitRunner.class)
public class WeatherResourceV2TestWithMock {

  @Mock private WeatherService weatherService;
  private WeatherResourceV2 resource;
  
  @Before
  public void setUp() {
    resource = new WeatherResourceV2(weatherService);
  }
  
  @Test
  public void testGetWeatherForCity_Valid() throws Exception {
    String json = IOUtils.toString(
        getClass().getResourceAsStream("/beijing_owm.json"),
        "UTF-8");
    Mockito
      .when(weatherService.getWeatherForCity("cn", "beijing"))
      .thenReturn(json);
    
    Weather result = resource.getWeatherForCity("cn", "beijing");
    
    assertEquals(result.getCityName(), "beijing");
    assertEquals("10", result.getTemperature());
    assertEquals("1000", result.getPressure());
    assertEquals("2@300", result.getWind());
    assertEquals("27", result.getHumidity());
    Mockito.verify(weatherService, Mockito.times(1))
      .getWeatherForCity(Mockito.anyString(), Mockito.anyString());
  }
  
  @Test
  public void testGetWeatherForCity_NotFound() throws Exception {
    Mockito
      .when(weatherService.getWeatherForCity("zz", "doesnotexist"))
      .thenReturn(null);
    
    Weather result = resource.getWeatherForCity("zz", "doesnotexist");
    
    assertEquals(result.getCityName(), "doesnotexist");
    assertNull(result.getTemperature());
    assertNull(result.getPressure());
    assertNull(result.getWind());
    assertNull(result.getHumidity());
    Mockito.verify(weatherService, Mockito.times(1))
      .getWeatherForCity(Mockito.anyString(), Mockito.anyString());
  }
}
