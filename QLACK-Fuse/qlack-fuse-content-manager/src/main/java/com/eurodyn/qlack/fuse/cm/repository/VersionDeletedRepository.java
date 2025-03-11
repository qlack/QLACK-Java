package com.eurodyn.qlack.fuse.cm.repository;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.common.exception.QValueIsRequiredException;
import com.eurodyn.qlack.fuse.cm.model.VersionDeleted;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.Optional;

@Repository
public interface VersionDeletedRepository extends JpaRepository<VersionDeleted, String>, QuerydslPredicateExecutor<VersionDeleted> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  Page findAll(Pageable pageable);

  default VersionDeleted fetchById(String id) {
    if (id == null) {
      throw new QValueIsRequiredException("Id is required to fetch an entity.");
    }
    Optional<VersionDeleted> optional = findById(id);

    return optional.orElseThrow(
            () -> new QDoesNotExistException(MessageFormat
                    .format("Entity with id {0} could not be found.", id)));
  }
}
