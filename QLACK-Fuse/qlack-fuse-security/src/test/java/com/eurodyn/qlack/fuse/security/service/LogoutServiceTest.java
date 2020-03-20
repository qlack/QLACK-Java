package com.eurodyn.qlack.fuse.security.service;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.service.UserService;
import com.eurodyn.qlack.fuse.security.InitTestValues;
import com.eurodyn.qlack.fuse.security.cache.AAAUserCaching;
import com.eurodyn.qlack.fuse.security.util.CachedUserUtil;
import com.eurodyn.qlack.util.jwt.JWTUtil;
import com.eurodyn.qlack.util.jwt.dto.JWTGenerateRequestDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class LogoutServiceTest {

  @InjectMocks
  private LogoutService logoutService;

  @Mock
  private UserService userService;

  @Mock
  private AAAUserCaching userCaching;

  @Mock
  private CachedUserUtil cachedUserUtil;

  private MockHttpServletRequest request;
  private InitTestValues initTestValues;
  private User user;
  private UserDTO userDTO;

  private final String jwtSecret = "qlackjwtsecret";
  private final int jwtExpiration = 3600000;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
    user = initTestValues.createUser();
    userDTO = initTestValues.userDTO(user);
    request = new MockHttpServletRequest();

    String jwt = JWTUtil.generateToken(new JWTGenerateRequestDTO(jwtSecret, user.getUsername(),
        jwtExpiration));

    request.addHeader(HttpHeaders.AUTHORIZATION, jwt);
    ReflectionTestUtils.setField(logoutService, "jwtSecret", jwtSecret);
    ReflectionTestUtils.setField(logoutService, "jwtExpiration", jwtExpiration);
  }

  @Test(expected = QDoesNotExistException.class)
  public void performLogoutExceptionTest() {
    when(userService.getUserByName(user.getUsername())).thenReturn(null);
    logoutService.performLogout(request);
  }

  @Test
  public void performLogoutTest() {
    when(userService.getUserByName(user.getUsername())).thenReturn(userDTO);
    logoutService.performLogout(request);

    verify(userService, times(1))
      .logout(userDTO.getId(), userDTO.getSessionId());
  }

  @Test
  public void performLogoutWithoutSessionIDTest() {
    userDTO.setSessionId(null);

    when(userService.getUserByName(user.getUsername())).thenReturn(userDTO);
    logoutService.performLogout(request);

    verify(userService, times(0)).logout(anyString(), anyString());
  }
}
