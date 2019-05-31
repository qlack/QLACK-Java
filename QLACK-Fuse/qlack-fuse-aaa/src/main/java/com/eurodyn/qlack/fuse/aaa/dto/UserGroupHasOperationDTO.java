package com.eurodyn.qlack.fuse.aaa.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGroupHasOperationDTO extends BaseDTO {

  private UserGroupDTO userGroupDTO;
  private OperationDTO operationDTO;
  private ResourceDTO resourceDTO;
  private boolean deny;
}
