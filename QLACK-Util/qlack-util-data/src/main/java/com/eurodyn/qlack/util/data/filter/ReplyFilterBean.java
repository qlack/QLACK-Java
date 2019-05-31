package com.eurodyn.qlack.util.data.filter;

import com.eurodyn.qlack.util.data.jackson.QInstantFromISOUTCDeserializer;
import com.eurodyn.qlack.util.data.jackson.QInstantToISOUTCSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.github.bohnman.squiggly.context.provider.SimpleSquigglyContextProvider;
import com.github.bohnman.squiggly.filter.SquigglyPropertyFilter;
import com.github.bohnman.squiggly.filter.SquigglyPropertyFilterMixin;
import com.github.bohnman.squiggly.parser.SquigglyParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;

/**
 * Global configuration of Spring's Jackson {@link ObjectMapper} to include
 * https://github.com/bohnman/squiggly-java filters.
 */

//TODO Creating our own ObjectMapper to register squiggly overrides the default ObjectMapper from Spring which effectively
//ignores any other annotation-based/bean-based/configuration-based configuration for Jackson a user might need (i.e. all
// configuration has to take place here). How can we do this better?
@Configuration
public class ReplyFilterBean {

  /**
   * Reconfiguration of Spring's Jackson {@link ObjectMapper} to include
   * https://github.com/bohnman/squiggly-java filters.
   */
  @Bean
  public ObjectMapper objectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.addMixIn(Object.class, SquigglyPropertyFilterMixin.class);

    SquigglyPropertyFilter propertyFilter = new SquigglyPropertyFilter(
        new SimpleSquigglyContextProvider(new SquigglyParser(), "**"));
    objectMapper.setFilterProvider(
        new SimpleFilterProvider().addFilter(SquigglyPropertyFilter.FILTER_ID, propertyFilter));

    // Registering Instant serializer/deserializer, this is better to be configured by the target project (see TODO above).
    SimpleModule module = new SimpleModule();
    module.addDeserializer(Instant.class, new QInstantFromISOUTCDeserializer());
    module.addSerializer(Instant.class, new QInstantToISOUTCSerializer());
    objectMapper.registerModule(module);

    return objectMapper;
  }
}
