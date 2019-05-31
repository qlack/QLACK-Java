# QLACK Lexicon module

This module provides language translations for the system.

## Integration

### Add qlack-fuse-lexicon dependency to your pom.xml:
```
    <properties>
        <!-- ... -->
        <version.qlack>3.0.0-SNAPSHOT</version.qlack>
    </properties>

    <dependency>
        <groupId>com.eurodyn.qlack.fuse</groupId>
        <artifactId>qlack-fuse-lexicon</artifactId>
        <version>${version.qlack}</version>
    </dependency>
```

### Add qlack-fuse-lexicon changelog in your application liquibase changelog:
```
<include file="db/changelog/qlack.fuse.lexicon.changelog.xml"/>
```

### Add the packages in the Spring boot application main class declaration:
```java
@SpringBootApplication
@EnableCaching
@EnableJpaRepositories("com.eurodyn.qlack.fuse.lexicon.repository")
@EntityScan("com.eurodyn.qlack.fuse.lexicon.model")
@ComponentScan(basePackages = {"com.eurodyn.qlack.fuse.lexicon"})
```

### Example 1
```java
// ..

import org.springframework.beans.factory.annotation.Autowired;
import com.eurodyn.qlack.fuse.lexicon.service.GroupService;
import com.eurodyn.qlack.fuse.lexicon.service.KeyService;
import com.eurodyn.qlack.fuse.lexicon.service.LanguageService;

@Autowired
private LanguageService languageService;

@Autowired
private KeyService keyService;

@Autowired
private GroupService groupService;
// ..


public void createLanguage(){
    String languageId = languageService.createLanguage(createLanguageDTO());
    System.out.println("Language with id " +languageId+ " has been created.");
    }

public void updateTranslation {
    String UPDATED_TRANSLATION = "Add attachment description here";
    keyService.updateTranslation(
               keyService.getKeyByName("attach_desc",groupService.getGroupByName("ui").getId(), false).getId(),
               languageService.getLanguageByLocale("en").getId(), UPDATED_TRANSLATION);
    }

public void createKey() {
    System.out.println("******************");
    System.out.println("Testing createKey method.");
    String key = keyService.createKey(createKeyDTO(), false);
    System.out.println("Created key with id:" + key);
    System.out.println("******************");
    }

private LanguageDTO createLanguageDTO(){
    LanguageDTO languageDTO = new LanguageDTO();
    languageDTO.setLocale("en_us");
    languageDTO.setName("English_US");
    languageDTO.setActive(true);
    return languageDTO;
    }

private KeyDTO createKeyDTO() {
    KeyDTO keyDTO = new KeyDTO();
    keyDTO.setName("contact_us");

    Map<String, String> translations = new HashMap<>();
    translations.put(languageService.getLanguageByLocale("en").getId(), "Please contact our team");
    translations.put(languageService.getLanguageByLocale("fr").getId(), "S'il vous plaît contacter notre équipe");
    keyDTO.setTranslations(null);
    return keyDTO;
}
```

### Example 2

Automated addition/update of translations is enabled by adding file "qlack-lexicon-config.yaml", in <project_path>/resources.
Each time your application is being deployed, the module checks for changes in the .yaml file. If changes are found, the file is being processed in the following ways.
Newly created groups, languages and keys will be added to the system while existing ones will be ignored.

Useful tags:
```yaml
   - not_in_group: [group1, group2...], keys under this tag will be updated in all groups, except the ones in the [].  
   - group: , group tag without a value will result in adding keys without a group  
   - forceUpdate: true , this tag will force update to existing groups/keys/languages.  
   - forceDelete: true, this tag is applied only to keys and is used for deleting a key and all of its translations.  
```

Yaml example:

```yaml
groups:
  - name: ui
    description: UI translations
  - name: reports
    description: Translations of report title and descriptions
  - name: errors
    description: Translations of errors
languages:
  - name: English
    locale: en
  - name: Français
    locale: fr
  - name: Portuguese
    locale: pt
translations:
################################################################################
# ENGLISH translations
################################################################################
  - not_in_group: [errors]
    locale: en
    keys:
      - desc: Short description
  - group:
    locale: en
    keys:
      - generic_key : A key without a group
  - group: ui
    locale: en
    keys:
      - welc: Welcome
      - user_details: User Details Section
      - add_attachment_description: Add attachment description
        forceDelete: true
  - group: errors
    locale: en
    keys:
      - warn: Warning
      - unexpected_error: An unexpected error has occurred.
      - login_failure: Login failed due to invalid credentials, please try again
        forceUpdate: true
        #forceUpdate flag is used to change a value of existing key
################################################################################
# FRENCH translations
################################################################################
  - group: ui
    locale: fr
    keys:
      - add_attachment_description: Description de la pièce jointe
  - group: reports
    locale: fr
    keys:
      - reports_dir: Répertoire des rapports
  - group: errors
    locale: fr
    keys:
      - warn: Attention
      - unexpected_error: Une erreur imprévue s'est produite
      - login_failure: La connexion a échoué en raison d'informations d'identification non valides
  ################################################################################
  # PORTUGUESE translations
  ################################################################################
  - group: ui
    locale: pt
    keys:
      - add_attachment_description: adicionar descrição do anexo
  - group: errors
    locale: pt
    keys:
      - warn: Aviso
      - unexpected_error: Ocorreu um erro inesperado
      - login_failure: Falha no login devido a credenciais inválidas
```
