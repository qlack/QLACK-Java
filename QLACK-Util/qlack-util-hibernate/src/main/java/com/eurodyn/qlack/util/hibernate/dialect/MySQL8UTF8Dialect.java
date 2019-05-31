package com.eurodyn.qlack.util.hibernate.dialect;

import org.hibernate.dialect.MySQL8Dialect;

/**
 * A dialect for MariaDB 10.3+ creating tables in UTF-8.
 */
public class MySQL8UTF8Dialect extends MySQL8Dialect {

  @Override
  public String getTableTypeString() {
    return " DEFAULT CHARSET=utf8";
  }
}
