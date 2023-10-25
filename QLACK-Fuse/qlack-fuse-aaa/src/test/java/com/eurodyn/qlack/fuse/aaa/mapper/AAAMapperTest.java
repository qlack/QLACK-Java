package com.eurodyn.qlack.fuse.aaa.mapper;


import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.BaseDTO;
import com.eurodyn.qlack.fuse.aaa.model.AAAModel;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author European Dynamics
 */

@ExtendWith(MockitoExtension.class)
public class AAAMapperTest {

  final private AAAMapper aaaMapper = new AAAMapper() {
    @Override
    public BaseDTO mapToDTO(AAAModel entity) {
      return null;
    }

    @Override
    public List mapToDTO(List entity) {
      return null;
    }

    @Override
    public AAAModel mapToEntity(BaseDTO dto) {
      return null;
    }

    @Override
    public void mapToExistingEntity(BaseDTO dto, AAAModel entity) {

    }

    @Override
    public List mapToEntity(List dto) {
      return null;
    }
  };

  @Mock
  Page<AAAModel> aaaModel;


  private InitTestValues initTestValues;

  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapTest() {
    aaaMapper.map(aaaModel);
    assertNotNull(aaaModel);
  }


}
