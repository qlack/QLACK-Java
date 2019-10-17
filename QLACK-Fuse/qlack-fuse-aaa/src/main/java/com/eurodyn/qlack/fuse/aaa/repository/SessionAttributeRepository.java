package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.SessionAttribute;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * A session Attribute Repository interface written to define a number of crud methods for the
 * SessionAttribute model.
 *
 * @author European Dynamics SA
 */
@Repository
public interface SessionAttributeRepository extends CrudRepository<SessionAttribute, String> {

  /**
   * A retrieve method to find the @{@link SessionAttribute} object
   *
   * @param sessionId the id of Session
   * @param name the name
   * @return the {@link SessionAttribute} object by its name and the id
   */
  SessionAttribute findBySessionIdAndName(String sessionId, String name);
}
