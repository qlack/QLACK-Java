package com.eurodyn.qlack.fuse.mailing.repository;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.mailing.model.MailingModel;
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
 * Generic repository interface for entities of the <tt>qlack-fuse-mailing</tt>
 * module.
 *
 * @param <T> an entity class
 * @param <I> type of the class representing the entity Id
 * @author European Dynamics SA.
 */
public interface MailingRepository<T extends MailingModel, I extends Serializable> extends
  JpaRepository<T, I>, QuerydslPredicateExecutor<T> {

  /**
   * Finds and returns a list of all the predicate eligible <tt>T</tt>
   * entities
   *
   * @param predicate the QueryDsl predicate
   * @return list of eligible <tt>T</tt> entities
   */
  @NonNull
  List<T> findAll(@NonNull Predicate predicate);

  /**
   * Finds and returns a list of all the predicate eligible <tt>T</tt>
   * entities
   *
   * @param predicate a QueryDsl predicate
   * @param sort a {@link Sort} object
   * @return list of eligible <tt>T</tt> entities
   */
  @NonNull
  List<T> findAll(@NonNull Predicate predicate, @NonNull Sort sort);

  /**
   * Finds a <tt>T</tt> entity by its id
   *
   * @param id the entity id
   * @return the <tt>T</tt> entity
   */
  default T fetchById(I id) {
    if (id == null) {
      throw new IllegalArgumentException("Null id");
    }
    Optional<T> optional = findById(id);

    return optional.orElseThrow(
      () -> new QDoesNotExistException(
        MessageFormat.format("Entity with Id {0} could not be found.", id)));
  }
}
