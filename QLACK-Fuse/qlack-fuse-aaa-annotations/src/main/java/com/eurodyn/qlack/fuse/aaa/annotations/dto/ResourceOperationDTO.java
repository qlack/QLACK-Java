package com.eurodyn.qlack.fuse.aaa.annotations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an operation/resource pair
 * used for authorization
 * @author European Dynamics
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResourceOperationDTO {
    String operation;
    String resourceId;
}