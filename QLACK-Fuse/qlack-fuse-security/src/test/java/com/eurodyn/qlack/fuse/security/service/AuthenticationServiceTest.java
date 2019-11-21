package com.eurodyn.qlack.fuse.security.service;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.service.UserService;
import com.eurodyn.qlack.fuse.security.InitTestValues;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticationServiceTest {

  @InjectMocks
  private AuthenticationService authenticationService;

  @Mock
  private AuthenticationProvider authenticationProvider;

  @Mock
  private UserService userService;

  @Mock
  private Authentication authentication;

  private InitTestValues initTestValues;
  private User user;
  private UserDTO userDTO;
  private UserDetailsDTO userDetailsDTO;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
    user = initTestValues.createUser();
    userDTO = initTestValues.userDTO(user);
    userDetailsDTO = initTestValues.createUserDetailDTO(user);
    ReflectionTestUtils.setField(authenticationService, "jwtSecret", "randomString");
    ReflectionTestUtils.setField(authenticationService, "jwtExpiration", 3600);
    ReflectionTestUtils.setField(authenticationService, "jwtIncludeRoles", false);
  }

  private void commonMocks() {
    when(authentication.getPrincipal()).thenReturn(userDetailsDTO);
    when(userService.login(user.getId(), userDetailsDTO.getSessionId(), true)).
        thenReturn(userDTO);

  }

  @Test
  public void authenticateTest() {
    when(authenticationProvider.authenticate(authentication)).thenReturn(authentication);
    commonMocks();
    String generatedJWT = authenticationService
        .authenticate(authentication, userDetailsDTO.getSessionId());

    assertTrue(generatedJWT.contains("Bearer"));
  }

  @Test
  public void authenticateWithUserDetailsDTOTest() {
    when(authenticationProvider.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    commonMocks();
    String generatedJWT = authenticationService.authenticate(userDetailsDTO);

    assertTrue(generatedJWT.contains("Bearer"));
  }

  @Test
  public void authenticateWithUserDTOAndRolesIncluded() {
    ReflectionTestUtils.setField(authenticationService, "jwtIncludeRoles", true);
    when(authenticationProvider.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(authentication);
    commonMocks();
    String generatedJWT = authenticationService.authenticate(userDetailsDTO);

    assertTrue(generatedJWT.contains("Bearer"));
  }
}
