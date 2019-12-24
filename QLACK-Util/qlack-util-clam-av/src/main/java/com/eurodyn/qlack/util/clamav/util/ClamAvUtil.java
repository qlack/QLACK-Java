package com.eurodyn.qlack.util.clamav.util;

import io.sensesecure.clamav4j.ClamAV;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Helper utility class that provides class instances
 *
 * @author European Dynamics
 */
public class ClamAvUtil {

  private ClamAvUtil() {
  }

  /**
   * Provides an instance of {@link ClamAV} class
   *
   * @param address an {@link InetSocketAddress}
   * @param timeout the ClamAV scanning timeout
   * @return a {@link ClamAV} instance
   */
  public static ClamAV getClamAvInstance(InetSocketAddress address,
    int timeout) {
    return new ClamAV(address, timeout);

  }

  /**
   * Provides an instance of {@link Socket} class
   *
   * @param clamAvHost the hostname of ClamAv server
   * @param clamAvPort the port of ClamAv
   * @return a {@link Socket} instance
   * @throws IOException if an I/O error occurs when creating the socket.
   */
  @SuppressWarnings("squid:S4818")
  public static Socket getSocketInstance(String clamAvHost, int clamAvPort)
    throws IOException {
    return new Socket(clamAvHost, clamAvPort);
  }

}
