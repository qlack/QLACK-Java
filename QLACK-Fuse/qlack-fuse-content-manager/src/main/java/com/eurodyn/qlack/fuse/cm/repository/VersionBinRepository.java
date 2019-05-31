package com.eurodyn.qlack.fuse.cm.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.cm.model.Version;
import com.eurodyn.qlack.fuse.cm.model.VersionBin;
import java.util.List;

/**
 * @author European Dynamics
 */
public interface VersionBinRepository extends QlackBaseRepository {

  List<VersionBin> findByVersionOrderByChunkIndex(Version version);


}
