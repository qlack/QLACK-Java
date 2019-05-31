package com.eurodyn.qlack.fuse.mailing.util;

public enum EmailCharset {

  KOI8_R("koi8-r"),
  ISO_8859_1("iso-8859-1"),
  US_ASCII("us-ascii"),
  UTF_8("utf-8");

  private String value;

  EmailCharset(String value){
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
