# QLACK Fuse - Rules

This module provides a unified and simple api for configuring and executing rules.
It comes in two favors, the first one uses the Drools library while the second uses the Camunda DMN engine. 

## Integration

### Add either of the below dependencies to your pom.xml:
```xml
    <dependency>
        <groupId>com.eurodyn.qlack.fuse.rules</groupId>
        <artifactId>qlack-fuse-rules-camunda</artifactId>
        <version>${project.parent.version}</version>
    </dependency>
```
or
```xml
    <dependency>
        <groupId>com.eurodyn.qlack.fuse.rules</groupId>
        <artifactId>qlack-fuse-rules-drools</artifactId>
        <version>${project.parent.version}</version>
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
