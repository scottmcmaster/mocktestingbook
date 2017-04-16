package com.smcmaster.weatherapp.health;

import com.codahale.metrics.health.HealthCheck;

public class NullHealthCheck extends HealthCheck {
  @Override
  protected Result check() throws Exception {
    return Result.healthy();
  }
}
