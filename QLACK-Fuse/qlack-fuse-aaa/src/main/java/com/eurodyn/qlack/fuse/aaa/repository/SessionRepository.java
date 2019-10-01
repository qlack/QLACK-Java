package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * A repository interface for Session.It is used to define a number of abstract crud methods
 *
 * @author European Dynamics
 *
 */
@Repository
public interface SessionRepository extends AAARepository<Session, String> {

  /**
   * A deletion method
   * @param date the date
   */
  @Modifying
  void deleteByCreatedOnBefore(long date);

  /**
   * Retrieves the {@link Session} object by its user id
   * @param userId the user id
   * @param pageable the pageable
   * @return the {@link Session} object
   */
  Page<Session> findByUserId(String userId, Pageable pageable);

  /**
   * Retrieves a List of {@link Session} objects
   * @param date the date
   * @return a {@link Session} object
   */
  List<Session> findByCreatedOnBeforeAndTerminatedOnNull(long date);
}
