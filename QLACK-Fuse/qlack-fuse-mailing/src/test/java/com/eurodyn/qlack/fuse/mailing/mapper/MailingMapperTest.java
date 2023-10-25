package com.eurodyn.qlack.fuse.mailing.mapper;

import static org.junit.jupiter.api.Assertions.*;
import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MailingMapperTest {

  @InjectMocks
  private EmailMapperImpl mailingMapper;

  private InitTestValues initTestValues;

  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
  }

  @Test
  public void mapToDate() {
    assertNull(mailingMapper.map(null));
  }

  @Test
  public void mapToDTOTest() {
    assertNull(mailingMapper.mapToDTO((Long) null));
  }

}


