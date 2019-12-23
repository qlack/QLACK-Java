# QLACK Fuse - Token Server

## Integration

### Add qlack-fuse-token-server dependency to your pom.xml:

```xml
    <dependency>
        <artifactId>qlack-fuse-token-server</artifactId>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <version>${qlack.version}</version>
    </dependency>
```

### Add the required properties at the application.properties/yml file:

```properties
################################################################################
# qlack-fuse-token-server properties
################################################################################
# Enables cleanup scheduled task
qlack.fuse.tokenserver.enableCleanup=true
# Enables cleanup of expired tokens
qlack.fuse.tokenserver.cleanupExpired=true
# Enables cleanup of revoked tokens
qlack.fuse.tokenserver.cleanupRevoked=true
```
### Add qlack-fuse-token-server changelog in your application liquibase changelog
```
<include file="db/changelog/qlack-fuse-token-server/qlack.fuse.token-server.changelog.xml"/>
```

### Add the packages in your spring configuration class declaration:

```java
//..
@EnableJpaRepositories("com.eurodyn.qlack.fuse.tokenserver.repository")
@EntityScan("com.eurodyn.qlack.fuse.tokenserver.model")
@ComponentScan("com.eurodyn.qlack.fuse.tokenserver")
//..
```

### Example
```java
//..
  @Autowired
  private TokenServerService service;
//..
  service.createToken(tokenDTO);
//..
  service.findById(id);
//..
  service.deleteById(id);
//..
  service.extendValidity(tokenId, validUntil);
//..
  service.revoke(tokenId);
//..
  service.revoke(tokenIds);
//..
  service.deleteTokens(tokenIds);
//..
  service.isValid(tokenId);
//..
  service.getValidUntil(tokenId);
//..
  service.extendAutoExtendValidity(tokenId, autoExtendUntil);
//..
  service.checkAndSendQueued();
//..
```


