# QLACK Fuse - Rules

This module uses the Drools library and provides rules configuration and execution methods.

## Integration

### Add qlack-fuse-rules dependency to your pom.xml:
```xml
    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-rules</artifactId>
        <version>${qlack.version}</version>
    </dependency>
```

### Add qlack-fuse-rules changelog in your application liquibase changelog:
```
<include file="db/changelog/qlack-fuse-rules/qlack.fuse.rules.changelog.xml"/>
```

### Add the required properties at the application.properties file:
```properties
# Rules configuration
# The accepted pattern classes to be de-serialized by rules, separated by comma.
# Skip for whitelisting all classes.
qlack.fuse.rules.accepted.classes = com.eurodyn.qlack.example.rules.model,com.eurodyn.qlack.example.rules.dto
```

### Add the packages in the Spring boot application main class declaration:
```java
@SpringBootApplication
@EnableJpaRepositories("com.eurodyn.qlack.fuse.rules.repository")
@EntityScan("com.eurodyn.qlack.fuse.rules.model")
@ComponentScan(basePackages = {
    "com.eurodyn.qlack.fuse.rules"
})
```

### Add the required xml configuration for the default kSession and kBases under resources/META-INF/kmodule.xml
```xml
<kmodule xmlns="http://www.drools.org/xsd/kmodule">
    <kbase name="activate-rules" packages="org.drools.rules.activate" default="true">
        <ksession name="ksession-activate-rules" default="true"/>
    </kbase>
    <kbase name="deactivate-rules" packages="org.drools.rules.deactivate">
        <ksession name="ksession-deactivate-rules"/>
    </kbase>
    <kbase name="stateless-rules" packages="org.drools.rules">
        <ksession name="ksession-stateless" type="stateless" default="true"/>
    </kbase>
</kmodule>
```

### Add the rules for the defined kBases in .drl files in the abode defined packages
### Example under resources/ord/drools/rules/activate
```
import com.eurodyn.qlack.test.cmd.model.Account
import com.eurodyn.qlack.test.cmd.model.AccountStatus

rule "accountIsActive"
  when
    $account : Account((status == AccountStatus.INACTIVE) && (balance > 0))
  then
    $account.setStatus(AccountStatus.ACTIVE);
    System.out.println("The account " +$account.getId()+ " has been activated.");
end
```
