package com.eurodyn.qlack.fuse.aaa.mapper;

import com.eurodyn.qlack.fuse.aaa.dto.SessionAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.model.SessionAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * A mapper interface for {@link SessionAttribute} object values.
 *
 * @author European Dynmics SA
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SessionAttributeMapper extends
  AAAMapper<SessionAttribute, SessionAttributeDTO> {

  /**
   * Maps a {@link SessionAttribute} Entity to DTO
   *
   * @param sessionAttribute the sessionAttribute Object
   * @return a {@link SessionAttributeDTO} object
   */
  @Override
  @Mapping(source = "session.id", target = "sessionId")
  SessionAttributeDTO mapToDTO(SessionAttribute sessionAttribute);
}
