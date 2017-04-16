package com.smcmaster.mocktestingbook.chapter3;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.lang3.NotImplementedException;
import org.junit.Assert;

import com.smcmaster.weatherapp.services.WeatherQuery;
import com.smcmaster.weatherapp.services.WeatherService;

/**
 * Standalone mock demonstration.
 */
public class WeatherServiceMock implements WeatherService {
  
  /**
   * Whether the mock is in record or reply mode.
   */
  private boolean isReplayMode;
  
  // Hold the previous input for setting up expected outputs.
  private GetWeatherForCityInputs lastGetWeatherForCityInput;
  
  /**
   * The expected outputs by input.
   */
  private Map<GetWeatherForCityInputs, String> getWeatherForCityOutputs =
      new HashMap<>();

  /**
   * The expected calls, set up in record mode.
   */
  private Queue<GetWeatherForCityInputs> expectedGetWeatherForCityInputs =
      new ArrayDeque<>();
  
  /**
   * The actual calls, observed in replay mode.
   */
  private Queue<GetWeatherForCityInputs> actualGetWeatherForCityInputs =
      new ArrayDeque<>();
  
  /**
   * Holds the input parameters to calls to the getWeatherForCity method.
   */
  private static class GetWeatherForCityInputs {
    private final String country;
    private final String city;
    
    public GetWeatherForCityInputs(String country, String city) {
      this.country = country;
      this.city = city;
    }
    
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((city == null) ? 0 : city.hashCode());
      result = prime * result + ((country == null) ? 0 : country.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      GetWeatherForCityInputs other = (GetWeatherForCityInputs) obj;
      if (city == null) {
        if (other.city != null)
          return false;
      } else if (!city.equals(other.city))
        return false;
      if (country == null) {
        if (other.country != null)
          return false;
      } else if (!country.equals(other.country))
        return false;
      return true;
    }
    
    @Override
    public String toString() {
      return "getWeatherForCity(country: " + country + ", city: " + city + ")";
    }
  }
  
  @Override
  public String getWeatherForCity(String country, String city)
      throws Exception {
    GetWeatherForCityInputs input =
        new GetWeatherForCityInputs(country, city);
    if (isReplayMode) {
      // Capture the actual call.
      if (!getWeatherForCityOutputs.containsKey(input)) {
        Assert.fail("getWeatherForCity: Unexpected input: " + input);
      }
      actualGetWeatherForCityInputs.add(input);
      
      // Look up/return expected output for the given input.      
      return getWeatherForCityOutputs.get(input);
    } else {
      // Record the call as an expectation.
      lastGetWeatherForCityInput = input;
      expectedGetWeatherForCityInputs.add(input);
      return null;
    }
  }
  
  @Override
  public void ping() {
    // Nothing to do.
  }

  /**
   * Associates an ouput with the previous input.
   */
  public void getWeatherForCityThenReturn(String output) {
    if (lastGetWeatherForCityInput == null) {
      throw new IllegalStateException("No expected input");
    }
    getWeatherForCityOutputs.put(lastGetWeatherForCityInput, output);
    lastGetWeatherForCityInput = null;
  }
  
  /**
   * Puts the mock into replay mode.
   */
  public void replay() {
    isReplayMode = true;
  }
  
  /**
   * Verifies that all expectations were hit.
   */
  public void verify() {
    StringBuilder sb = new StringBuilder();
    for (GetWeatherForCityInputs input : expectedGetWeatherForCityInputs) {
      if (!actualGetWeatherForCityInputs.contains(input)) {
        sb.append(input.toString());
        sb.append('\n');
      }
    }
    if (sb.length() > 0) {
      Assert.fail("Expectations missed:\n" + sb.toString());
    }
  }

  @Override
  public String getWeatherForCityQuery(WeatherQuery query) throws Exception {
    throw new NotImplementedException("Use getWeatherForCity");
  }
}
