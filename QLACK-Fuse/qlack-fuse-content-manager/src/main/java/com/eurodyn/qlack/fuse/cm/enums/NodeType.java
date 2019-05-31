package com.eurodyn.qlack.fuse.cm.enums;

public enum NodeType {
  /**
   * Important: Do not modify the order of enum values below since EnumType.ORDINAL is used in the Node entity and therefore a change in
   * the order will render existing DB data inconsistent. New values should always be added to the end of the list.
   */
  FOLDER,
  FILE
}
