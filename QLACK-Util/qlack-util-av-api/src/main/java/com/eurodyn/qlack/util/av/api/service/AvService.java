package com.eurodyn.qlack.util.av.api.service;

import com.eurodyn.qlack.util.av.api.dto.VirusScanDTO;
import com.eurodyn.qlack.util.av.api.exception.VirusScanException;

/**
 * @author European Dynamics
 */
public interface AvService {
  VirusScanDTO virusScan(byte[] data) throws VirusScanException;

  boolean hostIsAvailable(String avHost, int avPort);

}
