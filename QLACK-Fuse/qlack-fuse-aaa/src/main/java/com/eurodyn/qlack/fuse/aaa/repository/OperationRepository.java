package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.Operation;
import org.springframework.stereotype.Repository;

/**
 * A Repository interface written for Operation . It is used to define abstract crud methods for
 * Operation model.
 *
 * @author European Dynamics SA
 */
@Repository
public interface OperationRepository extends AAARepository<Operation, String> {

  /**
   * Retrieves a name of operation
   *
   * @param name the name
   * @return the specified name
   */
  Operation findByName(String name);

}
