# QLACK Settings module

This module is responsible for storing and configuring internal settings of the application.

## Integration

### Add qlack-fuse-settings dependency to your pom.xml:
```
    <properties>
        <!-- ... -->
        <version.qlack>3.0.0-SNAPSHOT</version.qlack>
    </properties>

    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-settings</artifactId>
        <version>${version.qlack}</version>
    </dependency>
```

### Add qlack-fuse-settings changelog in your application liquibase changelog
```
<include file="db/changelog/qlack.fuse.settings.changelog.xml"/>
```

### Add the packages in the Spring boot application main class declaration:
```java
@SpringBootApplication
@EnableJpaRepositories("com.eurodyn.qlack.fuse.settings.repository")
@EntityScan("com.eurodyn.qlack.fuse.settings.model")
@ComponentScan(basePackages = {
    "com.eurodyn.qlack.fuse.settings"
})
```

### Example

```java

import com.eurodyn.qlack.common.exception.QAlreadyExistsException;
import com.eurodyn.qlack.fuse.settings.dto.SettingDTO;
import com.eurodyn.qlack.fuse.settings.service.SettingsService;
import org.springframework.beans.factory.annotation.Autowired;
// ..

    @Autowired
    private SettingsService settingsService;
// ..

    public void createSetting(){
        try {
            settingsService.createSetting(createSettingDTO());
            System.out.println("A new setting has been added.");
        } catch (QAlreadyExistsException e){
            System.out.println(e.getMessage());
        }
    }

    private SettingDTO createSettingDTO(){
        SettingDTO settingDTO = new SettingDTO();
        settingDTO.setKey("Setting Key 1");
        settingDTO.setGroup("Test Group");
        settingDTO.setOwner("Test Owner");
        settingDTO.setPassword(true);
        settingDTO.setCreatedOn(1625145120000L);
        return settingDTO;
    }

}
```
