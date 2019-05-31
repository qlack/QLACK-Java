package com.eurodyn.qlack.fuse.rules.repository;

import com.eurodyn.qlack.common.repository.QlackBaseRepository;
import com.eurodyn.qlack.fuse.rules.model.KnowledgeBase;
import org.springframework.stereotype.Repository;

/**
 * The Repository interface for the KnowledgeBase entity.
 *
 * @author European Dynamics SA
 */
@Repository
public interface KnowledgeBaseRepository extends QlackBaseRepository<KnowledgeBase, String> {

}
