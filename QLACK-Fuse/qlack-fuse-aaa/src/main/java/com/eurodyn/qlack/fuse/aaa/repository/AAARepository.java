package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.aaa.model.AAAModel;
import com.querydsl.core.types.Predicate;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.NonNull;

/**
 * A repository interface written for AAA.It is used to define a crud abstract
 * methods for AAA model.
 *
 * @param <T> a generic object
 * @param <I> I serializable object
 * @author European Dynamics SA
 */
public interface AAARepository<T extends AAAModel, I extends Serializable>
  extends JpaRepository<T, I>, QuerydslPredicateExecutor<T> {

  /**
   * A method that is used to retrieve a list of generic objects
   *
   * @param predicate the predicate type object
   * @return a List of generic objects
   */
  @NonNull
  List<T> findAll(@NonNull Predicate predicate);

  /**
   * A method that retrieves a List of {@link AAAModel} objects
   *
   * @param predicate the predicate object
   * @param sort a sorting object
   * @return a List of generic objects
   */
  @NonNull
  List<T> findAll(@NonNull Predicate predicate, @NonNull Sort sort);

  /**
   * A method to retrieve the specified id
   *
   * @param id the Serializable id
   * @return the relative id
   */
  default T fetchById(I id) {
    if (id == null) {
      throw new IllegalArgumentException("Null id");
    }
    Optional<T> optional = findById(id);

    return optional.orElseThrow(
      () -> new QDoesNotExistException(MessageFormat
        .format("Entity with Id {0} could not be found.", id)));
  }
}
