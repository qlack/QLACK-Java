package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.dto.OpTemplateDTO;
import com.eurodyn.qlack.fuse.aaa.model.OpTemplate;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
/**
 *  An OpTemplateMapper  interface that is used to map OpTemplate object values.
 *
 *  @author European Dynamics SA
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OpTemplateMapper extends AAAMapper<OpTemplate, OpTemplateDTO> {

}
