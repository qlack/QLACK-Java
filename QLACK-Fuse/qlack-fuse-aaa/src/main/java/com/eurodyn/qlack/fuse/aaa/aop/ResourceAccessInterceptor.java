package com.eurodyn.qlack.fuse.aaa.aop;

import com.eurodyn.qlack.fuse.aaa.annotation.OperationAccess;
import com.eurodyn.qlack.fuse.aaa.annotation.ResourceAccess;
import com.eurodyn.qlack.fuse.aaa.annotation.ResourceId;
import com.eurodyn.qlack.fuse.aaa.dto.ResourceOperationDTO;
import com.eurodyn.qlack.fuse.aaa.model.*;
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

import java.lang.reflect.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Authorizes requests by validating users' role, operation and resources permissions against the provided access rules.
 * The access rules are described using {@link ResourceAccess}, {@link OperationAccess} and {@link ResourceId} on the
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

  /**
   * List of allowed types as method parameters with ResourceId
   */
  private static final List<Class<?>> WRAPPER_TYPE_LIST;
  static {
    WRAPPER_TYPE_LIST = new ArrayList<>();
    WRAPPER_TYPE_LIST.add(int.class);
    WRAPPER_TYPE_LIST.add(byte.class);
    WRAPPER_TYPE_LIST.add(char.class);
    WRAPPER_TYPE_LIST.add(boolean.class);
    WRAPPER_TYPE_LIST.add(double.class);
    WRAPPER_TYPE_LIST.add(float.class);
    WRAPPER_TYPE_LIST.add(long.class);
    WRAPPER_TYPE_LIST.add(short.class);
    WRAPPER_TYPE_LIST.add(void.class);
    WRAPPER_TYPE_LIST.add(String.class);
    WRAPPER_TYPE_LIST.add(Integer.class);
    WRAPPER_TYPE_LIST.add(Boolean.class);
    WRAPPER_TYPE_LIST.add(Double.class);
    WRAPPER_TYPE_LIST.add(Float.class);
    WRAPPER_TYPE_LIST.add(Long.class);

  }

  @Pointcut("execution(@com.eurodyn.qlack.fuse.aaa.annotation.ResourceAccess * *(..)) || execution(@com.eurodyn.qlack.fuse.aaa.annotation.OperationAccess * *(..))")
  public void annotation() {
    //the pointcut declaration
  }

  /**
   * Authorizes user on a role or operation level, without resource
   *
   * @param joinPoint the annotations' joinPoint
   * @param operationAccess The {@link OperationAccess} object
   * @throws AccessDeniedException if authorization fails
   * @throws IllegalAccessException if a class field cannot be accessed through Java Reflection API
   */
  @Before("annotation() && @annotation(operationAccess)")
  public void authorizeOperation(JoinPoint joinPoint, OperationAccess operationAccess)
          throws IllegalAccessException {
    authorize(joinPoint, operationAccess.roleAccess(), operationAccess.operations(), false);
  }

  /**
   * Authorizes user on an operation with resources level
   *
   * @param joinPoint the annotations' joinPoint
   * @param resourceAccess The {@link ResourceAccess} object
   * @throws AccessDeniedException if authorization fails
   * @throws IllegalAccessException if a class field cannot be accessed through Java Reflection API
   */
  @Before("annotation() && @annotation(resourceAccess)")
  public void authorizeResource(JoinPoint joinPoint, ResourceAccess resourceAccess) throws IllegalAccessException {
    authorize(joinPoint, new String[0], resourceAccess.operations(), true);
  }

  /**
   * Retrieves user details and checks authorization
   *
   * @param joinPoint the annotations' joinPoint
   * @param roleAccess The roleAccess array
   * @param resourceOperations The resourceOperations array
   * @param withResourceId Whether to check for resource or not
   * @throws AccessDeniedException if authorization fails
   * @throws IllegalAccessException if a class field cannot be accessed through Java Reflection API
   */
  public void authorize(JoinPoint joinPoint, String[] roleAccess, String[] resourceOperations, boolean withResourceId)
          throws IllegalAccessException {
    Object principal = SecurityContextHolder.getContext().getAuthentication()
            .getPrincipal();

    //authorizeUserDetailsDTO method checks the fields of the com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO
    //if your security implementation adds another type of object in the spring security principal, a custom implementation must be added
    User user = null;
    if (principal instanceof String) {
      user = resourceAccessInterceptorService.findUser((String) principal);
    } else if (principal instanceof DefaultSaml2AuthenticatedPrincipal) {
      user = resourceAccessInterceptorService.findByUsername((String) ((DefaultSaml2AuthenticatedPrincipal) principal).getName());
    }
    if (user != null) {
      authorizeUser(user, joinPoint,
              roleAccess, resourceOperations, withResourceId);
    } else {
      throwUnauthorizedException();
    }
  }

  /**
   * Authorizes user on a role, operation or resource level
   *
   * @param user the User
   * @param joinPoint the annotations' joinPoint
   * @param roleAccess The roleAccess array
   * @param resourceOperations The resourceOperations array
   * @param withResourceId Whether to check for resource or not
   * @throws AccessDeniedException if authorization fails
   * @throws IllegalAccessException if a class field cannot be accessed through Java Reflection API
   */
  private void authorizeUser(User user, JoinPoint joinPoint,
                             String[] roleAccess, String[] resourceOperations,
                             boolean withResourceId) throws IllegalAccessException {

    boolean allowAccess;

    // superadmin users are authorized
    if (user.isSuperadmin()) {
      allowAccess = true;
    } else {
      List<UserGroup> groups = user.getUserGroups();

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

        for (String resourceOperation : resourceOperations) {
          allowAccess =
                  allowAccess || userHasOperation(user, resourceOperation, joinPoint, withResourceId);
        }
      }
    }

    if (!allowAccess) {
      throwUnauthorizedException();
    }
  }

  /**
   * Checks if user has the requested operation
   *
   * @param user a {@link java.net.UnknownServiceException} object that holds user information
   * @param operation the operation name to check permissions for
   * @param joinPoint the annotations' joinPoint
   * @param withResourceId Whether to check for resource or not
   * @throws IllegalAccessException if a class field cannot be accessed through Java Reflection API
   */
  private boolean userHasOperation(User user, String operation,
                                   JoinPoint joinPoint, boolean withResourceId)
          throws IllegalAccessException {

    // Get the user and group operations on resources
    List<ResourceOperationDTO> resourceOperations = getResourceOperations(user,
            operation, withResourceId);

    // If no operations for current user, the user is not authorized
    if (resourceOperations.isEmpty()) {
      return false;
    }

    // If operations found for current user and authorization level is on operation without objectId, the user is authorized
    if (!withResourceId) {
      return true;
    }

    if (findResourceInParameters(joinPoint, resourceOperations)) {
      return true;
    }

    // Find match between the parameters (GET, DELETE scenarios) or DTO fields(POST, PUT scenarios)
    // (Authorize on a resource level)
    Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
    Parameter[] params = method.getParameters();
    int index = 0;
    for (Parameter parameter : params) {
      if (!WRAPPER_TYPE_LIST.contains(parameter.getType())
              && userHasOperationWithResourceID(parameter, joinPoint,
              index,
              resourceOperations)) {
        return true;
      }
      index++;
    }
    return false;
  }

  /**
   * Retrieves User & Group operations from user principal
   *
   * @param user a {@link User} object that holds user information
   * @param operation the operation name to check permissions for
   * @param withResourceId filters the operations whether they should or not have a resource level
   * @return the operations and resources of the current user
   */
  private List<ResourceOperationDTO> getResourceOperations(User user,
      String operation, boolean withResourceId) {
    List<ResourceOperationDTO> resourceOperations;

    List<UserHasOperation> uho = user.getUserHasOperations();
    List<UserGroupHasOperation> gho = new ArrayList<>();
    user.getUserGroups().forEach(userGroup -> gho.addAll(userGroup.getUserGroupHasOperations()));

    if (withResourceId) {
      resourceOperations = uho.stream().filter(o -> o.getOperation().getName().equals(operation) &&
              o.getResource() != null && o.getResource().getObjectId() != null)
              .map(o -> new ResourceOperationDTO(o.getOperation().getName(), o.getResource().getObjectId()))
              .collect(Collectors.toList());
      resourceOperations.addAll(gho.stream().filter(o -> o.getOperation().getName().equals(operation) &&
              o.getResource() != null && o.getResource().getObjectId() != null)
              .map(o -> new ResourceOperationDTO(o.getOperation().getName(), o.getResource().getObjectId()))
              .collect(Collectors.toList()));
    } else {
      resourceOperations = uho.stream().filter(o -> o.getOperation().getName().equals(operation)
                      && (o.getResource() == null || (o.getResource() != null && o.getResource().getObjectId() == null)))
              .map(o -> new ResourceOperationDTO(o.getOperation().getName(), ""))
              .collect(Collectors.toList());
      resourceOperations.addAll(gho.stream().filter(o -> o.getOperation().getName().equals(operation)
              && (o.getResource() == null || (o.getResource() != null && o.getResource().getObjectId() == null)))
              .map(o -> new ResourceOperationDTO(o.getOperation().getName(), null))
              .collect(Collectors.toList()));
    }

    return resourceOperations;
  }

  /**
   * Checks for resource match in the methods parameters
   *
   * @param joinPoint the annotations' joinPoint
   * @param resourceOperations the operations and resources of the current user
   */
  private boolean findResourceInParameters(JoinPoint joinPoint, List<ResourceOperationDTO> resourceOperations) {

    int i = 0;
    Parameter[] params  = ((MethodSignature) joinPoint.getSignature()).getMethod().getParameters();
    for (Parameter param : params) {
      if (WRAPPER_TYPE_LIST.contains(param.getType()) &&
              param.getAnnotation(ResourceId.class) != null && joinPoint.getArgs()[i] != null) {
        String paramValue = joinPoint.getArgs()[i].toString();
        for (ResourceOperationDTO roDTO : resourceOperations) {
          if (roDTO.getResourceId().equals(paramValue)) {
            return true;
          }
        }
      }
      i++;
    }
    return false;
  }


  /**
   * Checks for resource match in the attributes of the method's object
   *
   * @param parameter the object to look into
   * @param joinPoint the annotations' joinPoint
   * @param resourceOperations the operations and resources of the current user
   */
  @SuppressWarnings("squid:S3011")
  private boolean userHasOperationWithResourceID(Parameter parameter,
                                                 JoinPoint joinPoint, int index,
                                                 List<ResourceOperationDTO> resourceOperations)
      throws IllegalAccessException {
    Field[] fields = parameter.getType().getDeclaredFields();
    for (Field field : fields) {
      try {
        field.setAccessible(true);
      } catch (InaccessibleObjectException e) {
        continue;
      }
      ResourceId resourceIdAnnotation = field.getAnnotation(ResourceId.class);
      if (resourceIdAnnotation != null) {
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
   * This method throws the QLACK Unauthorized exception
   */
  private void throwUnauthorizedException() {
    throw new AccessDeniedException(
        "403 - Unauthorized Access. This user is not authorized for the specific operation.");

  }
}
