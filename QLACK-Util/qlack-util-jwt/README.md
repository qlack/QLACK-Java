# QLACK Util - JWT

This module provides methods for generating and manipulating JSON Web Tokens (JWT). It additionally provides a Spring Boot request filter, so that you can validate incoming HTTP requests bearing a JWT.

## Integration

### Add qlack-util-querydsl dependency to your pom.xml:
```xml
    <dependency>
        <groupId>com.eurodyn.qlack.util</groupId>
        <artifactId>qlack-util-jwt</artifactId>
        <version>${qlack.version}</version>
    </dependency>
```

### Spring Boot request filter
If you generate JWTs to be used with your application, QLACK Util - JWT provides a Spring Boot request filter that automatically extracts and validates a JWT. To enable the filter you can incorporate the following configuration in your Spring's WebSecurityConfigurerAdapter:

```
private final JwtAuthenticationFilter jwtAuthenticationFilter;
...
.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
```

The filter will automatically be invoked in matched incoming requests and try to validate the JWT. The JWT is expected in an HTTP header named `Authorization` prefixed with the word `Bearer` followed by a space and the content of your JWT in Base64 encoding.

Upon successful JWT validation, the filter will also set Spring's SecurityContextHolder, so that you can have the security details of the user represented by the JWT available for your remaining Spring-based code. 