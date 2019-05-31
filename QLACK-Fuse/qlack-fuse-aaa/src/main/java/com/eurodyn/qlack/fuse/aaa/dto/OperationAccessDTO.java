package com.eurodyn.qlack.fuse.aaa.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OperationAccessDTO implements Serializable {

  private OperationDTO operation;
  private ResourceDTO resource;
  private boolean deny;

}
