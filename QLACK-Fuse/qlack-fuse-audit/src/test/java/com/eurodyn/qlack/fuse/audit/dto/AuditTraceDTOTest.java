package com.eurodyn.qlack.fuse.audit.dto;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class AuditTraceDTOTest {

    private AuditTraceDTO auditTraceDTO;

    @Before
    public void init(){
        auditTraceDTO = new AuditTraceDTO();
    }

    @Test
    public void AudiTraceDTOTest(){
        String traceData = "traceData";
        auditTraceDTO = new AuditTraceDTO(traceData);
        assertNotNull(auditTraceDTO);
    }
}
