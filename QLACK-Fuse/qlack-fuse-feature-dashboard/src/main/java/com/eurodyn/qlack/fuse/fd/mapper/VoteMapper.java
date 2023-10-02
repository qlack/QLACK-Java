package com.eurodyn.qlack.fuse.fd.mapper;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;

import com.eurodyn.qlack.fuse.fd.dto.VoteDTO;
import com.eurodyn.qlack.fuse.fd.model.Vote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

/**
 * An interface Mapper tha is used to map Vote objects to data transfer values.
 *
 * @author European Dynamics SA
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,  nullValueCheckStrategy = ALWAYS,
    uses = {ThreadMessageMapper.class})
public interface VoteMapper extends FeatureDashboardMapper<Vote, VoteDTO> {


  /**
   * Custom mapping for voteDto.
   * @param entity the source entity
   * @return dto the mapped object
   */
  @Override
  @Mapping(target = "threadId", source = "threadMessage.id")
  VoteDTO mapToDTO(Vote entity);

}
