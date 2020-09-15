package com.eurodyn.qlack.util.data.rest;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * A default provider for {@link RestTemplate} incorporating custom logging of HTTP errors.
 */
@Component
public class RestTemplateProvider {
  private final RestTemplateBuilder restTemplateBuilder;

  public RestTemplateProvider(
    RestTemplateBuilder restTemplateBuilder) {
    this.restTemplateBuilder = restTemplateBuilder;
  }

  @Bean
  public RestTemplate build() {
      return restTemplateBuilder
        .errorHandler(new RestResponseErrorHandler())
        .build();
  }
}
