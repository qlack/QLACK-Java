package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.SessionAttribute;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionAttributeRepository extends CrudRepository<SessionAttribute, String> {
  SessionAttribute findBySessionIdAndName(String sessionId, String name);
}
