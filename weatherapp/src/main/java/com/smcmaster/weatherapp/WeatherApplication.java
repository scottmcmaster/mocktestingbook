package com.smcmaster.weatherapp;

import java.util.EnumSet;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;

import org.eclipse.jetty.servlets.CrossOriginFilter;

import com.hubspot.dropwizard.guice.GuiceBundle;
import com.roskart.dropwizard.jaxws.EndpointBuilder;
import com.roskart.dropwizard.jaxws.JAXWSBundle;
import com.smcmaster.weatherapp.health.NullHealthCheck;
import com.smcmaster.weatherapp.resources.WeatherResourceV1;
import com.smcmaster.weatherapp.resources.WeatherResourceV2;
import com.smcmaster.weatherapp.resources.WeatherResourceV3;
import com.smcmaster.weatherapp.resources.WeatherServiceSOAPImpl;

import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;


public class WeatherApplication extends Application<WeatherApplicationConfiguration> {
  
  private GuiceBundle<WeatherApplicationConfiguration> guiceBundle;
  private JAXWSBundle jaxWsBundle = new JAXWSBundle();

  @Override
  public void initialize(Bootstrap<WeatherApplicationConfiguration> bootstrap) {

    guiceBundle = GuiceBundle.<WeatherApplicationConfiguration>newBuilder()
      .addModule(new WeatherApplicationModule())
      .setConfigClass(WeatherApplicationConfiguration.class)
      .build();

    bootstrap.addBundle(new AssetsBundle("/assets", "/", "index.html"));
    bootstrap.addBundle(guiceBundle);
    bootstrap.addBundle(jaxWsBundle);
  }


  @Override
  public void run(final WeatherApplicationConfiguration config,
      final Environment environment) throws Exception {
    final DefaultServerFactory serverFactory =
        (DefaultServerFactory) config.getServerFactory();
        serverFactory.setApplicationContextPath("/");
        serverFactory.setJerseyRootPath("/api");
        
    environment.healthChecks().register("nullCheck", new NullHealthCheck());
    environment.jersey().register(WeatherResourceV1.class);
    environment.jersey().register(WeatherResourceV2.class);
    environment.jersey().register(WeatherResourceV3.class);
    
    // Enable CORS headers
    final FilterRegistration.Dynamic cors =
        environment.servlets().addFilter("CORS", CrossOriginFilter.class);

    // Configure CORS parameters
    cors.setInitParameter("allowedOrigins", "*");
    cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
    cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

    jaxWsBundle.publishEndpoint(
        new EndpointBuilder("/weather",
            new WeatherServiceSOAPImpl()));
    
    // Add URL mapping
    cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
  }


  public static void main(String[] args) throws Exception {
    new WeatherApplication().run(args);
  }
}
