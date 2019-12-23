package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.dto.JSONConfig;
import com.eurodyn.qlack.fuse.aaa.dto.OpTemplateDTO;
import com.eurodyn.qlack.fuse.aaa.dto.OperationDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserGroupDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * A Service class that is used to configure json
 *
 * @author European Dynamics SA
 */
@Service
@Validated
@Transactional
public class JSONConfigService {

  // Logger
  private static final Logger LOGGER = Logger
    .getLogger(JSONConfigService.class.getName());

  // JSON parser.
  ObjectMapper mapper = new ObjectMapper();

  private UserGroupService groupService;
  private OpTemplateService templateService;
  private OperationService operationService;

  @Autowired
  public JSONConfigService(UserGroupService groupService,
    OpTemplateService templateService,
    OperationService operationService) {
    this.groupService = groupService;
    this.templateService = templateService;
    this.operationService = operationService;
  }

  @SuppressWarnings("squid:S4790")
  private void parseConfig(URL configFileURL) {
    LOGGER.log(Level.FINE, "Handling FUSE AAA configuration: {0}.",
      configFileURL.toExternalForm());

    // Parse the JSON file.
    JSONConfig config;
    try {
      config = mapper.readValue(configFileURL, JSONConfig.class);
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE, MessageFormat.format(
        "Could not parse configuration file {0}.",
        configFileURL.toExternalForm()), e);
      return;
    }

    // Calculate an MD5 for this file to know if it has changed in order to
    // avoid unnecessary database access.
    try {
      DigestUtils.md5Hex(mapper.writeValueAsString(config));
    } catch (JsonProcessingException e) {
      LOGGER.log(Level.SEVERE, MessageFormat.format(
        "Could not calculate MD5 for file {0}.",
        configFileURL.toExternalForm()), e);
      return;
    }

    parseConfigUserGroups(config);
    parseConfigTemplates(config);
    parseConfigOperations(config);
    parseConfigUserGroupHasOperations(config);
    parseConfigTemplateHasOperations(config);
  }

  @SuppressWarnings("squid:S4834")
  private void parseConfigUserGroups(JSONConfig config) {
    // Create userGroups.
    for (JSONConfig.Group g : config.getGroups()) {
      // If the userGroup exists, update it, otherwise create it.
      LOGGER.log(Level.FINEST, "Processing userGroup {0}", g.getName());
      UserGroupDTO userGroupDTO = groupService
        .getGroupByName(g.getName(), true);
      boolean isNew = userGroupDTO == null;
      if (isNew) {
        userGroupDTO = new UserGroupDTO();
      }
      userGroupDTO.setDescription(g.getDescription());
      userGroupDTO.setName(g.getName());
      if (StringUtils.isNotBlank(g.getParentGroupName())) {
        UserGroupDTO parentGroup = groupService
          .getGroupByName(g.getParentGroupName(), true);
        userGroupDTO.setParentId(parentGroup.getId());
      }
      if (isNew) {
        groupService.createGroup(userGroupDTO);
      } else {
        groupService.updateGroup(userGroupDTO);
      }
    }
  }

  private void parseConfigTemplates(JSONConfig config) {
    // Create templates.
    for (JSONConfig.Template t : config.getTemplates()) {
      // If the template exists, update it, otherwise create it.
      LOGGER.log(Level.FINEST, "Processing template {0}", t.getName());
      OpTemplateDTO templateDTO = templateService
        .getTemplateByName(t.getName());
      boolean isNew = templateDTO == null;
      if (isNew) {
        templateDTO = new OpTemplateDTO();
      }
      templateDTO.setDescription(t.getDescription());
      templateDTO.setName(t.getName());
      if (isNew) {
        templateService.createTemplate(templateDTO);
      } else {
        templateService.updateTemplate(templateDTO);
      }
    }
  }

  private void parseConfigOperations(JSONConfig config) {
    // Create Operations.
    for (JSONConfig.Operation o : config.getOperations()) {
      // If the operation exists, update it, otherwise create it.
      LOGGER.log(Level.FINEST, "Processing operation {0}", o.getName());
      OperationDTO opDTO = operationService.getOperationByName(o.getName());
      boolean isNew = opDTO == null;
      if (isNew) {
        opDTO = new OperationDTO();
      }
      opDTO.setDescription(o.getDescription());
      opDTO.setName(o.getName());
      if (isNew) {
        operationService.createOperation(opDTO);
      } else {
        operationService.updateOperation(opDTO);
      }
    }
  }

  private void parseConfigUserGroupHasOperations(JSONConfig config) {
    // Create UserGroup has Operations.
    for (JSONConfig.GroupHasOperation gho : config.getGroupHasOperations()) {
      // If the operation exists, update it, otherwise create it.
      LOGGER.log(Level.FINEST, "Processing userGroup has operation {0}-{1}",
        new String[]{gho.getGroupName(), gho.getOperationName()});
      UserGroupDTO userGroupDTO = groupService
        .getGroupByName(gho.getGroupName(), true);
      if (!operationService.getAllowedGroupsForOperation(
        gho.getOperationName(), false).contains(userGroupDTO.getId())) {
        operationService.addOperationToGroup(
          userGroupDTO.getId(), gho.getOperationName(), gho.isDeny());
      }
    }
  }

  private void parseConfigTemplateHasOperations(JSONConfig config) {
    // Create Template has Operations.
    for (JSONConfig.TemplateHasOperation tho : config
      .getTemplateHasOperations()) {
      // If the operation exists, update it, otherwise create it.
      LOGGER.log(Level.FINEST, "Processing template has operation {0}-{1}",
        new String[]{tho.getTemplateName(), tho.getOperationName()});
      OpTemplateDTO templateDTO = templateService
        .getTemplateByName(tho.getTemplateName());
      if (templateService
        .getOperationAccess(templateDTO.getId(), tho.getOperationName())
        == null) {
        templateService
          .addOperation(templateDTO.getId(), tho.getOperationName(),
            tho.isDeny());
      }
    }
  }

  @PostConstruct
  public void init() {
    initWithFile("qlack-aaa-config.json");
  }

  public void initWithFile(String configFile) {
    // Find AAA configurations.
    try {
      Enumeration<URL> entries = this.getClass().getClassLoader()
        .getResources(configFile);
      if (entries != null) {
        while (entries.hasMoreElements()) {
          parseConfig(entries.nextElement());
        }
      }
    } catch (IOException e) {
      LOGGER.log(Level.SEVERE,
        "Could not search QLACK Fuse AAA configuration files.", e);
    }
  }
}
