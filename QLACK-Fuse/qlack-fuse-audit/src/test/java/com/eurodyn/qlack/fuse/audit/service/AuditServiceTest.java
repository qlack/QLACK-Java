package com.eurodyn.qlack.fuse.audit.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.audit.InitTestValues;
import com.eurodyn.qlack.fuse.audit.dto.AuditDTO;
import com.eurodyn.qlack.fuse.audit.exception.QAuditException;
import com.eurodyn.qlack.fuse.audit.mapper.AuditMapper;
import com.eurodyn.qlack.fuse.audit.model.Audit;
import com.eurodyn.qlack.fuse.audit.model.QAudit;
import com.eurodyn.qlack.fuse.audit.repository.AuditLevelRepository;
import com.eurodyn.qlack.fuse.audit.repository.AuditRepository;
import com.eurodyn.qlack.fuse.audit.util.AuditProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author European Dynamics
 */
@RunWith(MockitoJUnitRunner.class)
public class AuditServiceTest {

  @InjectMocks
  private AuditService auditService;

  private AuditRepository auditRepository = mock(AuditRepository.class);
  private AuditLevelRepository auditLevelRepository = mock(AuditLevelRepository.class);
  private AuditProperties auditProperties = mock(AuditProperties.class);

  @Spy
  private AuditMapper auditMapper;

  private InitTestValues initTestValues;
  private QAudit qAudit;

  private Audit audit;
  private AuditDTO auditDTO;
  private List<AuditDTO> auditsDTO;
  private List<Audit> audits;

  @Mock
  private ObjectMapper objectMapper;

  @Before
  public void init() {
    auditService = new AuditAsyncService(auditProperties, auditRepository, auditMapper,
        auditLevelRepository);
    initTestValues = new InitTestValues();
    audit = initTestValues.createAudit();
    auditDTO = initTestValues.createAuditDTO();
    auditsDTO = initTestValues.createAuditsDTO();
    audits = initTestValues.createAudits();
    qAudit = new QAudit("audit");
  }

  @Test
  public void auditWith3ParametersTest() {
    when(auditMapper.mapToEntity(any(AuditDTO.class))).thenReturn(audit);
    auditService.audit(auditDTO.getLevel(), auditDTO.getEvent(),
        auditDTO.getShortDescription());

    verify(auditRepository, times(1)).save(audit);
  }

  @Test
  public void auditWith4ParametersTest() {
    when(auditMapper.mapToEntity(any(AuditDTO.class))).thenReturn(audit);
    auditService.audit(auditDTO.getLevel(), auditDTO.getEvent(),
        auditDTO.getShortDescription(), auditDTO.getTrace());

    verify(auditRepository, times(1)).save(audit);
  }

  @Test
  public void testAuditWith6Parameters() {
    when(auditMapper.mapToEntity(any(AuditDTO.class))).thenReturn(audit);
    auditService.audit(auditDTO.getLevel(), auditDTO.getEvent(), auditDTO.getGroupName(),
        auditDTO.getShortDescription(), auditDTO.getPrinSessionId(), auditDTO.getTrace());

    verify(auditRepository, times(1)).save(audit);
  }

  @Test
  public void testAuditWith7Parameters() {
    when(auditMapper.mapToEntity(any(AuditDTO.class))).thenReturn(audit);
    auditService.audit(auditDTO.getLevel(), auditDTO.getEvent(), auditDTO.getGroupName(),
        auditDTO.getShortDescription(), auditDTO.getPrinSessionId(), auditDTO.getTrace(),
        audit.getReferenceId());

    verify(auditRepository, times(1)).save(audit);
  }

  @Test
  public void auditTraceDataTest() {
    when(auditProperties.isTraceData()).thenReturn(true);
    when(auditMapper.mapToEntity(any(AuditDTO.class))).thenReturn(audit);
    auditService.audit(auditDTO.getLevel(), auditDTO.getEvent(), auditDTO.getGroupName(),
        auditDTO.getShortDescription(), auditDTO.getPrinSessionId(), auditDTO.getTrace(),
        audit.getReferenceId());

    verify(auditRepository, times(1)).save(audit);
  }

  @Test(expected = QAuditException.class)
  public void auditTraceDataExceptionTest() throws JsonProcessingException {
    ReflectionTestUtils.setField(auditService, "mapper", objectMapper);
    when(objectMapper.writeValueAsString(auditDTO.getTrace()))
        .thenThrow(new JsonProcessingException("") {
        });
    when(auditProperties.isTraceData()).thenReturn(true);
    auditService.audit(auditDTO.getLevel(), auditDTO.getEvent(), auditDTO.getGroupName(),
        auditDTO.getShortDescription(), auditDTO.getPrinSessionId(), auditDTO.getTrace(),
        audit.getReferenceId());

    verify(auditRepository, times(1)).save(audit);
  }

  @Test
  public void auditTraceDataFalseTest() {
    when(auditProperties.isTraceData()).thenReturn(true);
    when(auditMapper.mapToEntity(any(AuditDTO.class))).thenReturn(audit);
    auditService.audit(auditDTO.getLevel(), auditDTO.getEvent(), auditDTO.getGroupName(),
        auditDTO.getShortDescription(), auditDTO.getPrinSessionId(), null,
        audit.getReferenceId());

    verify(auditRepository, times(1)).save(audit);
  }

  @Test
  public void testAuditWithTraceDataAsString() {
    when(auditMapper.mapToEntity(any(AuditDTO.class))).thenReturn(audit);
    auditService.audit(auditDTO.getLevel(), auditDTO.getEvent(), auditDTO.getGroupName(),
        auditDTO.getShortDescription(), auditDTO.getPrinSessionId(),
        auditDTO.getTrace().getTraceData());

    verify(auditRepository, times(1)).save(audit);
  }

  @Test
  public void testAuditWithSingleArgument() {
    when(auditMapper.mapToEntity(auditDTO)).thenReturn(audit);
    auditService.audit(auditDTO);

    verify(auditRepository, times(1)).save(audit);
  }

  @Test
  public void testAudit() {
    auditDTO.setTrace(null);
    audit.setTrace(null);
    when(auditMapper.mapToEntity(auditDTO)).thenReturn(audit);
    String auditId = auditService.audit(auditDTO);

    assertEquals(auditDTO.getId(), auditId);
    verify(auditRepository, times(1)).save(audit);
  }

  @Test
  public void testAuditWithTraceData() {
    when(auditMapper.mapToEntity(auditDTO)).thenReturn(audit);
    String auditId = auditService.audit(auditDTO);

    assertEquals(auditDTO.getId(), auditId);
    verify(auditRepository, times(1)).save(audit);
  }

  @Test
  public void testAuditCorellatedDTOList() {
    Collection<String> auditIds = new ArrayList<>();
    for (int i = 0; i < auditsDTO.size(); i++) {
      auditsDTO.get(i).setTrace(null);
      audits.get(i).setTrace(null);
      when(auditMapper.mapToEntity(auditsDTO.get(i))).thenReturn(audits.get(i));
      auditIds.add(auditsDTO.get(i).getId());
    }

    List<String> createdAuditIDs = auditService
        .audits(auditsDTO, initTestValues.getCorrelationId());
    assertEquals(auditIds, createdAuditIDs);
  }

  @Test
  public void testAuditCorrelatedDTOListWithTraceData() {
    Collection<String> auditIds = new ArrayList<>();
    for (int i = 0; i < auditsDTO.size(); i++) {
      when(auditMapper.mapToEntity(auditsDTO.get(i))).thenReturn(audits.get(i));
      auditIds.add(auditsDTO.get(i).getId());
    }

    List<String> createdAuditIDs = auditService
        .audits(auditsDTO, initTestValues.getCorrelationId());
    assertEquals(auditIds, createdAuditIDs);
  }

  @Test
  public void testDeleteAudit() {
    Audit audit2 = initTestValues.createAudit();
    when(auditRepository.fetchById(audit.getId())).thenReturn(audit2);
    auditService.deleteAudit(audit.getId());

    verify(auditRepository, times(1)).delete(audit2);
  }

  @Test
  public void testTruncateAudits() {
    auditService.truncateAudits();
    verify(auditRepository, times(1)).deleteAll();
  }

  @Test
  public void testTruncateAuditsCreatedBeforeDate() {
    Date date = Calendar.getInstance().getTime();
    auditService.truncateAudits(date);
    verify(auditRepository, times(1)).deleteByCreatedOnBefore(date.toInstant().toEpochMilli());
  }

  @Test
  public void testTruncateAuditsRetentionPeriod() {
    Long retentionPeriod = 2629743L;
    auditService.truncateAudits(retentionPeriod);

    verify(auditRepository, times(1)).deleteByCreatedOnBefore(any());
  }

  @Test
  public void tetsGetAuditById() {
    when(auditRepository.fetchById(auditDTO.getId())).thenReturn(audit);
    when(auditMapper.mapToDTO(audit)).thenReturn(auditDTO);
    AuditDTO foundAudit = auditService.getAuditById(auditDTO.getId());

    assertEquals(auditDTO, foundAudit);
  }

  @Test
  public void testGetAuditLogs() {
    Page<AuditDTO> auditPagesDTO = new PageImpl<>(initTestValues.createAuditsDTO());
    Page<Audit> auditPages = new PageImpl<>(initTestValues.createAudits());
    String expression = "%Params%";
    Predicate event = qAudit.event.like(expression);
    Pageable firstTen = PageRequest.of(0, 10);

    when(auditRepository.findAll(event, firstTen)).thenReturn(auditPages);
    when(auditMapper.toAuditDTO(auditPages)).thenReturn(auditPagesDTO);
    Page<AuditDTO> foundAudits = auditService.getAuditLogs(firstTen, event);

    assertEquals(auditPagesDTO, foundAudits);
  }

  @Test
  public void testGetDistinctEventsForReferenceId() {
    List<String> expectedEvents = new ArrayList<>();
    expectedEvents.add("Front End Event");
    expectedEvents.add("Back End Event");

    when(auditRepository.findDistinctEventsByReferenceId(audit.getReferenceId()))
        .thenReturn(expectedEvents);

    List<String> actualEvents = auditService
        .getDistinctEventsForReferenceId(audit.getReferenceId());
    assertEquals(expectedEvents, actualEvents);
  }
}
