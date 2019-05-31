package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.Operation;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends AAARepository<Operation, String> {

  Operation findByName(String name);

}
