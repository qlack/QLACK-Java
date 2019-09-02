package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.naming.NamingException;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

@RunWith(MockitoJUnitRunner.class)
public class LdapUserUtilTest {

  @InjectMocks private LdapUserUtil ldapUserUtil;

  @Mock private User user;
  @Mock private UserService userService;
  @Mock private UserDTO userDTO;

  private InitTestValues initTestValues;
  private UserRepository userRepository;
  private String ldapUrl;
  private String ldapMappingUid;
  private String ldapBaseDN;


  @Before
  public void init() {
    ldapUserUtil = new LdapUserUtil(userRepository);
    initTestValues = new InitTestValues();
    user = initTestValues.createUser();
    userDTO = initTestValues.createUserDTO();
    ldapUrl = "ldap://localhost:389";
    ldapMappingUid = "uid";
    ldapBaseDN = "dc=example,dc=com";
  }

  @Test
  public void testCanAuthenticateNull(){
    ldapUserUtil.canAuthenticate(user.getUsername(), user.getPassword());
    assertFalse(ldapUserUtil.isLdapEnable());
  }

  @Test
  public void testCanAuthenticate(){
    ldapUserUtil.setLdapEnable(true);
    ldapUserUtil.setLdapUrl(ldapUrl);
    ldapUserUtil.setLdapBaseDN(ldapBaseDN);
    ldapUserUtil.setLdapMappingUid(ldapMappingUid);
    userService.createUser(userDTO, null);
    ldapUserUtil.canAuthenticate(userDTO.getUsername(), userDTO.getPassword());
    assertTrue(ldapUserUtil.isLdapEnable());
  }
}
