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
  private NonceCachingService nonceCachingService;

  @Mock
  private UserCache userCache;

  private MockHttpServletRequest request;
  private InitTestValues initTestValues;
  private User user;
  private UserDTO userDTO;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
    user = initTestValues.createUser();
    userDTO = initTestValues.userDTO(user);
    request = new MockHttpServletRequest();
    request.addHeader(HttpHeaders.AUTHORIZATION,
      "Bearer eyJhbGciOiJIUzI1NiJ9"
        +
        ".eyJpYXQiOjE1NzQwOTIzMjEsInN1YiI6InRlc3RAdGVzdC5jb20iLCJleHAiOjE1NzQxNzg3MjEsInJvbGUxIjoiVXNlciJ9.2ZRJEW-beEs647zdjwCbIWJLECokxubDZwbhEVeCe7Y");
    ReflectionTestUtils.setField(logoutService, "jwtSecret", "qlackjwtsecret");
    ReflectionTestUtils.setField(logoutService, "jwtExpiration", 3600000);
  }

  @Test(expected = QDoesNotExistException.class)
  public void performLogoutExceptionTest() {
    when(userService.getUserByName(user.getUsername())).thenReturn(null);
    logoutService.performLogout(request);
  }

  @Test
  public void performLogoutTest() {
    when(userService.getUserByName(user.getUsername())).thenReturn(userDTO);
    when(userCaching.getUserCache()).thenReturn(userCache);
    logoutService.performLogout(request);

    verify(userService, times(1))
      .logout(userDTO.getId(), userDTO.getSessionId());
  }

  @Test
  public void performLogoutWithoutSessionIDTest() {
    userDTO.setSessionId(null);

    when(userService.getUserByName(user.getUsername())).thenReturn(userDTO);
    when(userCaching.getUserCache()).thenReturn(userCache);
    logoutService.performLogout(request);

    verify(userService, times(0)).logout(anyString(), anyString());
  }
}
