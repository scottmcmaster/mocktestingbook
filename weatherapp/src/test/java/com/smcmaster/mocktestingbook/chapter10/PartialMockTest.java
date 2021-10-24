package com.smcmaster.mocktestingbook.chapter10;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import static org.easymock.EasyMock.*;
import org.junit.Before;
import org.junit.Test;

import com.smcmaster.weatherapp.models.Weather;
import com.smcmaster.weatherapp.openweathermap.OpenWeatherMapWeatherServiceV0;

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
        partialMockBuilder(OpenWeatherMapWeatherServiceV0.class)
          .withConstructor()
          .addMockedMethod("retrieveWeatherData")
          .createMock();
    expect(service.retrieveWeatherData("cn", "Beijing"))
      .andReturn(json);
    
    // Replay.
    replay(service);
    
    Weather result = service.getWeatherForCity("cn", "Beijing");
    
    // Verify.
    assertWeatherContents(result);
    verify(service);
  }
  
  private void assertWeatherContents(Weather result) {
    assertEquals("Beijing", result.getCityName());
    assertEquals("10", result.getTemperature());
    assertEquals("1000", result.getPressure());
    assertEquals("2@300", result.getWind());
    assertEquals("27", result.getHumidity());
  }
}
