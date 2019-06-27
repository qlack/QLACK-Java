# QLACK Audit module

This module provides standard operations for system audit actions.

## Integration

### Add qlack-fuse-audit dependency to your pom.xml:
```
    <properties>
        <!-- ... -->
        <version.qlack>3.0.0-SNAPSHOT</version.qlack>
    </properties>

    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-audit</artifactId>
        <version>${version.qlack}</version>
    </dependency>
```

### Add qlack-fuse-audit changelog in your application liquibase changelog:
```
<include file="db/changelog/qlack-fuse-audit/qlack.fuse.audit.changelog.xml"/>
```

### If you wish to store audit trace data, add the following property in application.properties
```properties
qlack.fuse.audit.traceData=true
```

### Add the packages in the Spring boot application main class declaration:
```java
@SpringBootApplication
@EnableCaching
@EnableJpaRepositories("com.eurodyn.qlack.fuse.audit.repository")
@EntityScan("com.eurodyn.qlack.fuse.audit.model")
@ComponentScan(basePackages = {
        "com.eurodyn.qlack.fuse.audit"
})
```

### Example
```java
import com.eurodyn.qlack.fuse.audit.dto.AuditDTO;
import com.eurodyn.qlack.fuse.audit.dto.AuditTraceDTO;
import com.eurodyn.qlack.fuse.audit.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
// ..

    @Autowired
    private AuditService auditService;
// ..

    public void audit(){
        String auditId = auditService.audit(createAuditDTO);
        System.out.println("Audit with id " +auditId+ " has been created.");
    }
    
    private AuditTraceDTO createAuditTraceDTO() {
        AuditTraceDTO auditTraceDTO = new AuditTraceDTO();
        auditTraceDTO.setTraceData("{\n" +
                "\tcolor: \"red\",\n" +
                "\tvalue: \"#f00\"\n" +
                "}");
        return auditTraceDTO;
    }

    private AuditDTO createAuditDTO(){
        AuditDTO auditDTO = new AuditDTO();
        auditDTO.setLevel("Back End");
        auditDTO.setEvent("System Check");
        auditDTO.setShortDescription("Daily user check of the system.");
        auditDTO.setGroupName("BackEnd Audit group");
        auditDTO.setPrinSessionId("537925a3-80ae-447d-837c-e092a2e8f38e");
        auditDTO.setOpt1("new users: 3");
        auditDTO.setOpt2("deleted users: 0");
        auditDTO.setOpt3("expired users: 1");
        auditDTO.setReferenceId("1111");
        auditDTO.setCreatedOn(1625145120000L);
        auditDTO.setTrace(createAuditTraceDTO());

        return auditDTO;
    }
}
```