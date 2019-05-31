package com.eurodyn.qlack.fuse.audit;

import com.eurodyn.qlack.fuse.audit.dto.AuditDTO;
import com.eurodyn.qlack.fuse.audit.dto.AuditLevelDTO;
import com.eurodyn.qlack.fuse.audit.dto.AuditTraceDTO;
import com.eurodyn.qlack.fuse.audit.model.Audit;
import com.eurodyn.qlack.fuse.audit.model.AuditLevel;
import com.eurodyn.qlack.fuse.audit.model.AuditTrace;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 * @author European Dynamics
 */
@Getter
public class InitTestValues {

  private String auditId = "300a084c-1109-4965-b6ba-be2d2199c111";
  private String auditLevelName = "Front End";
  private String auditEvent = "Front End Event";
  private String shortDescription = "Audit short description";
  private String groupName = "Audit group";
  private String auditPrinSessionId = "sessionId";
  private String opt1 = "new users: 3";
  private String opt2 = "deleted users: 0";
  private String opt3 = "expired users: 1";
  private String correlationId = "12345";
  private String referenceId = "11111";
  private Long auditCreatedOn = 1625145120000L;

  private String auditLevelId = "8a882f69-147b-4d4c-a39e-76221b408644";
  private String auditLevelDescription = "Level Description";

  private String auditTraceId = "311a084c-1109-4965-b6ba-be2d2199c612";
  private String auditTraceData = "{\n" +
    "\tcolor: \"red\",\n" +
    "\tvalue: \"#f00\"\n" +
    "}";

  public AuditDTO createAuditDTO() {
    AuditDTO auditDTO = new AuditDTO();
    auditDTO.setId(auditId);
    auditDTO.setLevel(auditLevelName);
    auditDTO.setEvent(auditEvent);
    auditDTO.setShortDescription(shortDescription);
    auditDTO.setGroupName(groupName);
    auditDTO.setPrinSessionId(auditPrinSessionId);
    auditDTO.setOpt1(opt1);
    auditDTO.setOpt2(opt2);
    auditDTO.setOpt3(opt3);
    auditDTO.setReferenceId(referenceId);
    auditDTO.setCreatedOn(auditCreatedOn);
    auditDTO.setTrace(createAuditTraceDTO());
    return auditDTO;
  }

  public Audit createAudit() {
    Audit audit = new Audit();
    audit.setId(auditId);
    audit.setLevelId(createAuditLevel());
    audit.setTrace(createAuditTrace());
    audit.setPrinSessionId(auditPrinSessionId);
    audit.setShortDescription(shortDescription);
    audit.setEvent(auditEvent);
    audit.setCreatedOn(auditCreatedOn);
    audit.setReferenceId(referenceId);
    audit.setGroupName(groupName);
    audit.setOpt1(opt1);
    audit.setOpt2(opt2);
    audit.setOpt3(opt3);
    return audit;
  }

  public List<Audit> createAudits() {
    List<Audit> audits = new ArrayList<>();
    audits.add(createAudit());

    Audit audit2 = createAudit();
    audit2.setId("ed9884fd-2015-4d55-a8b0-80d56606e830");
    audit2.setEvent("Back End Event");
    audits.add(audit2);

    Audit audit3 = createAudit();
    audit3.setId("ed9884fd-2015-4d55-a8b0-80d56606e811");
    audit3.setEvent("Back End Event");
    audits.add(audit3);

    return audits;
  }

  public List<AuditDTO> createAuditsDTO() {
    List<AuditDTO> auditsDTO = new ArrayList<>();
    auditsDTO.add(createAuditDTO());

    AuditDTO auditDTO2 = createAuditDTO();
    auditDTO2.setId("ed9884fd-2015-4d55-a8b0-80d56606e830");
    auditDTO2.setEvent("Back End Event");
    auditsDTO.add(auditDTO2);

    AuditDTO auditDTO3 = createAuditDTO();
    auditDTO3.setId("ed9884fd-2015-4d55-a8b0-80d56606e811");
    auditDTO3.setEvent("Back End Event");
    auditsDTO.add(auditDTO3);

    return auditsDTO;
  }

  public AuditLevelDTO createAuditLevelDTO() {
    AuditLevelDTO auditLevelDTO = new AuditLevelDTO();
    auditLevelDTO.setId(auditLevelId);
    auditLevelDTO.setName(auditLevelName);
    auditLevelDTO.setDescription(auditLevelDescription);
    auditLevelDTO.setPrinSessionId(auditPrinSessionId);
    auditLevelDTO.setCreatedOn(auditCreatedOn);
    return auditLevelDTO;
  }

  public AuditLevel createAuditLevel() {
    AuditLevel auditLevel = new AuditLevel();
    auditLevel.setId(auditLevelId);
    auditLevel.setName(auditLevelName);
    auditLevel.setDescription(auditLevelDescription);
    auditLevel.setPrinSessionId(auditPrinSessionId);
    auditLevel.setCreatedOn(auditCreatedOn);
    return auditLevel;
  }

  public List<AuditLevelDTO> createAuditLevelsDTO() {
    List<AuditLevelDTO> auditLevelsDTO = new ArrayList<>();
    auditLevelsDTO.add(createAuditLevelDTO());
    AuditLevelDTO auditLevelDTO = new AuditLevelDTO();
    auditLevelDTO.setId("c247dcfb-4a80-4bec-9e64-d867b7e92080");
    auditLevelDTO.setName(auditLevelName);
    auditLevelDTO.setDescription(auditLevelDescription);
    auditLevelDTO.setPrinSessionId(auditPrinSessionId);
    auditLevelDTO.setCreatedOn(auditCreatedOn);
    auditLevelsDTO.add(auditLevelDTO);
    return auditLevelsDTO;
  }

  public List<AuditLevel> createAuditLevels() {
    List<AuditLevel> auditLevels = new ArrayList<>();
    auditLevels.add(createAuditLevel());
    AuditLevel auditLevel = new AuditLevel();
    auditLevel.setId("c247dcfb-4a80-4bec-9e64-d867b7e92080");
    auditLevel.setName(auditLevelName);
    auditLevel.setDescription(auditLevelDescription);
    auditLevel.setPrinSessionId(auditPrinSessionId);
    auditLevel.setCreatedOn(auditCreatedOn);
    auditLevels.add(auditLevel);
    return auditLevels;
  }

  public AuditTraceDTO createAuditTraceDTO() {
    AuditTraceDTO auditTraceDTO = new AuditTraceDTO();
    auditTraceDTO.setId(auditTraceId);
    auditTraceDTO.setTraceData(auditTraceData);
    return auditTraceDTO;
  }

  public AuditTrace createAuditTrace() {
    AuditTrace auditTrace = new AuditTrace();
    auditTrace.setId(auditTraceId);
    auditTrace.setTraceData(auditTraceData);
    return auditTrace;
  }

  public List<AuditTraceDTO> createAuditTracesDTO() {
    List<AuditTraceDTO> auditTracesDTO = new ArrayList<>();
    auditTracesDTO.add(createAuditTraceDTO());
    AuditTraceDTO auditTraceDTO = createAuditTraceDTO();
    auditTraceDTO.setId("e0b5c8e6-a7f3-431f-bd5a-be472bf30f76");
    auditTraceDTO.setTraceData(auditTraceData);
    auditTracesDTO.add(auditTraceDTO);
    return auditTracesDTO;
  }

  public List<AuditTrace> createAuditTraces() {
    List<AuditTrace> auditTraces = new ArrayList<>();
    auditTraces.add(createAuditTrace());
    AuditTrace auditTrace = createAuditTrace();
    auditTrace.setId("e0b5c8e6-a7f3-431f-bd5a-be472bf30f76");
    auditTrace.setTraceData(auditTraceData);
    auditTraces.add(auditTrace);
    return auditTraces;
  }
}
