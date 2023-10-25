package com.eurodyn.qlack.fuse.fd.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.fd.InitTestValues;
import com.eurodyn.qlack.fuse.fd.dto.VoteDTO;
import com.eurodyn.qlack.fuse.fd.mapper.VoteMapperImpl;
import com.eurodyn.qlack.fuse.fd.model.QVote;
import com.eurodyn.qlack.fuse.fd.model.Vote;
import com.eurodyn.qlack.fuse.fd.repository.VoteRepository;
import com.eurodyn.qlack.fuse.fd.util.Reaction;
import com.querydsl.core.types.Predicate;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * @author European Dynamics
 */

@ExtendWith(MockitoExtension.class)
public class VoteServiceTest {

  @InjectMocks
  private VoteService voteService;

  @Mock
  private EntityManager entityManager;

  private final VoteRepository repository = mock(
      VoteRepository.class);

  @Spy
  private VoteMapperImpl mapper;

  @Mock
  private VoteDTO voteDTO;

  @Mock
  private Vote vote;

  private QVote qVote;

  @BeforeEach
  public void init() {
    voteService = new VoteService(repository, mapper, entityManager);

    InitTestValues initTestValues = new InitTestValues();
    voteDTO = initTestValues.createVoteDTO();
    vote = initTestValues.createVote();
    qVote = new QVote("vote");
  }

  @Test
  public void testCreateVote() {
    voteService.create(voteDTO);
    verify(repository, times(1)).save(any());
  }

  @Test
  public void testUpdateVote() {

    when(mapper.mapToEntity(voteDTO)).thenReturn(vote);
    when(mapper.mapToDTO(vote)).thenReturn(voteDTO);

    voteDTO.setReaction(Reaction.DISLIKE);
    when(repository.save(any())).thenReturn(vote);

    VoteDTO updatedVote = voteService.update(voteDTO);
    verify(repository, times(1)).save(vote);

    assertEquals(voteDTO.getId(), updatedVote.getId());
    assertEquals(voteDTO.getReaction(), updatedVote.getReaction());
    assertEquals(voteDTO.getVoterId(), updatedVote.getVoterId());
  }

  @Test
  public void testDeleteVote() {
    voteService.delete(vote.getId());
    verify(repository, times(1)).deleteById(vote.getId());
  }

  @Test
  public void testGetVote() {
    when(repository.findById(voteDTO.getId())).thenReturn(Optional.of(vote));
    when(mapper.mapToDTO(vote)).thenReturn(voteDTO);

    VoteDTO foundVoteDTO = voteService.findById(voteDTO.getId());
    assertEquals(voteDTO, foundVoteDTO);
  }

  @Test
  public void testGetVoteException() {

    assertThrows(QDoesNotExistException.class, () -> voteService.findById("no id"));
  }

  @Test
  public void testFindByNullVoteException() {
    assertThrows(QDoesNotExistException.class, () -> voteService.findById(null));
  }

  @Test
  public void testAddSameVote() {
    VoteDTO newVote = voteService.addVote(voteDTO);
    assertNull(newVote);
  }

  @Test
  public void testAddNewVote() {

    Predicate predicate = qVote.voterId.eq(voteDTO.getVoterId())
        .and(qVote.threadMessage.id.eq(voteDTO.getThreadId()));
    when(repository.exists(predicate)).thenReturn(false);
    when(repository.save(any())).thenReturn(vote);

    VoteDTO newVote = voteService.addVote(voteDTO);
    assertEquals(newVote.getReaction(), voteDTO.getReaction());
  }

  @Test
  public void testChangeDifferentVote() {
    vote.setReaction(Reaction.DISLIKE);
    Predicate predicate = qVote.voterId.eq(voteDTO.getVoterId())
        .and(qVote.threadMessage.id.eq(voteDTO.getThreadId()));
    when(repository.exists(predicate)).thenReturn(true);
    when(repository.save(any())).thenReturn(vote);

    voteDTO.setReaction(Reaction.DISLIKE);
    VoteDTO newVote = voteService.addVote(voteDTO);
    assertEquals(newVote.getReaction(), voteDTO.getReaction());
  }


  @Test
  public void testChangeSameVote() {
    Predicate predicate = qVote.voterId.eq(voteDTO.getVoterId())
        .and(qVote.threadMessage.id.eq(voteDTO.getThreadId()));
    when(repository.exists(predicate)).thenReturn(true);
    voteDTO.setReaction(Reaction.DISLIKE);
    VoteDTO newVote = voteService.addVote(voteDTO);
    assertNull(newVote); //Undo the vote case
  }


  @Test
  public void testChangeVoteById() {
    when(repository.findById(voteDTO.getId())).thenReturn(Optional.of(vote));
    voteDTO.setReaction(Reaction.DISLIKE);
    Predicate sameVotepredicate = qVote.voterId.eq(voteDTO.getVoterId())
        .and(qVote.threadMessage.id.eq(voteDTO.getThreadId())
            .and(qVote.reaction.eq(voteDTO.getReaction())));
    when(repository.exists(sameVotepredicate)).thenReturn(true);
    when(mapper.mapToEntity(voteDTO)).thenReturn(vote);
    when(mapper.mapToDTO(vote)).thenReturn(voteDTO);
    when(repository.save(any())).thenReturn(vote);

    VoteDTO newVote = voteService.changeVote(voteDTO.getId(), voteDTO.getReaction());
    assertNotNull(newVote);
    assertEquals(newVote.getReaction(), voteDTO.getReaction());
  }
}

