package com.eurodyn.qlack.fuse.fd.util;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * The ThreadStatus Enum that represents the kinds of status that a Thread can support.
 *
 * @author European Dynamics SA
 */
@Getter
public enum Reaction {

  /**
   * Positive reaction
   */
  LIKE("1"),
  /**
   * Negative reaction
   */
  DISLIKE("2");

  /**
   * Textual representation.
   */
  final String num;


  /**
   * Constructor
   * @param num value of enum
   */
  Reaction(String num) {
    this.num = num;
  }

  @Override
  @JsonValue
  public String toString() {
    return this.num;
  }

}
