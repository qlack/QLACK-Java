# QLACK Util - QueryDSL

This module provides annotation-based checks for empty QueryDSL predicates. When using QueryDSL for 
your REST endpoints this utility component makes sure that empty predicates do not produce an error.

## Integration

### Add qlack-util-querydsl dependency to your pom.xml:
```xml
    <dependency>
        <groupId>com.eurodyn.qlack.util</groupId>
        <artifactId>qlack-util-querydsl</artifactId>
        <version>${qlack.version}</version>
    </dependency>
```

### Use the @EmptyPredicateCheck annotation for SQL queries
For example:
```java
@EmptyPredicateCheck
public ResponseEntity<Page<UserGroupDTO>> findAll(@QuerydslPredicate(root = UserGroup.class) Predicate predicate,
Pageable pageable) {
return userGroupManagementService.findAll(predicate, pageable);
}
```
