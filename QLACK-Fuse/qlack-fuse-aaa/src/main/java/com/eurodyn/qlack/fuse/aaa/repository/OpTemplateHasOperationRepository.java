package com.eurodyn.qlack.fuse.aaa.repository;

import com.eurodyn.qlack.fuse.aaa.model.OpTemplateHasOperation;
import org.springframework.stereotype.Repository;

/**A Repository Interface written for OpTemplateHasOperation. It is used to
 *define a number of crud abstract methods.
 *
 * @author European Dynamics SA
 */
@Repository
public interface OpTemplateHasOperationRepository extends AAARepository<OpTemplateHasOperation, String> {

  /**
   * @param templateId
   * @param operationName
   * @return
   */
  OpTemplateHasOperation findByTemplateIdAndOperationName(String templateId, String operationName);

  /**A method that retrieves an OpTemplateHasOperation object provided
   * by its parameters
   * @param templateId the template id
   * @param resourceId the resource id
   * @param operationName the operation name
   * @return a @{@link OpTemplateHasOperation} object provided by the id
   * of template , the resource id , the operation name
   */
  OpTemplateHasOperation findByTemplateIdAndResourceIdAndOperationName(String templateId,
      String resourceId, String operationName);
}
