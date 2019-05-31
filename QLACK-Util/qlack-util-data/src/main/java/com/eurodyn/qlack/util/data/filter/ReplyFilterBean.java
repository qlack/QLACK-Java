package com.eurodyn.qlack.util.data.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.github.bohnman.squiggly.context.provider.SimpleSquigglyContextProvider;
import com.github.bohnman.squiggly.filter.SquigglyPropertyFilter;
import com.github.bohnman.squiggly.filter.SquigglyPropertyFilterMixin;
import com.github.bohnman.squiggly.parser.SquigglyParser;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Global configuration of Spring's Jackson {@link ObjectMapper} to include
 * https://github.com/bohnman/squiggly-java filters.
 */

@Configuration
public class ReplyFilterBean {

  /**
   * Reconfiguration of Spring's Jackson {@link ObjectMapper} to include
   * https://github.com/bohnman/squiggly-java filters.
   */
  @Bean
  public Jackson2ObjectMapperBuilderCustomizer addCustomBigDecimalDeserialization() {
    return jacksonObjectMapperBuilder -> {
      jacksonObjectMapperBuilder.mixIn(Object.class, SquigglyPropertyFilterMixin.class);
      jacksonObjectMapperBuilder.filters(new SimpleFilterProvider()
        .addFilter(SquigglyPropertyFilter.FILTER_ID, new SquigglyPropertyFilter(
          new SimpleSquigglyContextProvider(new SquigglyParser(), "**"))));
    };
  }
}
