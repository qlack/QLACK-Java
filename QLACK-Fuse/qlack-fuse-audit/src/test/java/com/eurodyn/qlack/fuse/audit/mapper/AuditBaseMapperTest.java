package com.eurodyn.qlack.fuse.audit.mapper;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import com.eurodyn.qlack.fuse.audit.InitTestValues;
import com.eurodyn.qlack.fuse.audit.dto.AuditBaseDTO;
import com.eurodyn.qlack.fuse.audit.dto.AuditDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

/**
 * @author European Dynamics
 */

@RunWith(MockitoJUnitRunner.class)
public class AuditBaseMapperTest {

  @Mock
  private AuditBaseMapper auditBaseMapper;

  private InitTestValues initTestValues;

  private QlackBaseModel qlackBaseModel;
  private AuditBaseDTO auditBaseDTO;
  private List<QlackBaseModel> qlackBaseModels;
  private List<AuditBaseDTO> auditBaseDTOS;


  @Before
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void testMap() {
   auditBaseMapper.map( (Page<AuditBaseDTO>) auditBaseDTO);
  }


}
