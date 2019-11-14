# QLACK Security module

The QLACK-based Spring Security implementation, which provides user authentication functionality based on the https://spring.io/projects/spring-security[Spring Security framework] and JWT (JSON Web Tokens) standard, https://tools.ietf.org/html/rfc7519[RFC-7519].

## Integration

### Add qlack-fuse-security dependency to your pom.xml:

```
    <properties>
        <!-- ... -->
        <version.qlack>3.0.0-SNAPSHOT</version.qlack>
    </properties>

    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-security</artifactId>
        <version>${version.qlack}</version>
    </dependency>
```

### Add the required properties at the application.properties file:

```properties
################################################################################
# qlack-fuse-security properties
################################################################################
# The string that will be used for signing the jwt
qlack.fuse.security.jwt.secret=qlackjwtsecret
# The jwt validity will be set to 24 hours
qlack.fuse.security.jwt.expiration=86400000
# Define if user roles will be added in the JWT claims
qlack.fuse.security.jwt.include.roles=true
# This paths will require authentication
qlack.fuse.security.authenticated.paths=/app/secured/**,/app/admin
# Clock difference between client and application for JWT validity checks
qlack.fuse.security.jwt.clocks.margin=120
```

### Add the packages in the Spring boot application main class declaration:

```java
@SpringBootApplication
@EnableJpaRepositories("com.eurodyn.qlack.fuse.aaa.repository")
@EntityScan("com.eurodyn.qlack.fuse.aaa.model")
@ComponentScan(basePackages = {
    "com.eurodyn.qlack.fuse.aaa",
    "com.eurodyn.qlack.fuse.security"
})
```

### Example
```java
import com.eurodyn.qlack.fuse.aaa.dto.UserDetailsDTO;
import com.eurodyn.qlack.fuse.security.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.HttpHeaders;
// ..

@Autowired
private AuthenticationService authenticationService;
// ..

@RequestMapping(method = RequestMethod.POST, value="/login")
@ResponseBody
public Response login(@RequestBody UserDetailsDTO user, HttpServletResponse response) {
    String generatedJwt = authenticationService.authenticate(user);
    response.setHeader(HttpHeaders.AUTHORIZATION, generatedJwt);

    // ..
}
```


