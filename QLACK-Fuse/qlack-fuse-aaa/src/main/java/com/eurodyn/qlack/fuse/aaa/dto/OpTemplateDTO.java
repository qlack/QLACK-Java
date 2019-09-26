package com.eurodyn.qlack.fuse.aaa.dto;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;

/**A simple DTO that is used to retrieve and save data of OpTemplate object
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
public class OpTemplateDTO extends BaseDTO {

  /**
   * the name
   */
  private String name;
  /**
   * a brief description of opTemplate
   */
  private String description;
  /**
   * the created by date
   */
  private String createdBy;
  /**
   * a set of operations
   */
  private Set<OperationAccessDTO> operations;

}
