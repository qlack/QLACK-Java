package com.eurodyn.qlack.fuse.fd.util;


import lombok.Getter;

/**
 * The ThreadStatus Enum that represents the kinds of status that a Thread can support.
 *
 * @author European Dynamics SA
 */

@Getter
public enum ThreadStatus {

  DRAFT("draft"),
  ACTIVE("active"),
  INACTIVE("inactive"),
  ARCHIVED("archived"),
  PUBLISHED("published"),
  UNPUBLISHED("unpublished"),
  APPROVED("approved"),
  DELETED("deleted"),
  REQUESTED("requested"),
  READ("read"),
  UNREAD("Unread"),
  HIDDEN("hidden"),
  SCHEDULED("Scheduled"),
  SPAM("spam"),
  REJECTED("rejected");


  /**
   * The Status name.
   */
  private final String name;


  /**
   * Constructor
   */
  ThreadStatus(String name) {
    this.name = name;
  }
}
