package com.eurodyn.qlack.fuse.rules.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.rules.model.DmnModel;
import org.springframework.stereotype.Repository;

/**
 * The Repository interface for the DmnModel entity.
 *
 * @author European Dynamics SA
 */
@Repository
public interface DmnModelRepository extends
        QlackBaseRepository<DmnModel, String> {
}
