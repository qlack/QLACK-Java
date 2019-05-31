package com.eurodyn.qlack.fuse.cm.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.cm.model.Node;
import org.springframework.stereotype.Repository;

/**
 * @author European Dynamics
 */

@Repository
public interface NodeRepository extends QlackBaseRepository<Node, String> {
}
