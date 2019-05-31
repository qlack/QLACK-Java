package com.eurodyn.qlack.fuse.aaa.annotations.aop;

import com.eurodyn.qlack.fuse.aaa.annotations.annotation.ResourceAccess;
import com.eurodyn.qlack.fuse.aaa.annotations.annotation.ResourceId;
import com.eurodyn.qlack.fuse.aaa.annotations.annotation.ResourceOperation;
import com.eurodyn.qlack.fuse.aaa.annotations.dto.ResourceOperationDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserGroupDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserGroupHasOperationDTO;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.eurodyn.qlack.fuse.aaa.dto.UserHasOperationDTO;
import lombok.extern.java.Log;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * Authorizes requests by validating users' role, operation and resources permissions
 * against the provided access rules. The access rules are described using {@link ResourceAccess},
 * {@link ResourceOperation} and {@link ResourceId} on the REST endpoint the request is issued for
 * or on the corresponding DTO fields that represent a resourceId field
 * @author European Dynamics
 */
@Aspect
@Component
@Log
public class ResourceAccessInterceptor {

    @Autowired
    private UserDetailsService userDetailsService;

    @Pointcut("execution(@com.eurodyn.qlack.fuse.aaa.annotations.annotation.ResourceAccess * *(..))")
    public void annotation() {
    }

    /**
     * Retrieves User & Group operations from user principal
     * @param user a {@link UserDetailsDTO} object that holds user information
     * @param operation the operation name to check permissions for
     * @return the operations and resources of the current user
     */
    private List<ResourceOperationDTO> getResourceOperations(UserDetailsDTO user, String operation) {
        List<ResourceOperationDTO> resourceOperations = new ArrayList<>();

        List<UserHasOperationDTO> uho = user.getUserHasOperations();
        List<UserGroupHasOperationDTO> gho = user.getUserGroupHasOperations();

        for (UserHasOperationDTO userHasOperationDTO : uho) {
            if (userHasOperationDTO.getOperation().getName().equals(operation)) {
                ResourceOperationDTO roDTO = new ResourceOperationDTO();
                roDTO.setOperation(operation);
                roDTO.setResourceId(userHasOperationDTO.getResource() != null && userHasOperationDTO.getResource().getObjectId() != null
                        ? userHasOperationDTO.getResource().getObjectId() : "");
                resourceOperations.add(roDTO);
            }
        }

        for (UserGroupHasOperationDTO userGroupHasOperationDTO : gho) {
            if (userGroupHasOperationDTO.getOperationDTO().getName().equals(operation)) {
                ResourceOperationDTO roDTO = new ResourceOperationDTO();
                roDTO.setOperation(operation);
                roDTO.setResourceId(userGroupHasOperationDTO.getResourceDTO() != null && userGroupHasOperationDTO.getResourceDTO().getObjectId() != null
                        ? userGroupHasOperationDTO.getResourceDTO().getObjectId() : "");
                resourceOperations.add(roDTO);
            }
        }

        return resourceOperations;
    }

    /**
     *
     * @param user a {@link UserDetailsDTO} object that holds user information
     * @param operation the operation name to check permissions for
     * @param resourceId the value of either resourceIdField or resourceIdParameter
     * @param joinPoint the annotations' joinPoint
     * @param searchInParams flag used for resource level access authorization
     * @return
     * @throws IllegalAccessException
     */
    private boolean userHasOperation(UserDetailsDTO user, String operation, String resourceId, JoinPoint joinPoint, boolean searchInParams)
            throws IllegalAccessException {

        // Get the user and group operations on resources
        List<ResourceOperationDTO> resourceOperations = getResourceOperations(user, operation);

        // If no operations for current user, the user is not authorized
        if (resourceOperations.size() == 0) {
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
                    for (ResourceOperationDTO roDTO : resourceOperations) {
                        if (roDTO.getResourceId().equals(String.valueOf(annotationResourceIdValue))) {
                            return true;
                        }
                    }
                }
            } else {
                Field[] fields = parameter.getType().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    ResourceId resourceIdAnnotation = field.getAnnotation(ResourceId.class);
                    if (resourceIdAnnotation != null) {
                        String annotationResourceId = resourceIdAnnotation.value();
                        if (!annotationResourceId.equals(resourceId)) {
                            continue;
                        }
                        Object annotationResourceIdValue = field.get(joinPoint.getArgs()[index]);
                        for (ResourceOperationDTO roDTO : resourceOperations) {
                            if (roDTO.getResourceId().equals(String.valueOf(annotationResourceIdValue))) {
                                return true;
                            }
                        }
                    }
                }
            }
            index++;
        }
        return false;
    }

    /**
     * Authorizes user on a role, operation and resources level
     * @param joinPoint the annotations' joinPoint
     * @param resourceAccess The {@link ResourceAccess} object
     * @throws AccessDeniedException if authorization fails
     * @throws IllegalAccessException if a class field cannot be accessed through Java Reflection API
     */
    @Before("annotation() && @annotation(resourceAccess)")
    public void authorize(JoinPoint joinPoint, ResourceAccess resourceAccess) throws AccessDeniedException, IllegalAccessException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //authorizeUserDetailsDTO method checks the fields of the com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO
        //if your security implementation adds another type of object in the spring security principal, a custom implementation must be added
        if (principal instanceof UserDetailsDTO){
            authorizeUserDetailsDTO((UserDetailsDTO) principal, joinPoint, resourceAccess);
        } else{
            log.severe("Contact the Qlack team.");
            throw new AccessDeniedException("403 - Unauthorized Access. This user is not authorized for the specific operation.");
        }
    }

    private void authorizeUserDetailsDTO(UserDetailsDTO user, JoinPoint joinPoint, ResourceAccess resourceAccess) throws AccessDeniedException, IllegalAccessException{

        boolean allowAccess;

        // superadmin users are authorized
        if (user.isSuperadmin()) {
            allowAccess = true;
        } else {
            List<UserGroupDTO> groups = user.getUserGroups();
            String[] roleAccess = resourceAccess.roleAccess();

            boolean authorizesOnRole = Arrays.stream(roleAccess).anyMatch(
                    new HashSet<>(groups.stream().map(g -> g.getName()).collect(Collectors.toList()))::contains);

            // If not authorized on role level, check specific operation permissions
            if (authorizesOnRole) {
                allowAccess = true;
            } else {
                allowAccess = false;
                log.info("The groups this user is assigned to are not authorized to access this resource.");

                ResourceOperation[] resourceOperations = resourceAccess.operations();
                for (ResourceOperation resourceOperation : resourceOperations) {
                    String resourceId = (!resourceOperation.resourceIdField().isEmpty() ? resourceOperation.resourceIdField()
                            : resourceOperation.resourceIdParameter());
                    boolean searchInParams = !resourceOperation.resourceIdParameter().isEmpty();
                    allowAccess =
                            allowAccess || userHasOperation(user, resourceOperation.operation(), resourceId, joinPoint, searchInParams);
                }
            }
        }

        if (!allowAccess) {
            throw new AccessDeniedException("403 - Unauthorized Access. This user is not authorized for the specific operation.");
        }
    }
}
