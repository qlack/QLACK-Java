# QLACK Fuse - AAA

This module provides custom Authentication, Authorization and Accounting operations. Authorization
(access control) which is carried out by an AspectJ class that works as an interceptor. This class
intercepts every call on an endpoint or a business method annotated by the @ResourceAccess or 
@OperationAccess annotation and authorizes the request by matching permissions with
user/group/resource permissions provided by the authenticated user principal.

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
#### Inject a password encoder bean.
```java
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//..

@EnableCaching
@SpringBootApplication
@EnableJpaRepositories("com.eurodyn.qlack.fuse.aaa.repository")
@EntityScan("com.eurodyn.qlack.fuse.aaa.model")
@ComponentScan(basePackages = {"com.eurodyn.qlack.fuse.aaa"})
//..

@Bean
public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
}
```


### Securing endpoints
To secure an endpoint use @OperationAccess or @ResourceAccess annotation.
The @OperationAccess describes access to resources at a role level (roleAccess property) 
or at an operation (operations property), that does not require further check for resource.
The @ResourceAccess describes access to resources at an operation and unique resource level
(operations property).

@OperationAccess
To describe access at a role level, the roleAccess property is used. The roleAcess is an array
containing the names of the roles allowed to access this endpoint. For example, the following code
gives access to users who have the role "Administrator". Access to more than one roles can be
described separated by comma.

### Example 1
```java
import com.eurodyn.qlack.fuse.aaa.annotation.OperationAccess;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
// ..

@Autowired
private UserService userService;
// ..

@ResponseBody
@RequestMapping(method = RequestMethod.GET, value="/app/users/admin")
@OperationAccess(roleAccess = {"Administrator"})
public UserDTO getAdmin() {
    return userService.getUserByName("admin");
}

```

To describe access at an operation level, withour link on resource, the operations property is used. 
The operations is an array containing the names of the operations allowed to access this endpoint.
For example, the following code gives access to users who have the operation "VIEW_USERS". 
Access to more than one operations can be described separated by comma.

### Example 2
```java
import com.eurodyn.qlack.fuse.aaa.annotation.OperationAccess;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
// ..

@Autowired
private UserService userService;
// ..

@ResponseBody
@RequestMapping(method = RequestMethod.GET, value="/app/users/admin")
@OperationAccess(operations = {"VIEW_USERS"})
public List<UserDTO> getUsers() {
    return userService.findAll();
}

```

@ResourceAccess is used to describe access at an operation with unique resource level.
The operations property is filled as in the previous example. In addition, a property must be annotated
with @ResourceId and contain the corresponding's objectId value, in order for the user to be authenticated.
The property can either be part of the method's parameters, or a field inside the method's object.

### Example 3
```java
import com.eurodyn.qlack.fuse.aaa.annotation.OperationAccess;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
// ..

@Autowired
private UserService userService;
// ..
@GetMapping(path = "user", produces = MediaType.APPLICATION_JSON_VALUE)
@ResourceAccess(operations = { "VIEW_USER" })
public UserDTO findUser(@RequestParam(name = "id", required = false) @ResourceId String id) {
        return userService.findById(id);
        }

```

### Example 4
```java
import com.eurodyn.qlack.fuse.aaa.annotation.ResourceAccess;
import org.springframework.web.bind.annotation.RequestBody;
// ..

RequestMapping(method = RequestMethod.PUT, value="/app/docs")
@ResourceAccess(operations = { "UPDATE_PERMISSION" })
public void updateDocument(@RequestBody DocumentDTO document) {
    // ..
}
```
```java
import com.eurodyn.qlack.fuse.aaa.annotation.ResourceId;

public class DocumentDTO {

    @ResourceId
    private String id;
    // ..
}
```

## LDAP Integration
QLACK Fuse AAA can integrate with an LDAP server to perform authentication for your users.
LDAP integration can be seamlessly enabled by specifying the following properties:

```
// Specifies if LDAP integrated is active or not.
qlack.fuse.aaa_ldap_enabled

// The URL of the LDAP server to use.
qlack.fuse.aaa.ldap_url

// The baseDN to be used when communicating with the LDAP. This is the basis under which queries
// and binding takes place.
qlack.fuse.aaa.ldap_basedn

// The LDAP attribute denoting the username of a user. This is the value that will be placed in
// AAA's User model as 'username'.
qlack.fuse.aaa_ldap_attr_username

// The LDAP attribute denoting the group Id of a user. All groups identified in the LDAP will be
// linked to AAA groups (if they exist).
qlack.fuse.aaa.ldap_attr_group

// The attribute to use when binding to the LDAP. This is to facilitate scenarios where the
// username you use in AAA does not match the bindDN of the LDAP (which may be
// using a different one). If this property is empty, ldapAttrUsername is used instead.
qlack.fuse.aaa.ldap_bind_with

// A comma-separated list of attributes to include when creating a new user from LDAP. All other
// attributes will be excluded if this value is set.
qlack.fuse.aaa.ldap_include_attr
```
 
AAA's LDAP integration will automatically create a AAA user for you when authentication takes place,
so that you can have a local user reference to be able to specify authorisation details. It
additionally provides a method to sync locally-created users with their LDAP representation, so that
you can keep your locally-created users in sync.

To test LDAP integration in your application, you can use a sample Docker LDAP
container ([https://github.com/rroemhild/docker-test-openldap](https://github.com/rroemhild/docker-test-openldap)),
configuring AAA as follows:

```
qlack.fuse.aaa.ldap_enabled=true
qlack.fuse.aaa.ldap_url=ldap://localhost
qlack.fuse.aaa.ldap_basedn=ou=people,dc=planetexpress,dc=com
qlack.fuse.aaa.ldap_bind_with=cn
qlack.fuse.aaa.ldap_attr_username=mail
qlack.fuse.aaa.ldap_attr_group=ou
qlack.fuse.aaa.ldap_include_attr=sn,displayName,description,cn,givenName,title
```

### Custom attributes handling
If you want your application to handle attribute names and values in a custom way, you can provide
an implementation of `LDAPAttributeHandler`:

```
@Component
public class LDAPHandler implements LDAPAttributeHandler {

  @Override
  public String handleAttributeName(String attributeName, Object attributeValue) {
    ...
  }

  @Override
  public Object handleAttributeValue(String attributeName, Object attributeValue) {
    ...
  }
}

```
