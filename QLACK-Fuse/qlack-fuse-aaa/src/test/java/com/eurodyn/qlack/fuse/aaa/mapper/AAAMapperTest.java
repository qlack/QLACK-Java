package com.eurodyn.qlack.fuse.aaa.mapper;

import static junit.framework.TestCase.assertNotNull;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.BaseDTO;
import com.eurodyn.qlack.fuse.aaa.model.AAAModel;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;

/**
 * @author European Dynamics
 */

@RunWith(MockitoJUnitRunner.class)
public class AAAMapperTest {

  private AAAMapper aaaMapper = new AAAMapper() {
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

  @Before
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapTest() {
    aaaMapper.map(aaaModel);
    assertNotNull(aaaModel);
  }


}
