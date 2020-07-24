package com.eurodyn.qlack.fuse.lexicon.service;


import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.lexicon.dto.TemplateDTO;
import com.eurodyn.qlack.fuse.lexicon.exception.TemplateProcessingException;
import com.eurodyn.qlack.fuse.lexicon.mapper.TemplateMapper;
import com.eurodyn.qlack.fuse.lexicon.model.Template;
import com.eurodyn.qlack.fuse.lexicon.repository.LanguageRepository;
import com.eurodyn.qlack.fuse.lexicon.repository.TemplateRepository;
import freemarker.template.TemplateException;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A Template Service class contains the implementations of crud operations to database for template entity and
 * language.
 *
 * @author European Dynamics SA
 */
@Transactional
@Service
@Validated
@Log
public class TemplateService {

  private final TemplateRepository templateRepository;
  private final LanguageRepository languageRepository;

  private TemplateMapper templateMapper;

  public TemplateService(TemplateRepository templateRepository,
      LanguageRepository languageRepository, TemplateMapper templateMapper) {
    this.templateRepository = templateRepository;
    this.languageRepository = languageRepository;
    this.templateMapper = templateMapper;
  }

  /**
   * Creates a template.
   *
   * @param template a DTO containing all information about persisted template
   * @return the id of the persisted template
   */
  public String createTemplate(TemplateDTO template) {
    log.info(MessageFormat.format("Creating template {0}", template));
    Template entity = new Template();
    saveEntity(template, entity);
    return entity.getId();
  }

  /**
   * Updates persisted template.
   *
   * @param template a DTO containing the updated data of the persisted template
   */
  public void updateTemplate(TemplateDTO template) {
    log.info(MessageFormat.format("Updating template {0}", template));
    Template entity = templateRepository.fetchById(template.getId());
    saveEntity(template, entity);
  }

  private void saveEntity(TemplateDTO template, Template entity) {
    entity.setName(template.getName());
    entity.setContent(template.getContent());
    entity.setLanguage(languageRepository.fetchById(template.getLanguageId()));
    templateRepository.save(entity);
  }

  /**
   * Deletes a persisted template by given id.
   *
   * @param templateId the id of the template to delete
   */
  public void deleteTemplate(String templateId) {
    log.info(MessageFormat.format("Deleting template with id {0}", templateId));
    templateRepository.deleteById(templateId);
  }

  /**
   * Fetching a template that matches the given id.
   *
   * @param templateId the id of the persisted template
   * @return the template matching the given id
   */
  public TemplateDTO getTemplate(String templateId) {
    log.info(MessageFormat.format("Fetching template with id {0}", templateId));
    return templateMapper.mapToDTO(
        templateRepository.findById(templateId)
            .orElseThrow(QDoesNotExistException::new));
  }

  /**
   * Fetches the content of a template matching the given name.
   *
   * @param templateName the name of the template
   * @return a map containing pairs of languageId/templateContent of the template matching the given name
   */
  public Map<String, String> getTemplateContentByName(String templateName) {
    log.info(MessageFormat
        .format("Fetching contents of template with name {0}", templateName));
    List<Template> templates = templateRepository.findByName(templateName);

    if (templates.isEmpty()) {
      return null;
    }
    Map<String, String> contents = new HashMap<>();
    for (Template template : templates) {
      contents.put(template.getLanguage().getId(), template.getContent());
    }
    return contents;
  }

  /**
   * Fetches the content of a template matching the given name and language.
   *
   * @param templateName the name of the template
   * @param languageId the language of the template content
   * @return the template content
   */
  public String getTemplateContentByName(String templateName,
      String languageId) {
    log.info(MessageFormat
        .format("Fetching template with name {0} and language {1}", templateName,
            languageId));
    Template template = templateRepository
        .findByNameAndLanguageId(templateName, languageId);

    return template != null ? template.getContent() : null;
  }

  /**
   * Retrieves a template that matches the given name and language and then processes it with the given data.
   *
   * @param templateName the name of the template that will be processed
   * @param languageId the id of the language of the template
   * @param templateData a map containing pairs of template key/translation.
   * @param forceEmptyChecks defines if ftl empty checks will be added to the template
   * @return the template content processed with the given data
   */
  public String processTemplateByName(String templateName, String languageId,
      Map<String, Object> templateData, boolean forceEmptyChecks) {
    log.info(MessageFormat
        .format("Processing template with name {0} and language {1}",
            templateName, languageId));
    Template template = templateRepository
        .findByNameAndLanguageId(templateName, languageId);

    return processTemplate(template, templateData, forceEmptyChecks);
  }

  /**
   * Retrieves a template that matches the given name and language and then processes it with the given data.
   *
   * @param templateName the name of the template that will be processed
   * @param locale the locale of the template
   * @param templateData a map containing pairs of template key/translation.
   * @param forceEmptyChecks defines if ftl empty checks will be added to the template
   * @return the template content processed with the given data
   */
  public String processTemplateByNameAndLocale(String templateName, String locale, Map<String, Object> templateData,
      boolean forceEmptyChecks) {
    log.info(MessageFormat
        .format("Fetching template with name {0} and locale {1}", templateName,
            locale));
    Template template = templateRepository
        .findByNameAndLanguageLocale(templateName, locale);

    return processTemplate(template, templateData, forceEmptyChecks);
  }

  /**
   * Creates a template from given argument and processes it with the given data.
   *
   * @param templateBody a String representation of the template
   * @param templateData a map containing pairs of template key/translation.
   * @param forceEmptyChecks defines if ftl empty checks will be added to the template
   * @return the template content processed with the given data
   */
  public String processTemplate(String templateBody,
      Map<String, Object> templateData, boolean forceEmptyChecks) {
    log.info("Processing non persisted template");
    Template template = new Template();
    template.setContent(templateBody);
    template.setName(UUID.randomUUID().toString());

    return processTemplate(template, templateData, forceEmptyChecks);
  }

  /**
   * Retrieves a template that matches the given name and language and then processes it with the given data.
   *
   * @param templateName the name of the template that will be processed
   * @param languageId the id of the language of the template
   * @param templateData a map containing pairs of template key/translation.
   * @return the template content processed with the given data
   */
  @Deprecated
  public String processTemplateByName(String templateName, String languageId,
      Map<String, Object> templateData) {
    log.info(MessageFormat
        .format("Processing template with name {0} and language {1}",
            templateName, languageId));
    Template template = templateRepository
        .findByNameAndLanguageId(templateName, languageId);

    return processTemplate(template, templateData, false);
  }

  /**
   * Retrieves a template that matches the given name and language and then processes it with the given data.
   *
   * @param templateName the name of the template that will be processed
   * @param locale the locale of the template
   * @param templateData a map containing pairs of template key/translation.
   * @return the template content processed with the given data
   */
  @Deprecated
  public String processTemplateByNameAndLocale(String templateName, String locale, Map<String, Object> templateData) {
    log.info(MessageFormat
        .format("Fetching template with name {0} and locale {1}", templateName,
            locale));
    Template template = templateRepository
        .findByNameAndLanguageLocale(templateName, locale);

    return processTemplate(template, templateData, false);
  }

  /**
   * Creates a template from given argument and processes it with the given data.
   *
   * @param templateBody a String representation of the template
   * @param templateData a map containing pairs of template key/translation.
   * @return the template content processed with the given data
   */
  @Deprecated
  public String processTemplate(String templateBody,
      Map<String, Object> templateData) {
    log.info("Processing non persisted template");
    Template template = new Template();
    template.setContent(templateBody);
    template.setName(UUID.randomUUID().toString());

    return processTemplate(template, templateData, false);
  }

  /**
   * Helper method to process a template. It performs a 2-passes variable replacement to support variables resolution
   * within variables. We have opted for a non-recursive calling approach to keep the code simple as only 2 levels of
   * nesting is supported.
   *
   * @param template the content to be parsed
   * @param templateData the data to be loaded at the template
   * @param forceEmptyChecks defines if ftl empty checks will be added to the template
   * @return the parsed content
   */
  private String processTemplate(Template template,
      Map<String, Object> templateData, boolean forceEmptyChecks) {
    String retVal = template.getContent();
    // 1st pass.
    if (retVal.contains("${")) {
      retVal = processTemplate(template.getContent(), template.getName(),
          templateData, forceEmptyChecks);
      // 2nd pass (to support variables in variables).
      if (retVal.contains("${")) {
        retVal = processTemplate(retVal, template.getName(), templateData, forceEmptyChecks);
      }
    }

    return retVal;
  }

  /**
   * Helper method to process a template as string.
   *
   * @param content the content to be parsed
   * @param templateName the name of the template content
   * @param templateData the data to be loaded at the template
   * @param forceEmptyChecks defines if ftl empty checks will be added to the template
   * @return the parsed content
   */
  private String processTemplate(String content, String templateName,
      Map<String, Object> templateData, boolean forceEmptyChecks) {
    StringWriter retVal = new StringWriter();
    try {
      freemarker.template.Template fTemplate = new freemarker.template.Template(
          templateName,
          new StringReader(forceEmptyChecks ? addChecksOnTemplateBody(content) : content), null);
      fTemplate.process(templateData, retVal);
      retVal.flush();
    } catch (TemplateException | IOException ex) {
      // Catch exception and throw RuntimeException instead in order to
      // also roll back the transaction.
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      throw new TemplateProcessingException(
          MessageFormat.format("Error processing template {0}.", templateName));
    }
    return retVal.toString();
  }

  /**
   * This method updates the template to add ftl empty checks in order to handle cases where the template is referring
   * to non-existing variables and the FreeMarker processing would fail.
   *
   * @param template the template to update
   * @return the updated template with the empty checks
   */
  private String addChecksOnTemplateBody(String template) {
    Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
    Matcher ftlVariableMatcher = pattern.matcher(template);

    StringBuilder builder = new StringBuilder();
    int i = 0;
    while (ftlVariableMatcher.find()) {
      builder.append(template, i, ftlVariableMatcher.start());

      if ((ftlVariableMatcher.group().length() > 1) && (!ftlVariableMatcher.group(1).trim().isEmpty())) {
        builder.append("${(").append(ftlVariableMatcher.group(1)).append(")!}");
      }

      i = ftlVariableMatcher.end();
    }
    builder.append(template.substring(i));
    return builder.toString();
  }
}
