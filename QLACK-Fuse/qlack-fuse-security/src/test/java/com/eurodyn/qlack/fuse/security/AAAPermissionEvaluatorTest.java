package com.eurodyn.qlack.fuse.security;

import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserGroupDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserGroupHasOperationDTO;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.security.access.AAAPermissionEvaluator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;

import java.io.Serializable;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.intThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AAAPermissionEvaluatorTest {

  @InjectMocks private AAAPermissionEvaluator aaaPermissionEvaluator;

  @Mock private Authentication authentication;
  @Mock private Object targetDomainObject;
  @Mock private User user;
  @Mock private UserDetailsDTO userDetailsDTO;
  @Mock private Serializable serializable;

  private String permission;
  private String targetType;
  private InitTestValues initTestValues;

  @Before
  public void init(){
    initTestValues = new InitTestValues();
    aaaPermissionEvaluator = new AAAPermissionEvaluator();
    user = initTestValues.createUser();
    userDetailsDTO = initTestValues.createUserDetailDTO(user);
    permission = "Test operation";
    targetType = "targetType";
  }

  @Test
  public void hasPermissionFalseTest(){
    assertFalse(aaaPermissionEvaluator.hasPermission(authentication, targetDomainObject, null));
    assertFalse(aaaPermissionEvaluator.hasPermission(null, targetDomainObject, null));
    assertFalse(aaaPermissionEvaluator.hasPermission(null, null, null));
    assertFalse(aaaPermissionEvaluator.hasPermission(authentication, null, null));
  }

  @Test
  public void hasPermissionNoGroupTest(){
    permission = "permission";
    when(authentication.getPrincipal()).thenReturn(userDetailsDTO);
    assertFalse(aaaPermissionEvaluator.hasPermission(authentication, targetDomainObject, permission));
  }

  @Test
  public void hasPermissionTest(){
    when(authentication.getPrincipal()).thenReturn(userDetailsDTO);
    assertTrue(aaaPermissionEvaluator.hasPermission(authentication, targetDomainObject, permission));
  }

  @Test
  public void hasPermissionTargetIdFalseTest(){
    assertFalse(aaaPermissionEvaluator.hasPermission(authentication, serializable, targetType, null));
    assertFalse(aaaPermissionEvaluator.hasPermission(null, serializable, targetType, null));
    assertFalse(aaaPermissionEvaluator.hasPermission(null, serializable, null, null));
    assertFalse(aaaPermissionEvaluator.hasPermission(authentication, serializable, null, null));
  }

  @Test
  public void hasPermissionTargetIdTest(){
    userDetailsDTO.getUserGroupHasOperations().forEach(userGroupHasOperationDTO -> userGroupHasOperationDTO.setDeny(true));
    when(authentication.getPrincipal()).thenReturn(userDetailsDTO);
    assertFalse(aaaPermissionEvaluator.hasPermission(authentication, serializable, targetType, permission));
  }
}
