package com.eurodyn.qlack.fuse.imaging.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Definition of a handler for a specific image format.
 *
 * @author European Dynamics SA.
 */
@Getter
@Setter
@ToString
public class ImageFormatHandler {

  /**
   * The format this handler handles.
   */
  private String format;

  /**
   * The list of classes providing the implementation for this handler.
   */
  private List<String> handlerClasses = new ArrayList<>();

  /**
   * Adds a specific image format handler class.
   *
   * @param handlerClass the class to be added
   */
  public void addHandlerClass(String handlerClass) {
    handlerClasses.add(handlerClass);
  }

}
