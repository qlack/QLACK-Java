# QLACK AAA module

This module provides custom Authentication, Authorization and Accounting operations.

## Integration

### Add qlack-fuse-aaa dependency to your pom.xml:
```
    <properties>
        <!-- ... -->
        <version.qlack>3.0.0-SNAPSHOT</version.qlack>
    </properties>

    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-aaa</artifactId>
        <version>${version.qlack}</version>
    </dependency>
```

### Add qlack-fuse-aaa changelog in your application liquibase changelog:
```
<include file="db/changelog/qlack.fuse.aaa.changelog.xml"/>
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