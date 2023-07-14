package com.eurodyn.qlack.fuse.cm.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.cm.model.VersionDeleted;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface VersionDeletedRepository extends
  QlackBaseRepository<VersionDeleted, String> {

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Override
  Page findAll(Pageable pageable);

}
