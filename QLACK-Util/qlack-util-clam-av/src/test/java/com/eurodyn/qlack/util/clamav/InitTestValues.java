package com.eurodyn.qlack.util.clamav;

import com.eurodyn.qlack.util.clamav.util.ClamAvProperties;

/**
 * Provides object initialization methods for Test classes
 *
 * @author European Dynamics SA.
 */
public class InitTestValues {

  private byte[] data = {80, 65, 78, 75, 65, 74};

  public byte[] createData() {
    return data;
  }

  public ClamAvProperties createProperties() {
    ClamAvProperties properties = new ClamAvProperties();
    properties.setClamAvHost("");
    properties.setClamAvPort(1);
    properties.setClamAvSocketTimeout(10);
    return properties;
  }
}
