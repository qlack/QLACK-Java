package com.eurodyn.qlack.fuse.fd.mapper;

import com.eurodyn.qlack.fuse.fd.InitTestValues;
import com.eurodyn.qlack.fuse.fd.dto.VoteDTO;
import com.eurodyn.qlack.fuse.fd.model.Vote;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class VoteMapperTest {

  @InjectMocks
  private VoteMapperImpl mapper;

  private InitTestValues initTestValues;

  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapToDTOTest() {
    Vote vote = initTestValues.createVote();
    VoteDTO voteDTO = mapper.mapToDTO(vote);

    assertEquals(vote.getThreadMessage().getId(), voteDTO.getThreadId());
    assertEquals(vote.getReaction(), voteDTO.getReaction());
    assertEquals(vote.getVoterId(), voteDTO.getVoterId());
    assertEquals(vote.getId(), voteDTO.getId());
  }

  @Test
  public void mapToDTOListTest() {
    List<Vote> votes = new ArrayList<>();
    votes.add(initTestValues.createVote());
    List<VoteDTO> voteDTOS = mapper.mapToDTO(votes);

    assertEquals(votes.size(), voteDTOS.size());
  }

  @Test
  public void mapToDTONullTest() {
    assertNull(mapper.mapToDTO((Vote) null));

    List<VoteDTO> voteDTOS = mapper.mapToDTO((List<Vote>) null);
    assertNull(voteDTOS);
  }

  @Test
  public void mapToEntityTest() {
    VoteDTO voteDTO = initTestValues.createVoteDTO();
    Vote vote = mapper.mapToEntity(voteDTO);

    assertEquals(voteDTO.getReaction(), vote.getReaction());
//    assertEquals(voteDTO.getThreadId(), vote.getThreadMessage().getId());
    assertEquals(voteDTO.getVoterId(), vote.getVoterId());
    assertEquals(voteDTO.getId(), vote.getId());
  }

  @Test
  public void mapToEntityListTest() {
    List<VoteDTO> voteDTOS = new ArrayList<>();
    voteDTOS.add(initTestValues.createVoteDTO());
    List<Vote> votes = mapper.mapToEntity(voteDTOS);

    assertEquals(voteDTOS.size(), votes.size());
  }

  @Test
  public void mapToEntityNullTest() {
    assertNull(mapper.mapToEntity((VoteDTO) null));

    List<Vote> votes = mapper.mapToEntity((List<VoteDTO>) null);
    assertNull(votes);
  }

}
