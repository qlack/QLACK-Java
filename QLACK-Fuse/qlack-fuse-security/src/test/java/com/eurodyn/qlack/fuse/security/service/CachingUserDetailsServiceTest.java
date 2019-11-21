package com.eurodyn.qlack.fuse.security.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.security.InitTestValues;
import com.eurodyn.qlack.fuse.security.cache.AAAUserCaching;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@RunWith(MockitoJUnitRunner.class)
public class CachingUserDetailsServiceTest {

  @InjectMocks
  private CachingUserDetailsService cachingUserDetailsService;

  @Mock
  private AAAUserCaching userCaching;

  @Mock
  private UserDetailsService delegate;

  @Mock
  private UserCache userCache;

  private InitTestValues initTestValues;
  private User user;
  private UserDetailsDTO userDetailsDTO;

  private String USERNAME;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
    user = initTestValues.createUser();
    userDetailsDTO = initTestValues.createUserDetailDTO(user);
    initTestValues = new InitTestValues();
    cachingUserDetailsService = new CachingUserDetailsService(userCaching, delegate);
    USERNAME = user.getUsername();

    when(userCaching.getUserCache()).thenReturn(userCache);
  }

  @Test
  public void removeUserTest() {
    cachingUserDetailsService.removeUser(USERNAME);
    verify(userCache, times(1)).removeUserFromCache(USERNAME);
  }

  @Test
  public void getUserCacheTest() {
    UserCache actualUserCache = cachingUserDetailsService.getUserCache();
    verify(userCaching, times(1)).getUserCache();
  }

  @Test
  public void loadUserByUsernameNullUserTest() {
    when(userCache.getUserFromCache(USERNAME)).thenReturn(null);
    when(delegate.loadUserByUsername(USERNAME)).thenReturn(null);

    UserDetails actualUserDetails = cachingUserDetailsService.loadUserByUsername(USERNAME);

    verify(delegate, times(1)).loadUserByUsername(USERNAME);
    verify(userCache, times(0)).putUserInCache(any(UserDetails.class));
    assertNull(actualUserDetails);
  }

  @Test
  public void loadUserByUsernameTest() {
    when(userCache.getUserFromCache(USERNAME)).thenReturn(userDetailsDTO);

    UserDetails actuaUserDetails = cachingUserDetailsService.loadUserByUsername(USERNAME);

    verify(delegate, times(0)).loadUserByUsername(USERNAME);
    verify(userCache, times(1)).putUserInCache(userDetailsDTO);
    assertEquals(userDetailsDTO, actuaUserDetails);
  }
}
