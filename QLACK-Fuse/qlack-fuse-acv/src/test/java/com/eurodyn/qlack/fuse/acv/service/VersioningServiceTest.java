package com.eurodyn.qlack.fuse.acv.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.eurodyn.qlack.fuse.acv.dto.ChangeDTO;
import com.eurodyn.qlack.fuse.acv.PersonDTO;
import com.eurodyn.qlack.fuse.acv.RoleDTO;
import com.eurodyn.qlack.fuse.acv.dto.VersionDTO;
import java.util.List;
import org.javers.core.Javers;
import org.javers.core.JaversBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class VersioningServiceTest {

  private VersioningService service;

  private CompareService compareService;

  @Before
  public void init() {
    Javers javers = JaversBuilder.javers().build();
    this.service = new VersioningService(javers);
    this.compareService = new CompareService(javers, service);
  }

  @Test
  public void testVersionService() {

    // create first version
   PersonDTO personDTO = createPersonDTO("username", null, 22);

    long commitId = service.createVersion("user1", personDTO, "message 1");
    assertEquals(1, commitId);

    // create second version
    personDTO.getRole().setPosition("senior_developer");
    personDTO.getRole().setSalary(2000);

    commitId = service.createVersion("apo", personDTO, "message 2");
    assertEquals(2, commitId);

    // find all versions
    List<VersionDTO> versions = service.findVersions(personDTO);
    assertEquals(2, versions.size());

    // retrieve a specific version
    PersonDTO dto = service.retrieveVersion(personDTO, 2);
    assertNotNull(dto);
    assertEquals("senior_developer", dto.getRole().getPosition());
    assertEquals(2000, dto.getRole().getSalary());

    // one year passed
    personDTO.setAge(personDTO.getAge() + 1);

    // compare modified object with specific version
    List<ChangeDTO> changes = compareService.compareObjectWithVersion(personDTO, 1);
    assertEquals(3, changes.size());

    // compare modified object with latest commited version
    changes = compareService.compareObjectWithLatestVersion(personDTO);
    assertEquals(1, changes.size());

    // compare two object versions
    changes = compareService.compareVersions(personDTO, 1, 2);
    assertEquals(2, changes.size());

  }

  private PersonDTO createPersonDTO(String username, String email, int age) {

    PersonDTO dto = new PersonDTO();
    dto.setName(username);
    dto.setEmail(email);
    dto.setAge(age);

    RoleDTO roleDTO = new RoleDTO();
    roleDTO.setPosition("developer");
    roleDTO.setSalary(1000);
    dto.setRole(roleDTO);

    return dto;
  }

}
