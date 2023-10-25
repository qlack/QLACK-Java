package com.eurodyn.qlack.fuse.aaa.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.OpTemplateDTO;
import com.eurodyn.qlack.fuse.aaa.model.OpTemplate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OpTemplateMapperImplTest {

  @InjectMocks
  private OpTemplateMapperImpl opTemplateMapper;

  private InitTestValues initTestValues;

  private OpTemplate opTemplate;

  private OpTemplateDTO opTemplateDTO;

  private List<OpTemplate> opTemplateList;

  private List<OpTemplateDTO> opTemplateDTOList;

  @BeforeEach
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
    assertNull(opTemplateDTO);
  }

  @Test
  public void mapToDTOListNullTest() {
    List<OpTemplateDTO> opTemplateDTOS = opTemplateMapper
      .mapToDTO((List<OpTemplate>) null);
    assertNull(opTemplateDTOS);
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
    assertNull(opTemplate);
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
    assertNull(opTemplateMapper.mapToEntity((List<OpTemplateDTO>) null));
    List<OpTemplate> opTemplates = opTemplateMapper
      .mapToEntity((List<OpTemplateDTO>) null);
    assertNull(opTemplates);

  }

  @Test
  public void mapToExistingEntityTest() {
    opTemplateMapper.mapToExistingEntity(opTemplateDTO, opTemplate);
    opTemplateDTO.setName(null);
    assertNull(opTemplateDTO.getName());
  }

  @Test
  public void mapToExistingEntitySetDTONullTest() {
    OpTemplateDTO opTemplateDTO = initTestValues.createOpTemplateDTO();
    opTemplateDTO.setDescription(null);
    opTemplateMapper.mapToExistingEntity(null, opTemplate);
    assertNull(opTemplateDTO.getDescription());
  }

}
