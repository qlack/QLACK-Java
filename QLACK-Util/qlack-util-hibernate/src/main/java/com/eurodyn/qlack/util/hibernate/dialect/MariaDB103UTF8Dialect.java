package com.eurodyn.qlack.util.hibernate.dialect;

import org.hibernate.dialect.MariaDBDialect;

/**
 * A dialect for MariaDB 10.3+ creating tables in UTF-8.
 */
@SuppressWarnings("squid:MaximumInheritanceDepth")
public class MariaDB103UTF8Dialect extends MariaDBDialect {

  @Override
  public String getTableTypeString() {
    return " DEFAULT CHARSET=utf8";
  }
}
