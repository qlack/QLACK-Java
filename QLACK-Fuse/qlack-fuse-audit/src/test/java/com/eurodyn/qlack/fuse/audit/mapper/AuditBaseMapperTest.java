package com.eurodyn.qlack.fuse.audit.mapper;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import com.eurodyn.qlack.fuse.audit.InitTestValues;
import com.eurodyn.qlack.fuse.audit.dto.AuditBaseDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;

import java.util.List;

import static junit.framework.TestCase.assertNotNull;

/**
 * @author European Dynamics
 */

@RunWith(MockitoJUnitRunner.class)
public class AuditBaseMapperTest {


  private AuditBaseMapper auditBaseMapper = new AuditBaseMapper() {
    @Override public AuditBaseDTO mapToDTO(QlackBaseModel entity) {
      return null;
    }

    @Override public List mapToDTO(List entity) {
      return null;
    }

    @Override public QlackBaseModel mapToEntity(AuditBaseDTO dto) {
      return null;
    }

    @Override public void mapToExistingEntity(AuditBaseDTO dto, QlackBaseModel entity) {

    }

    @Override public List mapToEntity(List dto) {
      return null;
    }
  };

  @Mock
  Page<AuditBaseDTO> auditBaseDTO;


  private InitTestValues initTestValues;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapTest() {
    auditBaseMapper.map(auditBaseDTO);
    assertNotNull(auditBaseDTO);
  }


}
