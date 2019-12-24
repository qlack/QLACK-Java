package com.eurodyn.qlack.util.clamav.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import com.eurodyn.qlack.util.av.api.exception.VirusScanException;
import com.eurodyn.qlack.util.clamav.InitTestValues;
import com.eurodyn.qlack.util.clamav.service.impl.ClamAvServiceImpl;
import com.eurodyn.qlack.util.clamav.util.ClamAvProperties;
import com.eurodyn.qlack.util.clamav.util.ClamAvUtil;
import io.sensesecure.clamav4j.ClamAV;
import java.io.ByteArrayInputStream;
import java.net.Socket;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;


@RunWith(PowerMockRunner.class)
@PrepareForTest(ClamAvUtil.class)
public class ClamAvServiceTest {

  @InjectMocks
  private ClamAvServiceImpl clamAvService;

  private ClamAvProperties properties;

  private InitTestValues initTestValues;
  private byte[] data;

  @Mock
  private ClamAV clamAVMock;

  @Mock
  private Socket socketMock;


  @Before
  public void init() {
    initTestValues = new InitTestValues();
    properties = initTestValues.createProperties();
    clamAvService = new ClamAvServiceImpl(properties);
    data = initTestValues.createData();
    PowerMockito.mock(ClamAvServiceImpl.class);
  }

  @Test(expected = NullPointerException.class)
  public void testVirusScanHostNameIsNull() {
    properties.setClamAvHost(null);
    clamAvService.virusScan(data);
  }

  @Test(expected = VirusScanException.class)
  public void testVirusScanHostNotAvailable() {
    clamAvService.virusScan(data);
  }

  @Test
  public void testVirusScanIOException() throws Exception {
    ClamAvServiceImpl spyObj = spy(clamAvService);
    when(spyObj
      .hostIsAvailable(properties.getClamAvHost(), properties.getClamAvPort()))
      .thenReturn(true);
    whenNew(ClamAV.class).withAnyArguments().thenReturn(clamAVMock);
    when(clamAVMock.scan(ArgumentMatchers.any(ByteArrayInputStream.class)))
      .thenReturn("OK");
    assertTrue(spyObj.virusScan(data).isVirusFree());
  }

  @Test(expected = VirusScanException.class)
  public void testVirusScanHostIsAvailable() {
    ClamAvServiceImpl spyObj = spy(clamAvService);
    when(spyObj
      .hostIsAvailable(properties.getClamAvHost(), properties.getClamAvPort()))
      .thenReturn(true);
    spyObj.virusScan(data);
  }

  @Test
  public void hostIsAvailableTest() throws Exception {
    whenNew(Socket.class).withArguments(properties.getClamAvHost(),
      properties.getClamAvPort()).thenReturn(socketMock);

    assertTrue(clamAvService.hostIsAvailable(properties.getClamAvHost(),
      properties.getClamAvPort()));
  }

}

