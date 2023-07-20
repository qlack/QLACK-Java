package com.eurodyn.qlack.util.hibernate.dialect;

import org.hibernate.dialect.MySQLDialect;

/**
 * A dialect for MariaDB 10.3+ creating tables in UTF-8.
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class MySQL8UTF8Dialect extends MySQLDialect {

  @Override
  public String getTableTypeString() {
    return " DEFAULT CHARSET=utf8";
  }
}
