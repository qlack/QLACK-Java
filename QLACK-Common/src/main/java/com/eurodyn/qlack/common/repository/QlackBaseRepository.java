package com.eurodyn.qlack.common.repository;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.common.exception.QValueIsRequiredException;
import com.eurodyn.qlack.common.model.QlackBaseModel;
import com.querydsl.core.types.Predicate;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

/**
 * The base Repository interface that includes some common JPA methods.
 *
 * @param <T> the entity class
 * @param <I> the class type of the identifier
 * @author European Dynamics SA
 */
@Repository
public interface QlackBaseRepository<T extends QlackBaseModel, I extends Serializable>
  extends JpaRepository<T, I>, QuerydslPredicateExecutor<T> {

  @NonNull
  List<T> findAll(@NonNull Predicate predicate);

  @NonNull
  List<T> findAll(@NonNull Predicate predicate, @NonNull Sort sort);

  default T fetchById(I id) {
    if (id == null) {
      throw new QValueIsRequiredException("Id is required to fetch an entity.");
    }
    Optional<T> optional = findById(id);

    return optional.orElseThrow(
      () -> new QDoesNotExistException(MessageFormat
        .format("Entity with id {0} could not be found.", id)));
  }
}
