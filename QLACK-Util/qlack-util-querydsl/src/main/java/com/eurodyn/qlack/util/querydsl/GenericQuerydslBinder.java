package com.eurodyn.qlack.util.querydsl;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.dsl.DateTimePath;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;

public interface GenericQuerydslBinder<T extends EntityPath<?>> extends
  QuerydslBinderCustomizer<T> {

  default void addGenericBindings(QuerydslBindings bindings) {
    // Ignore-case for strings.
    bindings.bind(String.class)
      .first(
        (SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);

    // Generic from/to binding for dates.
    bindings.bind(Date.class)
      .all(
        (final DateTimePath<Date> path, final Collection<? extends Date> values) -> {
          final List<? extends Date> dates = new ArrayList<>(values);
          Collections.sort(dates);
          if (dates.size() == 2) {
            return Optional.of(path.between(dates.get(0), dates.get(1)));
          } else {
            return Optional.of(path.eq(dates.get(0)));
          }
        });

    bindings.bind(Instant.class)
      .all(
        (final DateTimePath<Instant> path, final Collection<? extends Instant> values) -> {
          final List<? extends Instant> dates = new ArrayList<>(values);
          Collections.sort(dates);
          if (dates.size() == 2) {
            return Optional.of(path.between(dates.get(0), dates.get(1)));
          } else {
            return Optional.of(path.eq(dates.get(0)));
          }
        });
  }
}
