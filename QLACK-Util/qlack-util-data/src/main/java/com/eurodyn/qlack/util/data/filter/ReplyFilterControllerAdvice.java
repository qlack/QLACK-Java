package com.eurodyn.qlack.util.data.filter;

import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.github.bohnman.squiggly.context.provider.SimpleSquigglyContextProvider;
import com.github.bohnman.squiggly.filter.SquigglyPropertyFilter;
import com.github.bohnman.squiggly.parser.SquigglyParser;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.AbstractMappingJacksonResponseBodyAdvice;

/**
 * A {@link ControllerAdvice} to process {@link ReplyFilter} annotations.
 */
@ControllerAdvice
public class ReplyFilterControllerAdvice extends AbstractMappingJacksonResponseBodyAdvice {

  private final static String SPRING_PAGE_DEFAULT_FIELDS =
    "first,last,number,numberOfElements,pageable,size,sort,totalElements,totalPages";

  @Override
  protected void beforeBodyWriteInternal(MappingJacksonValue bodyContainer, MediaType contentType,
    MethodParameter returnType, ServerHttpRequest req, ServerHttpResponse res) {
    // The value of the filter to apply.
    String filterValue = null;

    // Check what type of filter annotation is set and obtain the value of the filter.
    if (returnType.getMethodAnnotation(ReplyFilter.class) != null) {
      filterValue = returnType.getMethodAnnotation(ReplyFilter.class).value();
    } else if (returnType.getMethodAnnotation(ReplyPageableFilter.class) != null) {
      filterValue =
        SPRING_PAGE_DEFAULT_FIELDS + ",content["
          + returnType.getMethodAnnotation(ReplyPageableFilter.class).value()
          + "]";
    }

    // If a filter annotation was found apply the filter.
    if (filterValue != null) {
      SquigglyPropertyFilter propertyFilter = new SquigglyPropertyFilter(
        new SimpleSquigglyContextProvider(new SquigglyParser(), filterValue));
      final SimpleFilterProvider filters = new SimpleFilterProvider()
        .addFilter(SquigglyPropertyFilter.FILTER_ID, propertyFilter);
      bodyContainer.setFilters(filters);
    }
  }
}
