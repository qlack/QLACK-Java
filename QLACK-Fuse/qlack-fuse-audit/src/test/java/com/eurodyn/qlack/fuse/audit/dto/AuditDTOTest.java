package com.eurodyn.qlack.fuse.audit.dto;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.MessageFormat;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class AuditDTOTest {

    private AuditDTO auditDTO;

    @Before
    public void init(){
        auditDTO = new AuditDTO();
    }

    @Test
    public void testSetShortDescription(){
        int fileCount = 1273;
        String diskName = "MyDisk";
        String message = "The disk \"{1}\" contains {0} file(s).";
        Object[] testArgs = {new Long(fileCount), diskName};

        MessageFormat messageFormat = new MessageFormat(message);
        auditDTO.setShortDescription(message,messageFormat.format(testArgs));
        assertNotNull(auditDTO.getShortDescription(),messageFormat.format(testArgs));
    }

}
