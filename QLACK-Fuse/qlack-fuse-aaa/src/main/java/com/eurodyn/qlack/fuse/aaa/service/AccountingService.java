package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.aaa.dto.SessionAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.SessionDTO;
import com.eurodyn.qlack.fuse.aaa.mapper.SessionAttributeMapper;
import com.eurodyn.qlack.fuse.aaa.mapper.SessionMapper;
import com.eurodyn.qlack.fuse.aaa.model.QSession;
import com.eurodyn.qlack.fuse.aaa.model.Session;
import com.eurodyn.qlack.fuse.aaa.model.SessionAttribute;
import com.eurodyn.qlack.fuse.aaa.repository.SessionAttributeRepository;
import com.eurodyn.qlack.fuse.aaa.repository.SessionRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * Provides accounting information for the user. For details regarding the
 * functionality offered see the respective interfaces.
 *
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional
public class AccountingService {

  // JUL reference.
  private static final Logger LOGGER = Logger
    .getLogger(AccountingService.class.getName());

  // QuertyDSL helpers.
  private static QSession qSession = QSession.session;

  // Repositories
  private final SessionRepository sessionRepository;
  private final UserRepository userRepository;
  private final SessionAttributeRepository sessionAttributeRepository;
  // Mappers
  private final SessionMapper sessionMapper;
  private final SessionAttributeMapper sessionAttributeMapper;

  @Autowired
  public AccountingService(SessionRepository sessionRepository,
    SessionAttributeRepository sessionAttributeRepository,
    SessionMapper sessionMapper, UserRepository userRepository,
    SessionAttributeMapper sessionAttributeMapper) {
    this.sessionRepository = sessionRepository;
    this.sessionAttributeRepository = sessionAttributeRepository;
    this.sessionMapper = sessionMapper;
    this.userRepository = userRepository;
    this.sessionAttributeMapper = sessionAttributeMapper;
  }

  /**
   * Creation method for Session
   *
   * @param sessionDTO the session DTO
   * @return the session id
   */
  public String createSession(SessionDTO sessionDTO) {
    Session entity = sessionMapper.mapToEntity(sessionDTO);
    if (entity.getCreatedOn() == 0) {
      entity.setCreatedOn(Instant.now().toEpochMilli());
    }
    entity.setUser(userRepository.fetchById(sessionDTO.getUserId()));
    if (entity.getSessionAttributes() != null) {
      for (SessionAttribute attribute : entity.getSessionAttributes()) {
        attribute.setSession(entity);
      }
    }
    sessionRepository.save(entity);
    return entity.getId();
  }

  /**
   * A termination method for Session
   *
   * @param sessionID the session id
   */
  public void terminateSession(String sessionID) {
    final Optional<Session> session = sessionRepository.findById(sessionID);
    if (session.isPresent()) {
      session.get().setTerminatedOn(Instant.now().toEpochMilli());
    } else {
      LOGGER
        .log(Level.WARNING,
          "Requested to terminate a session that does not exist, session ID: {0}",
          sessionID);
    }
  }

  /**
   * Terminates all sessions of the given user.
   *
   * @param userId The user id to terminate the sessions of.
   */
  public void terminateSessionByUserId(String userId) {
    final Page<Session> sessions = sessionRepository
      .findByUserId(userId, Pageable.unpaged());
    sessions.get().forEach(o -> {
      o.setTerminatedOn(Instant.now().toEpochMilli());
      sessionRepository.save(o);
    });
  }

  /**
   * Terminate the session by its application session id
   *
   * @param applicationSessionId the application session id
   */
  public void terminateSessionByApplicationSessionId(
    String applicationSessionId) {
    Predicate predicate = qSession.applicationSessionId
      .eq(applicationSessionId);
    final Session session = sessionRepository.findOne(predicate)
      .orElseThrow(() -> new QDoesNotExistException(MessageFormat
        .format(
          "Session with application session Id {0} could not be found to be terminated.",
          applicationSessionId)));

    terminateSession(session.getId());
  }

  /**
   * Retrieves the session
   *
   * @param sessionID the session id
   * @return the session
   */
  public SessionDTO getSession(String sessionID) {

    return sessionMapper.mapToDTO(findSession(sessionID));
  }

  /**
   * Retrieves the duration of Session
   *
   * @param sessionID the session id
   * @return the duration of session
   */
  public Long getSessionDuration(String sessionID) {
    Session session = findSession(sessionID);
    if (session.getTerminatedOn() == null) {
      return null;
    }

    return session.getTerminatedOn() - session.getCreatedOn();
  }

  /**
   * Retrieves the last login of the user in the app
   *
   * @param userID the userId
   * @return when the last login of the user was
   */
  public Long getUserLastLogIn(String userID) {
    Predicate predicate = qSession.user.id.eq(userID);
    List<Session> queryResult = sessionRepository
      .findAll(predicate, Sort.by("createdOn").descending());
    if (queryResult.isEmpty()) {
      return null;
    }

    return queryResult.get(0).getCreatedOn();
  }

  /**
   * Retrieves tha last logout of the user in the app
   *
   * @param userID the userId
   * @return the last logout of the user
   */
  public Long getUserLastLogOut(String userID) {
    Predicate predicate = qSession.user.id.eq(userID);
    List<Session> queryResult = sessionRepository
      .findAll(predicate, Sort.by("terminatedOn").descending());
    if (queryResult.isEmpty()) {
      return null;
    }

    return queryResult.get(0).getCreatedOn();
  }

  /**
   * Retrieves tha last login duration
   *
   * @param userID the userId
   * @return the last login duration
   */
  public Long getUserLastLogInDuration(String userID) {
    Predicate predicate = qSession.user.id.eq(userID);
    List<Session> queryResult = sessionRepository
      .findAll(predicate, Sort.by("terminatedOn").descending());
    if (queryResult.isEmpty() || (queryResult.get(0).getTerminatedOn()
      == null)) {
      return null;
    }
    Session session = queryResult.get(0);

    return session.getTerminatedOn() - session.getCreatedOn();
  }

  /**
   * Retrieves the number of times the user has logged in
   *
   * @param userID the userId
   * @return the number of times the user has logged in the app
   */
  public long getNoOfTimesUserLoggedIn(String userID) {
    Predicate predicate = qSession.user.id.eq(userID);

    return (long) sessionRepository.findAll(predicate).size();
  }

  /**
   * Retrieves online users
   *
   * @param userIDs the userIds
   * @return the filtered online users
   */
  public Set<String> filterOnlineUsers(Collection<String> userIDs) {
    Predicate predicate = qSession.terminatedOn.isNull()
      .and(qSession.user.id.in(userIDs));

    return sessionRepository.findAll(predicate).stream()
      .map(session -> session.getUser().getId())
      .collect(Collectors.toSet());
  }


  /**
   * Update the session attribute
   *
   * @param attribute the session attribute
   * @param createIfMissing checking value create if missing
   */
  public void updateAttribute(SessionAttributeDTO attribute,
    boolean createIfMissing) {
    Collection<SessionAttributeDTO> attributes = new ArrayList<>(1);
    attributes.add(attribute);
    updateAttributes(attributes, createIfMissing);
  }

  /**
   * Update a collections of {@link SessionAttributeDTO} objects
   *
   * @param attributes a collection of {@link SessionAttributeDTO} objects
   * @param createIfMissing checking value to create if missing
   */
  public void updateAttributes(Collection<SessionAttributeDTO> attributes,
    boolean createIfMissing) {
    for (SessionAttributeDTO attributeDTO : attributes) {
      SessionAttribute attribute = sessionAttributeRepository
        .findBySessionIdAndName(
          attributeDTO.getSessionId(), attributeDTO.getName());
      if ((attribute == null) && createIfMissing) {
        attribute = new SessionAttribute();
        attribute.setName(attributeDTO.getName());
        attribute.setSession(findSession(attributeDTO.getSessionId()));
      }

      if (attribute != null) {
        attribute.setValue(attributeDTO.getValue());
        sessionAttributeRepository.save(attribute);
      }
    }
  }

  /**
   * Deletes a session attribute
   *
   * @param sessionID the session Id
   * @param attributeName the attributeName
   */
  public void deleteAttribute(String sessionID, String attributeName) {
    SessionAttribute attribute = sessionAttributeRepository
      .findBySessionIdAndName(sessionID, attributeName);
    sessionAttributeRepository.delete(attribute);
  }

  /**
   * Retrieves @{@link SessionAttributeDTO} object
   *
   * @param sessionID the session id
   * @param attributeName the attributeName
   * @return a @{@link SessionAttributeDTO} object
   */
  public SessionAttributeDTO getAttribute(String sessionID,
    String attributeName) {

    return sessionAttributeMapper.mapToDTO(
      sessionAttributeRepository
        .findBySessionIdAndName(sessionID, attributeName));
  }

  /**
   * Retrieves the session ids for attribute
   *
   * @param sessionIDs the sessionIDs
   * @param attributeName the attributeName
   * @param attributeValue the attribute value
   * @return the session ids for attribute
   */
  public Set<String> getSessionIDsForAttribute(Collection<String> sessionIDs,
    String attributeName, String attributeValue) {
    Predicate predicate = qSession.sessionAttributes.any().name
      .eq(attributeName)
      .and(qSession.sessionAttributes.any().value.eq(attributeValue));
    if (sessionIDs != null) {
      BooleanBuilder builder = new BooleanBuilder();
      for (String id : sessionIDs) {
        builder.or(qSession.id.eq(id));
      }
      predicate = ((BooleanExpression) predicate).and(builder);
    }

    return sessionRepository.findAll(predicate).stream()
      .map(session -> session.getUser().getId())
      .collect(Collectors.toSet());
  }

  /**
   * Check if attribute value is unique
   *
   * @param userId the userId
   * @param attributeName the attributeName
   * @param attributeValue the attributeValue
   * @return a @{@link Boolean} check whether attribute is unique or not
   */
  public boolean isAttributeValueUnique(String userId, String attributeName,
    String attributeValue) {
    Predicate predicate = qSession.sessionAttributes.any().name
      .eq(attributeName)
      .and(qSession.sessionAttributes.any().value.eq(attributeValue))
      .and(qSession.user.id.eq(userId));

    return sessionRepository.findAll(predicate).isEmpty();
  }

  /**
   * Deletes the session before given date
   *
   * @param date the date
   */
  public void deleteSessionsBeforeDate(Date date) {
    sessionRepository.deleteByCreatedOnBefore(date.getTime());
  }

  /**
   * Terminates the session before given date
   *
   * @param date the date
   */
  public void terminateSessionsBeforeDate(Date date) {
    final List<Session> sessions = sessionRepository
      .findByCreatedOnBeforeAndTerminatedOnNull(
        date.getTime());
    sessions.forEach(o -> terminateSession(o.getId()));
  }

  /**
   * Retrieves sessions
   *
   * @param userId the userId
   * @param pageable the pageable
   * @return the sessions
   */
  public Page<SessionDTO> getSessions(String userId, Pageable pageable) {
    return sessionMapper
      .fromSessions(sessionRepository.findByUserId(userId, pageable));
  }

  /**
   * Finds {@link Session} object
   *
   * @param sessionId the sessionId
   * @return the session
   */
  private Session findSession(String sessionId) {

    return sessionRepository.fetchById(sessionId);
  }

}
