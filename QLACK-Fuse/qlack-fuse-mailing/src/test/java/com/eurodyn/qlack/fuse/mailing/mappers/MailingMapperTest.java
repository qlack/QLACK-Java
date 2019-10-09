package com.eurodyn.qlack.fuse.mailing.mappers;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class MailingMapperTest {

    @InjectMocks
    private EmailMapperImpl mailingMapper;

    private InitTestValues initTestValues;

    @Before
    public void init() {
        initTestValues = new InitTestValues();
    }

    @Test
    public void mapToDate() {
      assertNull(mailingMapper.map(null));
    }

    @Test
    public void mapToDTOTest(){
        assertNull(mailingMapper.mapToDTO((Long)null));
    }

}


