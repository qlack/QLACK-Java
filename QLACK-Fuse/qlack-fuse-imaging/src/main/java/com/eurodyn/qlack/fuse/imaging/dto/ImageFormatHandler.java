package com.eurodyn.qlack.fuse.imaging.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Definition of a handler for a specific image format.
 */
public class ImageFormatHandler {
  // The format this handler handles.
  private String format;

  // The list of classes providing the implementation for this handler.
  private List<String> handlerClasses = new ArrayList<>();

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public List<String> getHandlerClasses() {
    return handlerClasses;
  }

  public void setHandlerClasses(List<String> handlerClasses) {
    this.handlerClasses = handlerClasses;
  }

  public void addHandlerClass(String handlerClass) {
    handlerClasses.add(handlerClass);
  }

  @Override
  public String toString() {
    return "ImageFormatHandler{" +
        "format='" + format + '\'' +
        ", handlerClasses=" + handlerClasses +
        '}';
  }
}
