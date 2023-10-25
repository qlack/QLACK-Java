package com.eurodyn.qlack.fuse.audit.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import com.eurodyn.qlack.fuse.audit.InitTestValues;
import com.eurodyn.qlack.fuse.audit.dto.AuditBaseDTO;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

/**
 * @author European Dynamics
 */

@ExtendWith(MockitoExtension.class)
public class AuditBaseMapperTest {


  final private AuditBaseMapper auditBaseMapper = new AuditBaseMapper() {
    @Override
    public AuditBaseDTO mapToDTO(QlackBaseModel entity) {
      return null;
    }

    @Override
    public List mapToDTO(List entity) {
      return null;
    }

    @Override
    public QlackBaseModel mapToEntity(AuditBaseDTO dto) {
      return null;
    }

    @Override
    public void mapToExistingEntity(AuditBaseDTO dto, QlackBaseModel entity) {

    }

    @Override
    public List mapToEntity(List dto) {
      return null;
    }
  };

  @Mock
  Page<AuditBaseDTO> auditBaseDTO;


  private InitTestValues initTestValues;

  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapTest() {
    auditBaseMapper.map(auditBaseDTO);
    assertNotNull(auditBaseDTO);
  }


}
