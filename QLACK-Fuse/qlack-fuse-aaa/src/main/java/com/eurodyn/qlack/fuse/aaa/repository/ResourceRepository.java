package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.Resource;
import org.springframework.stereotype.Repository;

/**
 * A Repository interface for Resource model. It is used to define a number of
 * abstract crud methods.
 *
 * @author European Dynamics SA
 */
@Repository
public interface ResourceRepository extends AAARepository<Resource, String> {

  /**
   * Finds a specified object id
   *
   * @param objectId the object id
   * @return the specified object id
   */
  Resource findByObjectId(String objectId);
}
