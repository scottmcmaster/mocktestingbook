package com.smcmaster.mockitotestingbook.chapter10;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.openweathermap.OpenWeatherMapWeatherServiceV0;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PartialMockTest {

  private String json;

  @Before
  public void setUp() throws IOException {
    json = IOUtils.toString(
        getClass().getResourceAsStream("/beijing_owm.json"),
        "UTF-8");
  }

  @Test
  public void testGetWeatherForCity() throws Exception {
    OpenWeatherMapWeatherServiceV0 service =
        spy(OpenWeatherMapWeatherServiceV0.class);
    doReturn(json)
      .when(service)
      .retrieveWeatherData("cn", "Beijing");
    
    // Replay.
    Weather result = service.getWeatherForCity("cn", "Beijing");
    
    // Verify.
    assertWeatherContents(result);
    verify(service).retrieveWeatherData("cn", "Beijing");
  }
  
  private void assertWeatherContents(Weather result) {
    assertEquals("Beijing", result.getCityName());
    assertEquals("10", result.getTemperature());
    assertEquals("1000", result.getPressure());
    assertEquals("2@300", result.getWind());
    assertEquals("27", result.getHumidity());
  }
}
