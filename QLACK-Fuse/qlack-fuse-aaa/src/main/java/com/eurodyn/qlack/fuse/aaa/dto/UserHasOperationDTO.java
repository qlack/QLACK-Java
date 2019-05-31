package com.eurodyn.qlack.fuse.aaa.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * @author EUROPEAN DYNAMICS SA
 */
@Getter
@Setter
public class UserHasOperationDTO extends BaseDTO {

    private OperationDTO operation;

    private ResourceDTO resource;

    private boolean deny;

}
