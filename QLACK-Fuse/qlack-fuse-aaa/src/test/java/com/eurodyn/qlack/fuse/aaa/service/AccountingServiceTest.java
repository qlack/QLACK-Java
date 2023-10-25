package com.eurodyn.qlack.fuse.aaa.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.querydsl.core.types.Predicate;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
public class AccountingServiceTest {

  @InjectMocks
  AccountingService accountingService;

  @Mock
  SessionDTO sessionDTO;
  @Mock
  SessionMapper sessionMapper;
  @Mock
  Session session;
  @Mock
  SessionRepository sessionRepository;
  @Mock
  SessionAttribute sessionAttribute;
  @Mock
  SessionAttributeDTO sessionAttributeDTO;
  @Mock
  SessionAttributeRepository sessionAttributeRepository;
  @Mock
  Page<Session> sessionPage;
  @Mock
  SessionAttributeMapper sessionAttributeMapper;
  @Mock
  UserRepository userRepository;
  private List<SessionAttribute> listSessionAttributes;
  private List<Session> listSession;
  private QSession qSession;
  private Collection<String> stringCollection;
  final private Calendar calendar = Calendar.getInstance();
  final private Date date = calendar.getTime();


  @Test
  public void testCreateSession() {
    when(sessionMapper.mapToEntity(sessionDTO)).thenReturn(session);
    when(session.getId()).thenReturn("id");
    when(userRepository.fetchById(any())).thenReturn(any());
    assertEquals(accountingService.createSession(sessionDTO), session.getId());

    when(session.getCreatedOn()).thenReturn(2L);
    accountingService.createSession(sessionDTO);
    assertNotEquals(session.getCreatedOn(), Instant.now().toEpochMilli());
    assertEquals(2L, session.getCreatedOn());

    listSessionAttributes = new ArrayList<>();
    listSessionAttributes.add(sessionAttribute);
    when(session.getSessionAttributes()).thenReturn(listSessionAttributes);
    accountingService.createSession(sessionDTO);
    assertTrue(session.getSessionAttributes().size() > 0);
  }

  @Test
  public void testTerminateSession() {
    when(sessionRepository.findById(any()))
      .thenReturn(java.util.Optional.ofNullable(session));
    accountingService.terminateSession("id");
    verify(sessionRepository, times(1)).findById(any());
  }

  @Test
  public void testTerminateSessionByUserId() {
    when(sessionRepository.findByUserId(any(), any())).thenReturn(sessionPage);
    accountingService.terminateSessionByUserId("id");
    verify(sessionRepository, times(1)).findByUserId(any(), any());
  }

  @Test
  public void getTerminateSessionByApplicationSessionId() {
    qSession = new QSession("session");
    when(sessionRepository.findOne(qSession.applicationSessionId.eq("id")))
      .thenReturn(
        java.util.Optional.ofNullable(session));
    accountingService.terminateSessionByApplicationSessionId("id");
    verify(sessionRepository, times(1))
      .findOne(qSession.applicationSessionId.eq("id"));
  }

  @Test
  public void testGetSessionDurationNull() {
    when(sessionRepository.fetchById(any())).thenReturn(session);
    when(session.getTerminatedOn()).thenReturn(null);
    Long result = accountingService.getSessionDuration("id");
    assertNull(result);
  }

  @Test
  public void testGetSessionDurationNotNull() {
    when(sessionRepository.fetchById(any())).thenReturn(session);
    when(session.getTerminatedOn()).thenReturn(2L);
    Long result = accountingService.getSessionDuration("id");
    assertEquals(2L, result.longValue());
  }

  @Test
  public void testGetSession() {
    when(sessionRepository.fetchById(any())).thenReturn(session);
    when(sessionMapper.mapToDTO(session)).thenReturn(sessionDTO);
    SessionDTO result = accountingService.getSession("id");
    assertNotNull(result);
  }

  @Test
  public void testGetUserLastLogInEmptyList() {
    assertNull(accountingService.getUserLastLogIn("id"));
    verify(sessionRepository, times(1))
      .findAll((Predicate) any(), (Sort) any());
  }

  @Test
  public void testGetUserLastLogIn() {
    queryMock();
    when(sessionRepository.findAll(qSession.user.id.eq("id"),
      Sort.by("createdOn").descending())).thenReturn(listSession);
    assertEquals(2L, accountingService.getUserLastLogIn("id").longValue());
    verify(sessionRepository, times(1)).findAll(qSession.user.id.eq("id"),
      Sort.by("createdOn").descending());
  }

  @Test
  public void testGetUserLastLogOutEmptyList() {
    assertNull(accountingService.getUserLastLogOut("id"));
    verify(sessionRepository, times(1))
      .findAll((Predicate) any(), (Sort) any());
  }

  @Test
  public void testGetUserLastLogOut() {
    queryMock();
    when(sessionRepository.findAll(qSession.user.id.eq("id"),
      Sort.by("terminatedOn").descending())).thenReturn(listSession);
    assertEquals(2L, accountingService.getUserLastLogOut("id").longValue());
    verify(sessionRepository, times(1)).findAll(qSession.user.id.eq("id"),
      Sort.by("terminatedOn").descending());
  }

  @Test
  public void testGetUserLastLogInDurationEmptyList() {
    accountingService.getUserLastLogInDuration("id");
    verify(sessionRepository, times(1))
      .findAll((Predicate) any(), (Sort) any());
  }

  @Test
  public void testGetUserLastLogInDuration() {
    queryMock();
    when(sessionRepository.findAll(qSession.user.id.eq("id"),
      Sort.by("terminatedOn").descending())).thenReturn(listSession);
    assertEquals(session.getTerminatedOn() - session.getCreatedOn(),
      accountingService.getUserLastLogInDuration("id").longValue());
    verify(sessionRepository, times(1)).findAll(qSession.user.id.eq("id"),
      Sort.by("terminatedOn").descending());
  }

  @Test
  public void testGetNoOfTimesUserLoggedIn() {
    accountingService.getNoOfTimesUserLoggedIn("id");
    verify(sessionRepository, times(1)).findAll((Predicate) any());
  }

  @Test
  public void testFilterOnlineUsers() {
    qSession = new QSession(("session"));
    stringCollection = new ArrayList<>();
    accountingService.filterOnlineUsers(stringCollection);
    verify(sessionRepository, times(1)).findAll((Predicate) any());
  }

  @Test
  public void testUpdateAttribute() {
    when(sessionAttributeDTO.getName()).thenReturn("name");
    when(sessionAttributeDTO.getSessionId()).thenReturn("id");
    accountingService.updateAttribute(sessionAttributeDTO, true);
    sessionAttributeRepository.findBySessionIdAndName("id", "name");
    verify(sessionAttributeRepository, times(2)).
      save(sessionAttributeRepository.findBySessionIdAndName("id", "name"));
  }

  @Test
  public void testDeleteAttribute() {
    accountingService.deleteAttribute("id", "name");
    verify(sessionAttributeRepository, times(1))
      .delete(sessionAttributeRepository.findBySessionIdAndName("id", "name"));
  }

  @Test
  public void testGetAttribute() {
    //when(sessionAttributeMapper.mapToDTO(sessionAttribute)).thenReturn(sessionAttributeDTO);
    accountingService.getAttribute("id", "name");
    verify(sessionAttributeRepository, times(1))
      .findBySessionIdAndName("id", "name");
  }

  @Test
  public void testGetSessionIDsForAttributeNullId() {
    qSession = new QSession("session");
    accountingService.getSessionIDsForAttribute(stringCollection, "id", "name");
    verify(sessionRepository, times(1)).findAll((Predicate) any());
  }

  @Test
  public void testGetSessionIDsForAttribute() {
    qSession = new QSession("session");
    stringCollection = new ArrayList<>();
    stringCollection.add("id");
    accountingService.getSessionIDsForAttribute(stringCollection, "id", "name");
    verify(sessionRepository, times(1)).findAll((Predicate) any());
  }

  @Test
  public void testIsAttributeValueUnique() {
    accountingService.isAttributeValueUnique("id", "name", "value");
    verify(sessionRepository, times(1)).findAll((Predicate) any());
  }

  @Test
  public void testDeleteSessionsBeforeDate() {
    accountingService.deleteSessionsBeforeDate(date);
    verify(sessionRepository, times(1)).deleteByCreatedOnBefore(date.getTime());
  }

  @Test
  public void testTerminateSessionsBeforeDate() {
    accountingService.terminateSessionsBeforeDate(date);
    verify(sessionRepository, times(1))
      .findByCreatedOnBeforeAndTerminatedOnNull(date.getTime());
  }

  @Test
  public void testGetSessions() {
    accountingService.getSessions("id", Pageable.unpaged());
    verify(sessionRepository, times(1)).findByUserId(any(), any());
  }

  private void queryMock() {
    listSession = new ArrayList<>();
    qSession = new QSession(("session"));
    when(session.getCreatedOn()).thenReturn(2L);
    listSession.add(session);
  }

}
