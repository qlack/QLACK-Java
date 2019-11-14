package com.eurodyn.qlack.fuse.security.access;

import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import java.io.Serializable;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Permission expression evaluator for the AAA model.
 *
 * @author EUROPEAN DYNAMICS SA
 */
@Component
public class AAAPermissionEvaluator implements PermissionEvaluator {

  @Override
  public boolean hasPermission(Authentication authentication, Object targetDomainObject,
      Object permission) {
    if (authentication == null || targetDomainObject == null || !(permission instanceof String)) {
      return false;
    }

    String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();

    return hasPrivilege(authentication, targetType, permission.toString());
  }

  @Override
  public boolean hasPermission(Authentication authentication, Serializable targetId,
      String targetType, Object permission) {
    if (authentication == null || targetType == null || !(permission instanceof String)) {
      return false;
    }

    return hasPrivilege(authentication, targetType.toUpperCase(), permission.toString());
  }

  private boolean hasPrivilege(Authentication auth, String targetType, String permission) {
    UserDetailsDTO user = (UserDetailsDTO) auth.getPrincipal();

    // If user has no such operation, check in group operations.
    return user.getUserGroupHasOperations().stream()
        .anyMatch(gho -> gho.getOperationDTO().getName().equalsIgnoreCase(permission.toUpperCase())
            && !gho.isDeny());
  }

}
