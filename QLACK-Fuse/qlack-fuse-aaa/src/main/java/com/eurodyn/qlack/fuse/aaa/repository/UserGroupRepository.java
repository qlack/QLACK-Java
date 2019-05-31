package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.stream.Collectors;

@Repository
public interface UserGroupRepository extends AAARepository<UserGroup, String> {

  UserGroup findByName(String name);

  UserGroup findByObjectId(String objectId);

  default Set<String> getAllIds(){

    return findAll().stream()
        .map(UserGroup::getId)
        .collect(Collectors.toSet());
  }

}
