package com.eurodyn.qlack.fuse.lexicon.mappers;

import static org.junit.Assert.assertEquals;

import com.eurodyn.qlack.fuse.lexicon.InitTestValues;
import com.eurodyn.qlack.fuse.lexicon.dto.TemplateDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Template;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author European Dynamics
 */
@RunWith(MockitoJUnitRunner.class)
public class TemplateMapperTest {

  @InjectMocks
  private TemplateMapperImpl templateMapperImpl;

  private InitTestValues initTestValues;
  private Template template;
  private TemplateDTO templateDTO;
  private List<Template> templates;
  private List<TemplateDTO> templatesDTO;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
    template = initTestValues.createTemplate();
    templateDTO = initTestValues.createTemplateDTO();
    templates = initTestValues.createTemplates();
    templatesDTO = initTestValues.createTemplatesDTO();
  }

  @Test
  public void testMapToDTOId() {
    templateDTO = templateMapperImpl.mapToDTO(template);
    assertEquals(template.getId(), templateDTO.getId());
  }

  @Test
  public void testMapToDTOName() {
    templateDTO = templateMapperImpl.mapToDTO(template);
    assertEquals(template.getName(), templateDTO.getName());
  }

  @Test
  public void testMapToDTOContent() {
    templateDTO = templateMapperImpl.mapToDTO(template);
    assertEquals(template.getContent(), templateDTO.getContent());
  }

  @Test
  public void testMapToDTOList() {
    templatesDTO = templateMapperImpl.mapToDTO(templates);
    assertEquals(templates.size(), templatesDTO.size());
  }

  @Test
  public void testMapToEntityId() {
    template = templateMapperImpl.mapToEntity(templateDTO);
    assertEquals(templateDTO.getId(), template.getId());
  }

  @Test
  public void testMapToEntityName() {
    template = templateMapperImpl.mapToEntity(templateDTO);
    assertEquals(templateDTO.getName(), template.getName());
  }

  @Test
  public void testMapToEntityContent() {
    template = templateMapperImpl.mapToEntity(templateDTO);
    assertEquals(templateDTO.getContent(), template.getContent());
  }

  @Test
  public void testMapToEntityList() {
    templates = templateMapperImpl.mapToEntity(templatesDTO);
    assertEquals(templatesDTO.size(), templates.size());
  }
}
