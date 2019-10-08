package com.eurodyn.qlack.fuse.acv.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.eurodyn.qlack.fuse.acv.dto.ChangeDTO;
import com.eurodyn.qlack.fuse.acv.PersonDTO;
import com.eurodyn.qlack.fuse.acv.RoleDTO;
import java.util.List;
import org.javers.core.JaversBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CompareServiceTest {

  CompareService service;

  @Before
  public void init() {
    service = new CompareService(JaversBuilder.javers().build(),
        new VersioningService(JaversBuilder.javers().build()));
  }

  @Test
  public void testCompare() {

    PersonDTO dto1 = createPersonDTO("username", null, 22);
    RoleDTO roleDTO2 = new RoleDTO();
    roleDTO2.setPosition("manager");
    roleDTO2.setSalary(1000);
    dto1.setRole(roleDTO2);

    PersonDTO dto2 = createPersonDTO("username", "username@european.com", 33);
    RoleDTO roleDTO = new RoleDTO();
    roleDTO.setPosition("manager");
    roleDTO.setSalary(2000);
    dto2.setRole(roleDTO);

    List<ChangeDTO> changes = service.compare(dto1, dto2);

    for (ChangeDTO changeDTO : changes) {
      System.out.println(changeDTO);
    }

    assertEquals(3, changes.size());

  }

  @Test
  public void testHasChanges() {

    PersonDTO dto1 = createPersonDTO("username", null, 22);
    PersonDTO dto2 = createPersonDTO("username", "username@european.com", 33);

    boolean hasChanges = service.hasChanges(dto1, dto2);

    assertTrue(hasChanges);

  }

  private PersonDTO createPersonDTO(String username, String email, int age) {
    PersonDTO dto = new PersonDTO();
    dto.setName(username);
    dto.setEmail(email);
    dto.setAge(age);
    return dto;
  }

}
