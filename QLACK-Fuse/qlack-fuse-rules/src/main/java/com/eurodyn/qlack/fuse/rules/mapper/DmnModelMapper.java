package com.eurodyn.qlack.fuse.rules.mapper;

import com.eurodyn.qlack.fuse.rules.dto.DmnModelDTO;
import com.eurodyn.qlack.fuse.rules.model.DmnModel;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapping interface for <tt>DmnModel</tt> entities and DTOs
 *
 * @author European Dynamics SA.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DmnModelMapper extends
        RulesMapper<DmnModel, DmnModelDTO> {
}
