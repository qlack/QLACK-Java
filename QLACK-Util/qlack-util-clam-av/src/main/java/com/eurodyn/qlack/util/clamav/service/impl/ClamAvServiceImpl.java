package com.eurodyn.qlack.util.clamav.service.impl;

/**
 * @author European Dynamics
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Objects;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eurodyn.qlack.util.av.api.dto.VirusScanDTO;
import com.eurodyn.qlack.util.av.api.exception.VirusScanException;
import com.eurodyn.qlack.util.av.api.service.AvService;
import com.eurodyn.qlack.util.clamav.util.ClamAvProperties;

import io.sensesecure.clamav4j.ClamAV;
import io.sensesecure.clamav4j.ClamAVException;
import lombok.extern.java.Log;

/**
 * @author European Dynamics
 */
@Log
@Service
public class ClamAvServiceImpl implements AvService {

  private ClamAvProperties properties;

  @Autowired
  public ClamAvServiceImpl(ClamAvProperties properties) {
    this.properties = properties;
  }

  /**
   * Sends file data for scanning to an open socket of
   * the ClamAV antivirus server instance as a
   * {@link ByteArrayInputStream}
   *
   * @param data a {@link java.lang.Byte} array containing file data to be scanned
   *
   * @return {@link com.eurodyn.qlack.util.clamav.util.ClamAvProperties} the scanning result
   */
  public VirusScanDTO virusScan(byte[] data) throws VirusScanException {
    Objects.requireNonNull(properties.getClamAvHost(), "The hostname can't be null. Please provide a valid antivirus "
      + "server hostname.");

    // Check antivirus server instance is running
    if (!hostIsAvailable(properties.getClamAvHost(), properties.getClamAvPort())) {
      throw new VirusScanException("Could not connect to Clam AV instance");
    }

    VirusScanDTO vsDTO = new VirusScanDTO();
    InetSocketAddress clamAVAddress = new InetSocketAddress(properties.getClamAvHost(), properties.getClamAvPort());

    log.log(Level.FINE, "Contacting ClamAV at: {0}.", clamAVAddress);
    ClamAV clamAV = new ClamAV(clamAVAddress, properties.getClamAvSocketTimeout());
    String scanResult;

    try (ByteArrayInputStream bis = new ByteArrayInputStream(data)) {
      log.log(Level.INFO, "Scanning file for viruses..");
      scanResult = clamAV.scan(bis);
    } catch (IOException | ClamAVException e) {
      log.log(Level.SEVERE, "Could not check file for virus", e);
      throw new VirusScanException("Could not check file for virus");
    }

    log.log(Level.INFO, "No threats were found.");
    vsDTO.setVirusFree(scanResult.equals("OK"));
    vsDTO.setVirusScanDescription(scanResult);

    return vsDTO;
  }

  public boolean hostIsAvailable(String clamAvHost, int clamAvPort) {
    log.log(Level.INFO, "Checking for AntiVirus server availability..");
    try (Socket s = new Socket(clamAvHost, clamAvPort)) {
      log.log(Level.INFO, "AntiVirus server is up and running.");
      return true;
    } catch (IOException ex) {
      return false;
    }
  }
}
