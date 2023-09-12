package com.eurodyn.qlack.fuse.aaa.aop;

import com.eurodyn.qlack.fuse.aaa.annotation.ResourceAccess;
import com.eurodyn.qlack.fuse.aaa.annotation.ResourceId;
import com.eurodyn.qlack.fuse.aaa.annotation.ResourceOperation;
import com.eurodyn.qlack.fuse.aaa.dto.ResourceOperationDTO;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.model.UserGroup;
import com.eurodyn.qlack.fuse.aaa.model.UserGroupHasOperation;
import com.eurodyn.qlack.fuse.aaa.model.UserHasOperation;
import com.eurodyn.qlack.fuse.aaa.service.OperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml2.provider.service.authentication.DefaultSaml2AuthenticatedPrincipal;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Authorizes requests by validating users' role, operation and resources permissions against the provided access rules.
 * The access rules are described using {@link ResourceAccess}, {@link ResourceOperation} and {@link ResourceId} on the
 * REST endpoint the request is issued for or on the corresponding DTO fields that represent a resourceId field
 *
 * @author European Dynamics SA
 */
@Aspect
@Component
@Log
@RequiredArgsConstructor
public class ResourceAccessInterceptor {

  private final ResourceAccessInterceptorService resourceAccessInterceptorService;

  private final OperationService operationService;

  @Pointcut("execution(@com.eurodyn.qlack.fuse.aaa.annotation.ResourceAccess * *(..))")
  public void annotation() {
    //the pointcut declaration
  }

  /**
   * Retrieves User & Group operations from user principal
   *
   * @param user a {@link User} object that holds user information
   * @param operation the operation name to check permissions for
   * @return the operations and resources of the current user
   */
  private List<ResourceOperationDTO> getResourceOperations(User user,
      String operation) {
    List<ResourceOperationDTO> resourceOperations = new ArrayList<>();

    List<UserHasOperation> uho = user.getUserHasOperations();
    List<UserGroupHasOperation> gho = new ArrayList<>();
    user.getUserGroups().forEach(userGroup -> gho.addAll(userGroup.getUserGroupHasOperations()));

    for (UserHasOperation userHasOperation : uho) {
      if (userHasOperation.getOperation().getName().equals(operation)) {
        resourceOperations.add(new ResourceOperationDTO(operation, userHasOperation.getResource() != null
            && userHasOperation.getResource().getObjectId() != null
            ? userHasOperation.getResource().getObjectId() : ""));
      }
    }

    for (UserGroupHasOperation userGroupHasOperation : gho) {
      if (userGroupHasOperation.getOperation().getName()
          .equals(operation)) {
        resourceOperations.add(new ResourceOperationDTO(operation, userGroupHasOperation.getResource() != null
            && userGroupHasOperation.getResource().getObjectId() != null
            ? userGroupHasOperation.getResource().getObjectId() : ""));
      }
    }

    return resourceOperations;
  }

  /**
   * @param user a {@link java.net.UnknownServiceException} object that holds user information
   * @param operation the operation name to check permissions for
   * @param resourceId the value of either resourceIdField or resourceIdParameter
   * @param joinPoint the annotations' joinPoint
   * @param searchInParams flag used for resource level access authorization
   */
  private boolean userHasOperation(User user, String operation,
      String resourceId,
      JoinPoint joinPoint, boolean searchInParams)
      throws IllegalAccessException {

    // Get the user and group operations on resources
    List<ResourceOperationDTO> resourceOperations = getResourceOperations(user,
        operation);

    // If no operations for current user, the user is not authorized
    if (resourceOperations.isEmpty()) {
      return false;
    }

    // If the user has permission for the current operation(s) and resourceId is not provided then authorize the user
    // (Authorization on operation level)
    if (resourceId.isEmpty()) {
      return true;
    }

    // Find match between the parameters (GET, DELETE scenarios) or DTO fields(POST, PUT scenarios)
    // (Authorize on a resource level)
    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    Parameter[] params = method.getParameters();
    int index = 0;
    for (Parameter parameter : params) {
      if (searchInParams) {
        String parameterName = parameter.getName();
        if (parameterName.equals(resourceId)) {
          Object annotationResourceIdValue = joinPoint.getArgs()[index];
          Optional<ResourceOperationDTO> foundResourceOperation = resourceOperations
              .stream()
              .filter(ro -> ro.getResourceId()
                  .equals(String.valueOf(annotationResourceIdValue)))
              .findAny();
          if (foundResourceOperation.isPresent()) {
            return true;
          }
        }
      } else if (userHasOperationIsFound(parameter, resourceId, joinPoint,
          index,
          resourceOperations)) {
        return true;
      }
      index++;
    }
    return false;
  }

  @SuppressWarnings("squid:S3011")
  private boolean userHasOperationIsFound(Parameter parameter,
      String resourceId,
      JoinPoint joinPoint, int index,
      List<ResourceOperationDTO> resourceOperations)
      throws IllegalAccessException {
    Field[] fields = parameter.getType().getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      ResourceId resourceIdAnnotation = field.getAnnotation(ResourceId.class);
      if (resourceIdAnnotation != null) {
        String annotationResourceId = resourceIdAnnotation.value();
        if (!annotationResourceId.equals(resourceId)) {
          continue;
        }
        Object annotationResourceIdValue = field
            .get(joinPoint.getArgs()[index]);
        for (ResourceOperationDTO roDTO : resourceOperations) {
          if (roDTO.getResourceId()
              .equals(String.valueOf(annotationResourceIdValue))) {
            return true;
          }
        }
      }
    }

    return false;
  }

  /**
   * Authorizes user on a role, operation and resources level
   *
   * @param joinPoint the annotations' joinPoint
   * @param resourceAccess The {@link ResourceAccess} object
   * @throws AccessDeniedException if authorization fails
   * @throws IllegalAccessException if a class field cannot be accessed through Java Reflection API
   */
  @Before("annotation() && @annotation(resourceAccess)")
  public void authorize(JoinPoint joinPoint, ResourceAccess resourceAccess)
          throws IllegalAccessException {
      Object principal = SecurityContextHolder.getContext().getAuthentication()
              .getPrincipal();

      //authorizeUserDetailsDTO method checks the fields of the com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO
      //if your security implementation adds another type of object in the spring security principal, a custom implementation must be added
    if (principal instanceof String) {
      User user = resourceAccessInterceptorService.findUser((String) principal);
      if (user != null) {
        authorizeUser(user, joinPoint,
                resourceAccess);
      } else {
        throwUnauthorizedException();
      }
    } else if (principal instanceof DefaultSaml2AuthenticatedPrincipal) {
      User user = resourceAccessInterceptorService.findByUsername((String)((DefaultSaml2AuthenticatedPrincipal) principal).getName());
      if (user != null) {
        authorizeUser(user, joinPoint,
                resourceAccess);
      } else {
        throwUnauthorizedException();
      }
    }else {
      throwUnauthorizedException();
    }
  }

  private void authorizeUser(User user, JoinPoint joinPoint,
      ResourceAccess resourceAccess) throws IllegalAccessException {

    boolean allowAccess;

    // superadmin users are authorized
    if (user.isSuperadmin()) {
      allowAccess = true;
    } else {
      List<UserGroup> groups = user.getUserGroups();
      String[] roleAccess = resourceAccess.roleAccess();

      boolean authorizesOnRole = Arrays.stream(roleAccess).anyMatch(
          new HashSet<>(
              groups.stream().map(UserGroup::getName)
                  .collect(Collectors.toList()))::contains);

      // If not authorized on role level, check specific operation permissions
      if (authorizesOnRole) {
        allowAccess = true;
      } else {
        allowAccess = false;
        log.info(
            "The groups this user is assigned to are not authorized to access this resource.");

        ResourceOperation[] resourceOperations = resourceAccess.operations();
        for (ResourceOperation resourceOperation : resourceOperations) {
          String resourceId = (!resourceOperation.resourceIdField().isEmpty()
              ? resourceOperation
              .resourceIdField()
              : resourceOperation.resourceIdParameter());
          boolean searchInParams = !resourceOperation.resourceIdParameter()
              .isEmpty();
          allowAccess =
              allowAccess || userHasOperation(user, resourceOperation.operation(),
                  resourceId,
                  joinPoint, searchInParams);
        }
      }
    }

    if (!allowAccess) {
      throwUnauthorizedException();
    }
  }

  /**
   * This method throws the QLACK Unauthorized exception
   */
  private void throwUnauthorizedException() {
    throw new AccessDeniedException(
        "403 - Unauthorized Access. This user is not authorized for the specific operation.");

  }
}
