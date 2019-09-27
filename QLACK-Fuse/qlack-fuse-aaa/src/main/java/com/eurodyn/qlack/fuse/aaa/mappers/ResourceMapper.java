package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.dto.ResourceDTO;
import com.eurodyn.qlack.fuse.aaa.model.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * A ResourceMapper interface that is used to map the Resource object values
 * @author European Dynamics SA
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ResourceMapper extends AAAMapper<Resource, ResourceDTO> {

}
