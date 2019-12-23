package com.eurodyn.qlack.fuse.cm.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.cm.model.Version;
import com.eurodyn.qlack.fuse.cm.model.VersionBin;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * @author European Dynamics
 */
@Repository
public interface VersionBinRepository extends
  QlackBaseRepository<VersionBin, String> {

  List<VersionBin> findByVersionOrderByChunkIndex(Version version);


}
