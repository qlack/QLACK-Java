package com.eurodyn.qlack.util.clamav.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.eurodyn.qlack.util.av.api.exception.VirusScanException;
import com.eurodyn.qlack.util.clamav.InitTestValues;
import com.eurodyn.qlack.util.clamav.service.impl.ClamAvServiceImpl;
import com.eurodyn.qlack.util.clamav.util.ClamAvProperties;
import io.sensesecure.clamav4j.ClamAV;

import java.io.ByteArrayInputStream;
import java.net.Socket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
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

  private MockedConstruction<ClamAV> mockedClamAV1;

  private MockedConstruction<Socket> mockedClamAV2;


  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
    properties = initTestValues.createProperties();
    clamAvService = new ClamAvServiceImpl(properties);
    data = initTestValues.createData();
    mock(ClamAvServiceImpl.class);
  }

  @Test
  public void testVirusScanHostNameIsNull() {
    assertThrows(NullPointerException.class, () -> {
      properties.setClamAvHost(null);
      clamAvService.virusScan(data);
    });
  }

  @Test
  public void testVirusScanHostNotAvailable() {
    assertThrows(VirusScanException.class, () -> {
      clamAvService.virusScan(data);
    });
  }

  @Test
  public void testVirusScanIOException(){
    ClamAvServiceImpl spyObj = spy(clamAvService);
    when(spyObj
            .hostIsAvailable(properties.getClamAvHost(), properties.getClamAvPort()))
            .thenReturn(true);

    mockedClamAV1 = Mockito.mockConstruction(ClamAV.class,
            (mock, context) -> {
              when(mock.scan(ArgumentMatchers.any(ByteArrayInputStream.class))).thenReturn("OK");
            });

    assertTrue(spyObj.virusScan(data).isVirusFree());
  }

  @Test
  public void testVirusScanHostIsAvailable() {
    assertThrows(VirusScanException.class, () -> {
      ClamAvServiceImpl spyObj = spy(clamAvService);
      when(spyObj
              .hostIsAvailable(properties.getClamAvHost(), properties.getClamAvPort()))
              .thenReturn(true);
      spyObj.virusScan(data);
    });
  }
/*
  @Test
  public void hostIsAvailableTest() throws Exception {
//    whenNew(Socket.class).withArguments(properties.getClamAvHost(),
//            properties.getClamAvPort()).thenReturn(socketMock);

    assertTrue(clamAvService.hostIsAvailable(properties.getClamAvHost(),
            properties.getClamAvPort()));
  }
*/
}

