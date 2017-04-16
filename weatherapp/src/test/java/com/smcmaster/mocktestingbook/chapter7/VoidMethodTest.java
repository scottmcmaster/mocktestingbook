package com.smcmaster.mocktestingbook.chapter7;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.smcmaster.weatherapp.resources.WeatherResourceV3;
import com.smcmaster.weatherapp.services.WeatherService;

public class VoidMethodTest {

  private WeatherService mock;
  private WeatherResourceV3 resource;
  
  @Before
  public void setUp() throws IOException {
    mock = mock(WeatherService.class); // returns a WeatherService
    resource = new WeatherResourceV3(mock);
  }

  @Test
  public void testHealth() {
    // This won't compile, expect() takes a value.
    //expect(mock.ping());
    
    // Instead, we use expectLastCall().
    mock.ping();
    expectLastCall();
    
    replay(mock);
    
    String result = resource.checkHealth();
    assertEquals("OK", result);
    verify(mock);
  }

  @Test
  public void testHealthWithStub() {
    mock.ping();
    expectLastCall().asStub();
    
    replay(mock);
    
    String result = resource.checkHealth();
    assertEquals("OK", result);
    verify(mock);
  }

}
