package com.smcmaster.weatherapp.resources;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smcmaster.weatherapp.models.Weather;

public class WeatherResourceUtil {
	
	private ObjectMapper mapper = new ObjectMapper();

	@SuppressWarnings("unchecked")
	public Weather createWeatherFromJSON(String json)
			throws Exception {
	    Map<String, Object> map = new HashMap<>();
	    map = mapper.readValue(json, new TypeReference<Map<String, Object>>(){});

	    Weather weather = new Weather(map.get("name").toString());    
	    if (map.containsKey("main")) {
	      Map<String, Object> mainData = (Map<String, Object>) map.get("main");
	      if (mainData.containsKey("temp")) {
	        weather.setTemperature(mainData.get("temp").toString());
	      }
	      if (mainData.containsKey("pressure")) {
	        weather.setPressure(mainData.get("pressure").toString());
	      }
	      if (mainData.containsKey("humidity")) {
	        weather.setHumidity(mainData.get("humidity").toString());
	      }
	    }
	    
	    if (map.containsKey("wind")) {
	      Map<String, Object> windData = (Map<String, Object>) map.get("wind");
	      if (windData.containsKey("speed")) {
	        String windStr = windData.get("speed").toString();
	        if (windData.containsKey("deg")) {
	          windStr += "@";
	          windStr += windData.get("deg").toString();
	        }
	        weather.setWind(windStr);
	      }
	    }
	    return weather;
	}
}
