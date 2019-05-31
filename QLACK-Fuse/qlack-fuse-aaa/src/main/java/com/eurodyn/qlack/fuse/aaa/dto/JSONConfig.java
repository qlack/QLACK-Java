package com.eurodyn.qlack.fuse.aaa.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * A Java class mirroring the JSON file with configuration options.
 */
@Getter
@Setter
public class JSONConfig {

  private Group[] groups;
  private Template[] templates;
  private Operation[] operations;
  private GroupHasOperation[] groupHasOperations;
  private TemplateHasOperation[] templateHasOperations;

  public Group[] getGroups() {
    return groups != null ? groups : new Group[0];
  }

  public Template[] getTemplates() {
    return templates != null ? templates : new Template[0];
  }

  public Operation[] getOperations() {
    return operations != null ? operations : new Operation[0];
  }

  public GroupHasOperation[] getGroupHasOperations() {
    return groupHasOperations != null ? groupHasOperations : new GroupHasOperation[0];
  }

  public void setGroupHasOperations(GroupHasOperation[] groupHasOperations) {
    this.groupHasOperations = groupHasOperations;
  }

  public TemplateHasOperation[] getTemplateHasOperations() {
    return templateHasOperations != null ? templateHasOperations : new TemplateHasOperation[0];
  }

  public void setTemplateHasOperations(TemplateHasOperation[] templateHasOperations) {
    this.templateHasOperations = templateHasOperations;
  }

  @Getter
  @Setter
  public static class Group {

    private String name;
    private String description;
    private String objectID;
    private String parentGroupName;
  }

  @Getter
  @Setter
  public static class Template {

    private String name;
    private String description;

  }

  @Getter
  @Setter
  public static class Operation {

    private String name;
    private String description;

  }

  @Getter
  @Setter
  public static class GroupHasOperation {

    private String groupName;
    private String operationName;
    private boolean deny;

  }

  @Getter
  @Setter
  public static class TemplateHasOperation {

    private String templateName;
    private String operationName;
    private boolean deny;

  }

}
