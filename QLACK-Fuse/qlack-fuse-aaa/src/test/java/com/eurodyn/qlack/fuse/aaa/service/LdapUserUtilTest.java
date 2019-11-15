package com.eurodyn.qlack.fuse.aaa.service;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.repository.UserAttributeRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserGroupRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import com.eurodyn.qlack.fuse.aaa.util.LdapProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class LdapUserUtilTest {

  @InjectMocks
  private LdapUserUtil ldapUserUtil;

  @Mock
  private User user;
  @Mock
  private UserService userService;
  @Mock
  private UserDTO userDTO;

  @Mock
  private LdapProperties ldapProperties;
  private InitTestValues initTestValues;
  private UserRepository userRepository;
  private UserGroupRepository userGroupRepository;
  private UserAttributeRepository userAttributeRepository;

  @Before
  public void init() {
    ldapUserUtil = new LdapUserUtil(ldapProperties, userRepository, userGroupRepository,
        userAttributeRepository);
    initTestValues = new InitTestValues();
    user = initTestValues.createUser();
    userDTO = initTestValues.createUserDTO();
  }

  @Test
  public void testCanAuthenticateNull() {
    ldapUserUtil.canAuthenticate(user.getUsername(), user.getPassword());
    assertFalse(ldapUserUtil.getProperties().isEnabled());
  }

  @Test
  public void testCanAuthenticate() {
    userService.createUser(userDTO, null);
    ldapUserUtil.canAuthenticate(userDTO.getUsername(), userDTO.getPassword());
    assertTrue(!ldapUserUtil.getProperties().isEnabled());
  }
}
