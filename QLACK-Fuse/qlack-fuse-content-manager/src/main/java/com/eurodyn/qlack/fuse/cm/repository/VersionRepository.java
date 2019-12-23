package com.eurodyn.qlack.fuse.cm.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.cm.model.Version;
import org.springframework.stereotype.Repository;

/**
 * @author European Dynamics
 */

@Repository
public interface VersionRepository extends
  QlackBaseRepository<Version, String> {

}
