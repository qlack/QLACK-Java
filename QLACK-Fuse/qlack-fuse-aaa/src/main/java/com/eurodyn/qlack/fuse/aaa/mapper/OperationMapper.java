package com.eurodyn.qlack.fuse.aaa.mapper;

import com.eurodyn.qlack.fuse.aaa.dto.OperationDTO;
import com.eurodyn.qlack.fuse.aaa.model.Operation;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * An OperationMapper  interface that is used to map Operation object values.
 *
 * @author European Dynamics SA
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OperationMapper extends AAAMapper<Operation, OperationDTO> {

}
