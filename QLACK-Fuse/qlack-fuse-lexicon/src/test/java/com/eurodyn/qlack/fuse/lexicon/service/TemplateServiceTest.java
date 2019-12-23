package com.eurodyn.qlack.fuse.lexicon.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.lexicon.InitTestValues;
import com.eurodyn.qlack.fuse.lexicon.dto.TemplateDTO;
import com.eurodyn.qlack.fuse.lexicon.exception.TemplateProcessingException;
import com.eurodyn.qlack.fuse.lexicon.mapper.TemplateMapper;
import com.eurodyn.qlack.fuse.lexicon.model.Data;
import com.eurodyn.qlack.fuse.lexicon.model.Language;
import com.eurodyn.qlack.fuse.lexicon.model.Template;
import com.eurodyn.qlack.fuse.lexicon.repository.LanguageRepository;
import com.eurodyn.qlack.fuse.lexicon.repository.TemplateRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author European Dynamics
 */

@RunWith(MockitoJUnitRunner.class)
public class TemplateServiceTest {

  @InjectMocks
  private TemplateService templateService;

  private TemplateRepository templateRepository = mock(
    TemplateRepository.class);
  private LanguageRepository languageRepository = mock(
    LanguageRepository.class);

  @Spy
  private TemplateMapper templateMapper;

  private InitTestValues initTestValues;
  private TemplateDTO templateDTO;
  private Template template;
  private List<Template> templates;
  private Language language;
  private Data data;
  private Map<String, Object> templateData;

  private String expectedProcessedNestedTemplate;
  private String expectedProcessedTemplate;

  @Before
  public void init() {
    templateService = new TemplateService(templateRepository,
      languageRepository, templateMapper);

    initTestValues = new InitTestValues();
    templateDTO = initTestValues.createTemplateDTO();
    template = initTestValues.createTemplate();
    templates = initTestValues.createTemplates();
    language = initTestValues.createEnglishLanguage();
    data = initTestValues.createData();
    templateData = new HashMap<>();
    templateData.put("data", data);
    templateData.put("template", template);

    expectedProcessedTemplate =
      "<h1>Template example</h1><p>Add attachment description</p>";

    expectedProcessedNestedTemplate = "<html>"
      + "<body>"
      + "<title>Nested Template</title>"
      + "<div>"
      + "<h1>Template example</h1>"
      + "<p>Add attachment description</p>"
      + "</div>"
      + "</body>"
      + "</html>";
  }

  @Test
  public void testCreateTemplate() {
    when(languageRepository.fetchById(templateDTO.getLanguageId()))
      .thenReturn(language);
    templateService.createTemplate(templateDTO);
    verify(templateRepository, times(1)).save(any());
  }

  @Test
  public void testUpdateTemplate() {
    templateDTO.setName("Updated name");
    templateDTO.setContent("Updated content");
    when(templateRepository.fetchById(templateDTO.getId()))
      .thenReturn(template);
    when(languageRepository.fetchById(templateDTO.getLanguageId()))
      .thenReturn(language);
    templateService.updateTemplate(templateDTO);

    verify(templateRepository, times(1)).save(template);
    assertEquals(templateDTO.getName(), template.getName());
    assertEquals(templateDTO.getContent(), template.getContent());
  }

  @Test
  public void testDeleteTemplate() {
    templateService.deleteTemplate(template.getId());
    verify(templateRepository, times(1)).deleteById(template.getId());
  }

  @Test
  public void testGetTemplate() {
    when(templateRepository.findById(template.getId()))
      .thenReturn(Optional.of(template));
    when(templateMapper.mapToDTO(template)).thenReturn(templateDTO);
    TemplateDTO foundTemplateDTO = templateService
      .getTemplate(template.getId());
    assertEquals(templateDTO, foundTemplateDTO);
  }

  @Test(expected = QDoesNotExistException.class)
  public void testGetTemplateException() {
    templateService.getTemplate(template.getId());
  }

  @Test
  public void testGetTemplateContentByName() {
    Map<String, String> expectedTemplateContentsByName = new HashMap<>();
    templates
      .forEach(t -> expectedTemplateContentsByName
        .put(t.getLanguage().getId(), t.getContent()));
    when(templateRepository.findByName(template.getName()))
      .thenReturn(templates);
    Map<String, String> templateContentByName = templateService
      .getTemplateContentByName(template.getName());
    assertEquals(expectedTemplateContentsByName, templateContentByName);
  }

  @Test
  public void testGetTemplateContentByNameNotFound() {
    Map<String, String> templateContentByName = templateService
      .getTemplateContentByName(template.getName());
    assertNull(templateContentByName);
  }

  @Test
  public void testGetTemplateContentByNameAndLanguageId() {
    when(templateRepository
      .findByNameAndLanguageId(template.getName(), language.getId()))
      .thenReturn(template);
    String templateContentByName = templateService
      .getTemplateContentByName(template.getName(), language.getId());
    assertEquals(template.getContent(), templateContentByName);
  }

  @Test
  public void testGetTemplateContentByNameAndLanguageIdNotFound() {
    String templateContentByName = templateService
      .getTemplateContentByName(template.getName(), language.getId());
    assertNull(templateContentByName);
  }

  @Test
  public void testProcessTemplateByName() {
    when(templateRepository
      .findByNameAndLanguageId(template.getName(), language.getId()))
      .thenReturn(template);
    String processedTemplate = templateService
      .processTemplateByName(template.getName(), language.getId(),
        templateData);
    assertEquals(expectedProcessedTemplate, processedTemplate);
  }

  @Test
  public void testProcessTemplateByNameAndLocale() {
    when(templateRepository
      .findByNameAndLanguageLocale(template.getName(), language.getLocale()))
      .thenReturn(template);
    String processedTemplate = templateService
      .processTemplateByNameAndLocale(template.getName(), language.getLocale(),
        templateData);
    assertEquals(expectedProcessedTemplate, processedTemplate);
  }

  @Test
  public void testProcessTemplateNested() {
    String processedTemplate = templateService
      .processTemplate(initTestValues.nestedTemplateContent, templateData);
    assertEquals(expectedProcessedNestedTemplate, processedTemplate);
  }

  @Test(expected = TemplateProcessingException.class)
  public void testProcessTemplateException() {
    templateData = new HashMap<>();
    templateData.put("data", null);
    String processedTemplate = templateService
      .processTemplate(template.getContent(), templateData);
    assertEquals(expectedProcessedTemplate, processedTemplate);
  }

  @Test
  public void processTemplateByNameNoValuesTest() {
    String content = "<h1>Template example</h1><p>message</p>";
    template.setContent(content);
    when(templateRepository
      .findByNameAndLanguageId(template.getName(), language.getId()))
      .thenReturn(template);
    String processedTemplate = templateService
      .processTemplateByName(template.getName(), language.getId(),
        templateData);
    assertEquals(content, processedTemplate);
  }

}
