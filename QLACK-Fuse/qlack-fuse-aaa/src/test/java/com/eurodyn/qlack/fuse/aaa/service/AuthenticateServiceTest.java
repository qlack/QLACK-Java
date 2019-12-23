package com.eurodyn.qlack.fuse.aaa.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.mapper.UserDetailsMapper;
import com.eurodyn.qlack.fuse.aaa.mapper.UserGroupHasOperationMapper;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AuthenticateServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private UserDetailsMapper userDetailsMapper;
  @Mock
  private UserGroupHasOperationMapper userGroupHasOperationMapper;

  private AuthenticateService authenticateService;
  private InitTestValues initTestValues;
  private User user;

  @Before
  public void init() {
    authenticateService = new AuthenticateService(userRepository,
      userDetailsMapper,
      userGroupHasOperationMapper);
    initTestValues = new InitTestValues();
    user = initTestValues.createUser();
  }

  @Test
  public void testLoadUserByUsername() {
    authenticateService.loadUserByUsername(user.getUsername());
    verify(userRepository, times(1)).findByUsername(any());
  }
}
