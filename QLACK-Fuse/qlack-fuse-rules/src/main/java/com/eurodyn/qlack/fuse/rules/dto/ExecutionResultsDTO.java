package com.eurodyn.qlack.fuse.rules.dto;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A DTO that is used to send the results of a KieSession execution. It contains the updated values
 * of the globals and the facts in byte[] representation.
 *
 * @author European Dynamics SA
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExecutionResultsDTO {

  /**
   * the serialized globals
   */
  private Map<String, byte[]> globals;

  /**
   * the serialized objects
   */
  private List<byte[]> facts;

}
