package com.eurodyn.qlack.fuse.lexicon.mapper;

import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.lexicon.InitTestValues;
import com.eurodyn.qlack.fuse.lexicon.dto.GroupDTO;
import com.eurodyn.qlack.fuse.lexicon.dto.LanguageDTO;
import com.eurodyn.qlack.fuse.lexicon.dto.TemplateDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
import com.eurodyn.qlack.fuse.lexicon.model.Language;
import com.eurodyn.qlack.fuse.lexicon.model.Template;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TemplateMapperTest {

  @InjectMocks
  private TemplateMapperImpl templateMapper;

  private InitTestValues initTestValues;

  @Before
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
    assertEquals(null, templateMapper.mapToDTO((Template) null));

    List<TemplateDTO> templateDTOS = templateMapper.mapToDTO(
      (List<Template>) null);
    assertEquals(null, templateDTOS);
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
    assertEquals(null, templateMapper.mapToEntity((TemplateDTO) null));

    List<Template> templates = templateMapper.mapToEntity(
      (List<TemplateDTO>) null);
    assertEquals(null, templates);
  }

}
