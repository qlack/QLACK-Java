package com.eurodyn.qlack.fuse.fd.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.fd.dto.VoteDTO;
import com.eurodyn.qlack.fuse.fd.exception.VoteException;
import com.eurodyn.qlack.fuse.fd.mapper.VoteMapper;
import com.eurodyn.qlack.fuse.fd.model.QVote;
import com.eurodyn.qlack.fuse.fd.model.Vote;
import com.eurodyn.qlack.fuse.fd.repository.VoteRepository;
import com.eurodyn.qlack.fuse.fd.service.interfaces.ServiceBase;
import com.eurodyn.qlack.fuse.fd.util.Reaction;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

/**
 * A Service class for Key that contains the implementations of crud methods.
 *
 * @author European Dynamics SA
 */
@Transactional
@Service
@Validated
@Log
public class VoteService implements ServiceBase<Vote, VoteDTO> {


  private final VoteRepository repository;

  private final VoteMapper mapper;

  private final JPAQueryFactory queryFactory;

  private static final QVote Q_VOTE = QVote.vote;

  /**
   * Constructor
   */
  @Autowired
  public VoteService(VoteRepository repository, VoteMapper mapper, EntityManager entityManager) {
    this.repository = repository;
    this.mapper = mapper;
    this.queryFactory = new JPAQueryFactory(entityManager);
  }

  /**
   * If vote doesn't exits, creates it. If exact vote exists, deletes it (undo). If vote exists but
   * it is different, change it.
   */
  public VoteDTO addVote(VoteDTO dto) {
    if (voteExists(dto)) {
      return update(dto);
    } else {
      return create(dto);
    }
  }

  /**
   * Finds specific vote.
   *
   * @param voterId voter id.
   * @param threadId thread id
   * @return entity
   */
  public Vote findVote(String voterId, String threadId) {
    Predicate predicate = Q_VOTE.voterId.eq(voterId).and(Q_VOTE.threadMessage.id.eq(threadId));
    return repository.findOne(predicate).orElse(null);
  }


  /**
   * Changes vote
   *
   * @param voteId voter id
   * @param reaction Reaction
   */
  public VoteDTO changeVote(String voteId, Reaction reaction) {
    var vote = findById(voteId);
    vote.setReaction(reaction);
    return update(vote);
  }


  /**
   * Creates a new vote.
   *
   * @param dto the DTO to be mapped to the created entity
   */
  @Override
  public VoteDTO create(VoteDTO dto) {
    log.finest(MessageFormat.format("Adding Vote {0}", dto));
    Vote vote = repository.save(mapper.mapToEntity(dto));
    return mapper.mapToDTO(vote);
  }


  /**
   * checks if a same voted already exists
   */
  private boolean sameVoteExists(VoteDTO dto) {
    Predicate predicate = Q_VOTE.voterId.eq(dto.getVoterId()).and(
        Q_VOTE.threadMessage.id.eq(dto.getThreadId()).and(Q_VOTE.reaction.eq(dto.getReaction())));
    return repository.exists(predicate);
  }

  /**
   * Check if a vote from a voter exists.
   */
  private boolean voteExists(VoteDTO dto) {
    Predicate predicate = Q_VOTE.voterId.eq(dto.getVoterId())
        .and(Q_VOTE.threadMessage.id.eq(dto.getThreadId()));
    return repository.exists(predicate);
  }


  /**
   * @param dto the DTO containing all the information regarding the entity to be updated
   */
  @Override
  public VoteDTO update(VoteDTO dto) {
    if (sameVoteExists(dto)) {
      log.finest(MessageFormat.format("Undo Vote {0}", dto));
      var vote = findVote(dto.getVoterId(), dto.getThreadId());
      if (Objects.nonNull(vote)) {
        delete(vote.getId());
        return null;
      }
    }
    log.finest(MessageFormat.format("Changing Vote {0}", dto));
    Vote vote = repository.save(mapper.mapToEntity(dto));
    return mapper.mapToDTO(vote);
  }


  /**
   * @param id the ID of the entity to be searched
   */
  @Override
  public VoteDTO findById(String id) {
    Vote vote = findResource(id);
    return Optional.ofNullable(vote).map(mapper::mapToDTO).orElseThrow(
        () -> new QDoesNotExistException(MessageFormat.format("No Vote Not Found for {0}", id)));
  }

  /**
   * @param id the ID of the entity to be searched
   */
  @Override
  public Vote findResource(String id) {
    return Objects.nonNull(id) ? repository.findById(id).orElse(null) : null;
  }

  /**
   * @param id the ID of the resource to be deleted
   */
  @Override
  public void delete(String id) {
    repository.deleteById(id);
  }


  /**
   * Finds all votes by predicate
   */
  public List<VoteDTO> findAll(Predicate predicate) {
    return mapper.mapToDTO(repository.findAll(predicate));
  }


  /**
   * Finds all votes by predicate sorted.
   */
  public List<VoteDTO> findAll(Predicate predicate, Sort sort) {
    return mapper.mapToDTO(repository.findAll(predicate, sort));
  }


}
