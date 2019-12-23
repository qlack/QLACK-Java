# QLACK Fuse - AAA

This module provides custom Authentication, Authorization and Accounting operations. Authorization (access control) which is carried out by an AspectJ class that works as an interceptor. This class intercepts every call on an endpoint or a business method annotated by the @ResourceAccess annotation and authorizes the request by matching permissions described by @ResourceOperation annotation with user/group/resource permissions provided by the authenticated user principal. @ResourceAccess can be described by multiple @ResourceOperation annotations.

## Integration

### Add qlack-fuse-aaa dependency to your pom.xml:
```xml
    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-aaa</artifactId>
        <version>${qlack.version}</version>
    </dependency>
```

### Add qlack-fuse-aaa changelog in your application liquibase changelog:
```
<include file="db/changelog/qlack-fuse-aaa/qlack.fuse.aaa.changelog.xml"/>
```

### Add the packages in the Spring boot application main class declaration:
#### Inject a password encoder bean (qlack-fuse-security has already this step implemented).
```java
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//..

@SpringBootApplication
@EnableCaching
@EnableJpaRepositories("com.eurodyn.qlack.fuse.aaa.repository")
@EntityScan("com.eurodyn.qlack.fuse.aaa.model")
@ComponentScan(basePackages = {
        "com.eurodyn.qlack.fuse.aaa"
})
//..

@Bean
public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```

### Example
```java
import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
// ..

    @Autowired
    private UserService userService;
    
    private final String USERNAME = "username";
// ..

    public void createUser(){
        UserDTO existingUser = userService.getUserByName(USERNAME);
        
        if (existingUser == null){
            String userId = userService.createUser(createUserDTO);
            System.out.println("User with id " +userId+ " has been created.");
        } else {
            System.out.println("User " +USERNAME+ " already exists.");
        }
    }
    
    private List<UserAttributeDTO> createUserAttributesDTO(){
        List<UserAttributeDTO> userAttributesDTO = new ArrayList<>();

        UserAttributeDTO userAttributeDTO = new UserAttributeDTO();
        userAttributeDTO.setName("fullName");
        userAttributeDTO.setData("Ioannis Mousmoutis");
        userAttributeDTO.setContentType("text");
        userAttributesDTO.add(userAttributeDTO);

        UserAttributeDTO userAttributeDTO2 = new UserAttributeDTO();
        userAttributeDTO2.setName("company");
        userAttributeDTO2.setData("European Dynamics");
        userAttributeDTO2.setContentType("text");
        userAttributesDTO.add(userAttributeDTO2);

        return userAttributesDTO;
    }
    
    private UserDTO createUserDTO(){
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(USERNAME);
        userDTO.setPassword("thisisaverysecurepassword");
        userDTO.setStatus((byte)1);
        userDTO.setSuperadmin(true);
        userDTO.setExternal(false);
        userDTO.setUserAttributes(new HashSet<>(createUserAttributesDTO()));
            
        return userDTO;
    }
}
```

### Securing endpoints
To secure an endpoint use @ResourceAccess annotation. The @ResourceAccess describes access to resources at a role level (roleAccess property) or at an operation and/or unique resource level (operations property).

To describe access at a role level, the roleAccess property is used. The roleAcess is an array containing the names of the roles allowed to access this endpoint. For example, the following code gives access to users who have the role "Administrator". Access to more than one roles can be described separated by comma.

### Example 1
```java
import com.eurodyn.qlack.fuse.aaa.annotation.ResourceAccess;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
// ..

@Autowired
private UserService userService;
// ..

@RequestMapping(method = RequestMethod.GET, value="/app/users/admin")
@ResponseBody
@ResourceAccess(roleAccess = {"Administrator"})
public UserDTO getAdmin() {
    return userService.getUserByName("admin");
}
```

@ResourceOperation is used to describe access at an operation/unique resource level. @ResourceOperation holds a pair of operation - resourceId properties which declare the operation name and perhaps the resource (object), a user must have permissions for. If the user has such permissions, the request is authorized.

The @ResourceOperation has the following properties:

operation: the name of the allowed operation. This operation should already exist in the database

resourceIdParameter (Optional): the name of the parameter which represents the resourceId (GET, DELETE scenarios)

resourceIdField (Optional): the name of the DTO field which represents the resourceId (POST, PUT scenarios)

When the resourceIdField property is used, the referenced field in the DTO should always be annotated with the @ResourceId annotation and reference the field name as its value.

### Example 2
```java
import com.eurodyn.qlack.fuse.aaa.annotation.ResourceAccess;
import com.eurodyn.qlack.fuse.aaa.annotation.ResourceOperation;
import org.springframework.web.bind.annotation.RequestBody;
// ..

RequestMapping(method = RequestMethod.PUT, value="/app/docs")
@ResourceAccess(
    operations = {
            @ResourceOperation(operation = "UPDATE_PERMISSION", 
            resourceIdField = "id")
    }
 )
public void updateDocument(@RequestBody DocumentDTO document) {
    // ..
}
```
```java
import com.eurodyn.qlack.fuse.aaa.annotation.ResourceId;

public class DocumentDTO {

    @ResourceId("id")
    private String id;
    // ..
}
```
