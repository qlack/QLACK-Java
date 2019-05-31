package com.eurodyn.qlack.fuse.aaa.mappers;

import com.eurodyn.qlack.fuse.aaa.dto.SessionAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.model.SessionAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SessionAttributeMapper extends AAAMapper<SessionAttribute, SessionAttributeDTO> {

  @Override
  @Mapping(source = "session.id", target = "sessionId")
  SessionAttributeDTO mapToDTO(SessionAttribute sessionAttribute);
}
