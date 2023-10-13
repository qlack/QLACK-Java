package com.eurodyn.qlack.fuse.fd.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.fd.dto.ThreadMessageDTO;
import com.eurodyn.qlack.fuse.fd.mapper.ThreadMessageMapper;
import com.eurodyn.qlack.fuse.fd.model.QThreadMessage;
import com.eurodyn.qlack.fuse.fd.model.ThreadMessage;
import com.eurodyn.qlack.fuse.fd.repository.ThreadMessageRepository;
import com.eurodyn.qlack.fuse.fd.service.interfaces.ServiceBase;
import com.eurodyn.qlack.fuse.fd.util.ThreadStatus;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


/**
 * A Service class for Key that contains the implementations of crud methods .
 *
 * @author European Dynamics SA
 */
@Transactional
@Service
@Validated
@Log
public class ThreadService implements ServiceBase<ThreadMessage, ThreadMessageDTO> {

  private final ThreadMessageRepository repository;
  private final ThreadMessageMapper mapper;

  private final JPAQueryFactory queryFactory;


  private static final QThreadMessage Q_THREAD_MESSAGE = QThreadMessage.threadMessage;

  /**
   * Constructor
   */
  public ThreadService(ThreadMessageRepository repository, ThreadMessageMapper mapper,
      EntityManager entityManager) {
    this.repository = repository;
    this.mapper = mapper;
    this.queryFactory = new JPAQueryFactory(entityManager);
  }

  /**
   * @param dto the DTO to be mapped to the created entity
   * @return upated ThreadMessageDTO
   */
  @Override
  public ThreadMessageDTO create(ThreadMessageDTO dto) {
    log.finest(MessageFormat.format("Creating Thread message {0}", dto));
    ThreadMessage threadMessage = repository.save(mapper.mapToEntity(dto));
    return mapper.mapToDTO(threadMessage);
  }

  /**
   * @param dto the DTO containing all the information regarding the entity to be updated
   * @return the updated ThreadMessageDTO
   */
  @Override
  public ThreadMessageDTO update(ThreadMessageDTO dto) {
    log.finest(MessageFormat.format("Updating Thread message {0}", dto));
    ThreadMessage threadMessage = repository.save(mapper.mapToEntity(dto));
    return mapper.mapToDTO(threadMessage);
  }

  /**
   * Ownership Mask setter
   */
  public ThreadMessageDTO setOwnershipMask(String threadId, String pattern) {
    ThreadMessage tm = findResource(threadId);
    tm.setOwnershipMask(pattern);
    return mapper.mapToDTO(repository.save(tm));
  }

  /**
   * Ownership Mask getter
   */
  public String getOwnershipMask(String threadId) {
    return queryFactory.selectFrom(Q_THREAD_MESSAGE)
        .select(Q_THREAD_MESSAGE.ownershipMask)
        .where(Q_THREAD_MESSAGE.id.eq(threadId))
        .fetchOne();
  }


  /**
   * Attribute Mask setter
   */
  public ThreadMessageDTO setAttributesMask(String threadId, String pattern) {
    ThreadMessage tm = findResource(threadId);
    tm.setAttributesMask(pattern);
    return mapper.mapToDTO(repository.save(tm));
  }


  /**
   * Attribute Mask getter
   */
  public String getAttributesMask(String threadId) {
    return queryFactory.selectFrom(Q_THREAD_MESSAGE)
        .select(Q_THREAD_MESSAGE.attributesMask)
        .where(Q_THREAD_MESSAGE.id.eq(threadId))
        .fetchOne();
  }


  /**
   * @param id the ID of the ThreadMessage to be searched
   */
  @Override
  public ThreadMessageDTO findById(String id) {
    return Optional.ofNullable(findResource(id)).map(mapper::mapToDTO).orElseThrow(
        () -> new QDoesNotExistException(MessageFormat.format("No Vote Not Found for {0}", id)));
  }

  /**
   * @param id the ID of the ThreadMessage to be searched
   */
  @Override
  public ThreadMessage findResource(String id) {
    return Objects.nonNull(id) ? repository.findById(id).orElse(null) : null;
  }

  /**
   * @param id the ID of the resource to be deleted. Hence the tree hierarchy all children of
   * message will deleted as well.
   */
  @Override
  public void delete(String id) {
    log.finest(MessageFormat.format("Deleting Thread message {0}", id));
    repository.deleteById(id);
  }

  /**
   * @param id the ID of the resource to be Soft deleted
   * @param deleteChildren whether the children shall be soft deleted or not.
   */
  public void deleteMessage(String id, boolean deleteChildren) {
    log.finest(MessageFormat.format("Soft Deleting  of Thread message {0}", id));
    setThreadStatus(id, deleteChildren, ThreadStatus.DELETED);
  }


  /**
   * Changes the Status of a Thread and or of a Thread message tree.
   *
   * @param threadMessageId threadMessage id
   * @param alsoChildren weather the status of given's id children
   * @param status the new status.
   */
  public void setThreadStatus(String threadMessageId, boolean alsoChildren, ThreadStatus status) {
    ThreadMessage threadMessage = findResource(threadMessageId);
    log.finest(MessageFormat.format("Changing Thread message {0} to status {1}", threadMessageId,
        status.getName()));
    threadMessage.setStatus(status);
    repository.save(threadMessage);
    if (alsoChildren) {
      List<ThreadMessage> childThreadMessages = findChildrenThreads(threadMessage);
      childThreadMessages.forEach(
          childMessage -> {
            log.finest(MessageFormat.format("Changing Thread message {0} to status {1}",
                childMessage.getId(),
                status.getName()));
            childMessage.setStatus(status);
          }
      );
      repository.saveAll(childThreadMessages);
    }
  }


  /**
   * Finds all available ThreadMessageDTO
   */
  public List<ThreadMessageDTO> findAll() {
    return mapper.mapToDTO(repository.findAll());
  }

  /**
   * Finds all available ThreadMessageDTO based on predicate
   */
  public List<ThreadMessageDTO> findAll(Predicate predicate) {
    return mapper.mapToDTO(repository.findAll(predicate));
  }

  /**
   * Finds all sortable ThreadMessageDTO based on predicate
   */
  public List<ThreadMessageDTO> findAll(Predicate predicate, Sort sort) {
    return mapper.mapToDTO(repository.findAll(predicate, sort));
  }

  /**
   * Finds the Page of ThreadMessageDTOs based on predicate
   */
  public Page<ThreadMessageDTO> findAll(Predicate predicate, Pageable pageable) {
    return mapper.map(repository.findAll(predicate, pageable));
  }

  /**
   * Finds the Page of NON soft deleted ThreadMessageDTOs based
   */
  public Page<ThreadMessageDTO> findAllActive(Pageable pageable) {
    Predicate predicate = Q_THREAD_MESSAGE.status.ne(ThreadStatus.DELETED);
    return mapper.map(repository.findAll(predicate, pageable));
  }


  /**
   * Finds All Messages By Author
   */
  public List<ThreadMessageDTO> findAllMessagesByAuthor(String author) {
    Predicate predicate = Q_THREAD_MESSAGE.author.eq(author);
    return findAll(predicate);
  }

  /**
   * Finds All Threads (Top level) By Author
   */
  public List<ThreadMessageDTO> findThreadsByAuthor(String author) {
    Predicate predicate = Q_THREAD_MESSAGE.author.eq(author)
        .and(Q_THREAD_MESSAGE.parentThreadMessage.isNull());
    return findAll(predicate);
  }


  /**
   * Finds messages by dates
   */
  public List<ThreadMessageDTO> findByDate(Instant from, Instant to, @NonNull Sort sort) {
    Predicate predicate = Q_THREAD_MESSAGE.createdOn.after(from)
        .and(Q_THREAD_MESSAGE.createdOn.before(to));
    return findAll(predicate, sort);
  }


  /**
   * Retrieves only Threads (parental messages)
   */
  public List<ThreadMessageDTO> findMainThreads() {
    Predicate predicate = Q_THREAD_MESSAGE.parentThreadMessage.isNull();
    return findAll(predicate);
  }

  /**
   * Finds All messages with body.
   */
  public List<ThreadMessageDTO> findThreadsWithComments() {
    Predicate predicate = Q_THREAD_MESSAGE.body.isNotNull();
    return findAll(predicate);
  }


  /**
   * Finds all messages by status
   */
  public List<ThreadMessageDTO> findThreadsByStatus(ThreadStatus... statues) {
    Predicate predicate = Q_THREAD_MESSAGE.status.in(statues);
    return findAll(predicate);
  }


  /**
   * Retrieves all the body texts of a Thread.
   */
  public List<String> getThreadTextMessages(String id) {

    var rootMessage = findRoot(findResource(id));
    if (Objects.isNull(rootMessage)) {
      return Collections.emptyList();
    }
    List<ThreadMessage> messages = new ArrayList<>();
    messages.add(rootMessage);
    messages.addAll(findChildrenThreads(rootMessage));
    return messages.stream().map(ThreadMessage::getBody).toList();
  }


  /**
   * Finds Root message, head of a Thread
   */
  public ThreadMessage findRoot(ThreadMessage child) {
    if (Objects.isNull(child)) {
      return null;
    }
    ThreadMessage parent = child.getParentThreadMessage();
    while (parent != null) {
      child = parent;
      parent = child.getParentThreadMessage();
    }
    return child;
  }

  /**
   * Finds Root message, head of a Thread
   */
  public ThreadMessage findRootThreadMessage(ThreadMessage childThreadMessage) {
    // If the given ThreadMessage has no parent, it is the root
    if (childThreadMessage.getParentThreadMessage() == null) {
      return childThreadMessage;
    } else {
      // Recursively traverse up the parent chain to find the root
      return findRootThreadMessage(childThreadMessage.getParentThreadMessage());
    }
  }

  /**
   * Finds child messages for a given parent
   */
  public List<ThreadMessage> findChildrenThreads(ThreadMessage parent) {
    Predicate isChild = Q_THREAD_MESSAGE.parentThreadMessage.eq(parent);
    List<ThreadMessage> childMessages = queryFactory.selectFrom(Q_THREAD_MESSAGE).where(isChild)
        .orderBy(Q_THREAD_MESSAGE.createdOn.desc()).fetch();

    // Recursively load children for each child
    for (ThreadMessage child : childMessages) {
      findChildrenThreads(child);
    }
    parent.setChildThreadMessages(childMessages);
    return childMessages;
  }

  /**
   * Retrieves all the body texts of a Thread Pageable.
   */
  public Page<String> getThreadMessagesPaged(String id, Pageable pageable) {
    var rootMessage = findRoot(findResource(id));
    List<ThreadMessage> messages = new ArrayList<>();
    messages.add(rootMessage);
    messages.addAll(findChildrenThreads(rootMessage));
    return getPage(pageable.getPageNumber(), pageable.getPageSize(),
        messages.stream().map(ThreadMessage::getBody).toList());
  }
}
