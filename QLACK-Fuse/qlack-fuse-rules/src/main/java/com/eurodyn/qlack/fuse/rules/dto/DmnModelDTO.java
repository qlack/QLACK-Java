package com.eurodyn.qlack.fuse.rules.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO that represents the DmnModel entity and holds the data of a Camunda
 * Dmn model.
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
@NoArgsConstructor
public class DmnModelDTO {

    /**
     * the id
     */
    private String id;

    /**
     * the model file
     */
    private String modelInstance;
}
