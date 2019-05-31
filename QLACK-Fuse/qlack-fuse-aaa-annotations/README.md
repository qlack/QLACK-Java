# QLACK AAA Annotations module

This module provides authorization (access control) which is carried out by an AspectJ class that works as an interceptor. This class intercepts every call on an endpoint or a business method annotated by the @ResourceAccess annotation and authorizes the request by matching permissions described by @ResourceOperation annotation with user/group/resource permissions provided by the authenticated user principal. @ResourceAccess can be described by multiple @ResourceOperation annotations.

## Integration

### Add qlack-fuse-aaa-annotations dependency to your pom.xml:

```
    <properties>
        <!-- ... -->
        <version.qlack>3.0.0-SNAPSHOT</version.qlack>
    </properties>

    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-aaa-annotations</artifactId>
        <version>${version.qlack}</version>
    </dependency>
```

### Usage
To secure an endpoint use @ResourceAccess annotation. The @ResourceAccess describes access to resources at a role level (roleAccess property) or at an operation and/or unique resource level (operations property).

To describe access at a role level, the roleAccess property is used. The roleAcess is an array containing the names of the roles allowed to access this endpoint. For example, the following code gives access to users who have the role "Administrator". Access to more than one roles can be described separated by comma.

### Example 1
```java
import com.eurodyn.qlack.fuse.aaa.annotations.annotation.ResourceAccess;
import com.eurodyn.qlack.fuse.aaa.service.UserService;
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
import com.eurodyn.qlack.fuse.aaa.annotations.annotation.ResourceAccess;
import com.eurodyn.qlack.fuse.aaa.annotations.annotation.ResourceOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
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
import com.eurodyn.qlack.fuse.aaa.annotations.annotation.ResourceId;

public class DocumentDTO {

    @ResourceId("id")
    private String id;
    // ..
}
```
