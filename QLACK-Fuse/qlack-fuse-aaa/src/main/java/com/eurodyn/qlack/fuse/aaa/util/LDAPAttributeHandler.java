package com.eurodyn.qlack.fuse.aaa.util;

public interface LDAPAttributeHandler {
  String handleAttributeName(String attributeName, Object attributeValue);
  Object handleAttributeValue(String attributeName, Object attributeValue);
}
