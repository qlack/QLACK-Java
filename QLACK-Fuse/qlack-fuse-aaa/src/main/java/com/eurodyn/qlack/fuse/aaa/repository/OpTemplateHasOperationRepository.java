package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.OpTemplateHasOperation;
import org.springframework.stereotype.Repository;

@Repository
public interface OpTemplateHasOperationRepository extends AAARepository<OpTemplateHasOperation, String> {

  OpTemplateHasOperation findByTemplateIdAndOperationName(String templateId, String operationName);

  OpTemplateHasOperation findByTemplateIdAndResourceIdAndOperationName(String templateId,
      String resourceId, String operationName);
}
