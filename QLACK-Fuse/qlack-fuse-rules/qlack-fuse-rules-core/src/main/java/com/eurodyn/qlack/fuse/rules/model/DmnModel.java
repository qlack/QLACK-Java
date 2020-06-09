package com.eurodyn.qlack.fuse.rules.model;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * The DMN model entity, that holds the data of a Camunda DMN model.
 *
 * @author European Dynamics SA
 */
@Entity
@Table(name = "dmn_models")
@Getter
@Setter
@NoArgsConstructor
public class DmnModel extends QlackBaseModel {

    /**
     * the model file
     */
    @Lob
    private byte[] modelInstance;

}
