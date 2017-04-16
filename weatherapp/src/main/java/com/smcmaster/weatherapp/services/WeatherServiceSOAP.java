package com.smcmaster.weatherapp.services;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import com.smcmaster.weatherapp.models.Weather;

@WebService(targetNamespace = "http://com.smcmaster.weatherapp/WeatherServiceSOAP")
public interface WeatherServiceSOAP {

  @WebMethod(operationName = "GetWeather")
  Weather getWeather(@WebParam(name="country") String country,
      @WebParam(name="city") String city) throws Exception;
}
