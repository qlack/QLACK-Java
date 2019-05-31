package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAttributeRepository extends AAARepository<UserAttribute, String> {

  UserAttribute findByUserIdAndName(String userId, String name);
}
