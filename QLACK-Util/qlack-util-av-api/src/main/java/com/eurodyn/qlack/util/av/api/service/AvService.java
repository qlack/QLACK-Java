package com.eurodyn.qlack.util.av.api.service;

import com.eurodyn.qlack.util.av.api.dto.VirusScanDTO;
import com.eurodyn.qlack.util.av.api.exception.VirusScanException;

/**
 * Provides Anti-virus file scanning service.
 *
 * @author European Dynamics SA.
 */
public interface AvService {

  /**
   * Scans a file's byte data for viruses
   * @param data the file's byte data array
   * @return a {@link VirusScanDTO} object
   * @throws VirusScanException if any error occurs during file scanning
   */
  VirusScanDTO virusScan(byte[] data) throws VirusScanException;

  /**
   * Checks if the Antivirus server host is up and running.
   * @param avHost the hostname of the server
   * @param avPort the port
   * @return true if the host is up and running, false otherwise
   */
  boolean hostIsAvailable(String avHost, int avPort);

}
