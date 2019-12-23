package com.eurodyn.qlack.fuse.aaa.mapper;

import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.OpTemplateDTO;
import com.eurodyn.qlack.fuse.aaa.model.OpTemplate;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OpTemplateMapperImplTest {

  @InjectMocks
  private OpTemplateMapperImpl opTemplateMapper;

  private InitTestValues initTestValues;

  private OpTemplate opTemplate;

  private OpTemplateDTO opTemplateDTO;

  private List<OpTemplate> opTemplateList;

  private List<OpTemplateDTO> opTemplateDTOList;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
    opTemplate = initTestValues.createOpTemplate();
    opTemplateDTO = initTestValues.createOpTemplateDTO();
    opTemplateDTOList = new ArrayList<>();
    opTemplateDTOList = new ArrayList<>();
  }


  @Test
  public void mapToDTOTest() {
    OpTemplateDTO opTemplateDTO = opTemplateMapper.mapToDTO(opTemplate);
    assertEquals(opTemplate.getName(), opTemplateDTO.getName());
  }

  @Test
  public void mapToDTONullTest() {
    OpTemplateDTO opTemplateDTO = opTemplateMapper.mapToDTO((OpTemplate) null);
    assertEquals(null, opTemplateDTO);
  }

  @Test
  public void mapToDTOListNullTest() {
    List<OpTemplateDTO> opTemplateDTOS = opTemplateMapper
      .mapToDTO((List<OpTemplate>) null);
    assertEquals(null, opTemplateDTOS);
  }

  @Test
  public void mapToDTOListTest() {
    List<OpTemplate> opTemplates = new ArrayList<>();
    opTemplates.add(initTestValues.createOpTemplate());
    List<OpTemplateDTO> opTemplateDTOS = opTemplateMapper.mapToDTO(opTemplates);
    assertEquals(opTemplates.size(), opTemplateDTOS.size());
  }

  @Test
  public void mapToEntityTest() {
    OpTemplate opTemplate = opTemplateMapper.mapToEntity(opTemplateDTO);
    assertEquals(opTemplate.getName(), opTemplateDTO.getName());

  }

  @Test
  public void mapToEntityNullTest() {
    OpTemplate opTemplate = opTemplateMapper.mapToEntity((OpTemplateDTO) null);
    assertEquals(null, opTemplate);
  }


  @Test
  public void mapToEntityListTest() {
    List<OpTemplateDTO> opTemplateDTOS = new ArrayList<>();
    opTemplateDTOS.add(initTestValues.createOpTemplateDTO());
    List<OpTemplate> opTemplates = opTemplateMapper.mapToEntity(opTemplateDTOS);
    assertEquals(opTemplateDTOS.size(), opTemplates.size());
  }

  @Test
  public void mapToEntityListNullTest() {
    assertEquals(null,
      opTemplateMapper.mapToEntity((List<OpTemplateDTO>) null));
    List<OpTemplate> opTemplates = opTemplateMapper
      .mapToEntity((List<OpTemplateDTO>) null);
    assertEquals(null, opTemplates);

  }

  @Test
  public void mapToExistingEntityTest() {
    opTemplateMapper.mapToExistingEntity(opTemplateDTO, opTemplate);
    opTemplateDTO.setName(null);
    assertEquals(null, opTemplateDTO.getName());
  }

  @Test
  public void mapToExistingEntitySetDTONullTest() {
    OpTemplateDTO opTemplateDTO = initTestValues.createOpTemplateDTO();
    opTemplateDTO.setDescription(null);
    opTemplateMapper.mapToExistingEntity(null, opTemplate);
    assertEquals(null, opTemplateDTO.getDescription());
  }

}
