package com.eurodyn.qlack.fuse.audit.dto;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertNotNull;


@RunWith(MockitoJUnitRunner.class)
public class AuditLevelDTOTest {

    private AuditLevelDTO auditLevelDTO;

    @Before
    public void init(){
        auditLevelDTO = new AuditLevelDTO();
    }

    @Test
    public void AuditLevelDTOTest(){
        String name = "name";
        auditLevelDTO = new AuditLevelDTO(name);
        assertNotNull(auditLevelDTO);

    }

    @Test
    public void testAuditLevelDTO(){
         String id = "id";
         String name = "name";
        auditLevelDTO = new AuditLevelDTO(id,name);
        assertNotNull(auditLevelDTO);
    }
}
