package com.eurodyn.qlack.fuse.lexicon.mapper;

import static org.junit.jupiter.api.Assertions.*;

import com.eurodyn.qlack.fuse.lexicon.InitTestValues;
import com.eurodyn.qlack.fuse.lexicon.dto.TemplateDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Template;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TemplateMapperTest {

  @InjectMocks
  private TemplateMapperImpl templateMapper;

  private InitTestValues initTestValues;

  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapToDTOTest() {
    Template template = initTestValues.createTemplate();
    TemplateDTO templateDTO = templateMapper.mapToDTO(template);

    assertEquals(template.getId(), templateDTO.getId());
    assertEquals(template.getName(), templateDTO.getName());
    assertEquals(template.getContent(), templateDTO.getContent());
  }

  @Test
  public void mapToDTOListTest() {
    List<Template> templates = new ArrayList<>();
    templates.add(initTestValues.createTemplate());
    List<TemplateDTO> templateDTOS = templateMapper.mapToDTO(templates);

    assertEquals(templates.size(), templateDTOS.size());
  }

  @Test
  public void mapToDTONullTest() {
    assertNull(templateMapper.mapToDTO((Template) null));

    List<TemplateDTO> templateDTOS = templateMapper.mapToDTO(
      (List<Template>) null);
    assertNull(templateDTOS);
  }

  @Test
  public void mapToEntityTest() {
    TemplateDTO templateDTO = initTestValues.createTemplateDTO();
    Template template = templateMapper.mapToEntity(templateDTO);

    assertEquals(templateDTO.getId(), template.getId());
    assertEquals(templateDTO.getName(), template.getName());
    assertEquals(templateDTO.getContent(), template.getContent());
  }

  @Test
  public void mapToEntityListTest() {
    List<TemplateDTO> templateDTOS = new ArrayList<>();
    templateDTOS.add(initTestValues.createTemplateDTO());
    List<Template> templates = templateMapper.mapToEntity(templateDTOS);

    assertEquals(templateDTOS.size(), templates.size());
  }

  @Test
  public void mapToEntityNullTest() {
    assertNull(templateMapper.mapToEntity((TemplateDTO) null));

    List<Template> templates = templateMapper.mapToEntity(
      (List<TemplateDTO>) null);
    assertNull(templates);
  }

}
