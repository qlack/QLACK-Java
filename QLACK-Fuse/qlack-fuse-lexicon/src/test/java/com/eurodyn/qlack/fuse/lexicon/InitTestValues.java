package com.eurodyn.qlack.fuse.lexicon;

import com.eurodyn.qlack.fuse.lexicon.dto.GroupDTO;
import com.eurodyn.qlack.fuse.lexicon.dto.KeyDTO;
import com.eurodyn.qlack.fuse.lexicon.dto.LanguageDTO;
import com.eurodyn.qlack.fuse.lexicon.dto.TemplateDTO;
import com.eurodyn.qlack.fuse.lexicon.exception.LanguageProcessingException;
import com.eurodyn.qlack.fuse.lexicon.model.Data;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
import com.eurodyn.qlack.fuse.lexicon.model.Key;
import com.eurodyn.qlack.fuse.lexicon.model.Language;
import com.eurodyn.qlack.fuse.lexicon.model.Template;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author European Dynamics
 */
public class InitTestValues {

  public String templateContent = "<h1>Template example</h1><p>${data.value}</p>";
  public String nestedTemplateContent = "<html>"
      + "<body>"
      + "<title>Nested Template</title>"
      + "<div>${template.content}</div>"
      + "</body>"
      + "</html>";
  private Language englishLang;
  private Language portugueseLang;
  private Key attachment_desc;
  private Key change_password;

  public Language createEnglishLanguage() {
    englishLang = new Language();
    englishLang.setId("71df58f1-be26-410a-94ca-cfc90ac955a4");
    englishLang.setLocale("en");
    englishLang.setName("English");
    return englishLang;
  }

  public LanguageDTO createEnglishLanguageDTO() {
    LanguageDTO englishLangDTO = new LanguageDTO();
    englishLangDTO.setId("71df58f1-be26-410a-94ca-cfc90ac955a4");
    englishLangDTO.setLocale("en");
    englishLangDTO.setName("English");
    return englishLangDTO;
  }

  public Language createPortugueseLanguage() {
    portugueseLang = new Language();
    portugueseLang.setId("12df58f1-be26-410a-94ca-cfc90ac955a4");
    portugueseLang.setName("Portuguese");
    portugueseLang.setLocale("pt");
    portugueseLang.setActive(true);
    return portugueseLang;
  }

  public LanguageDTO createPortugueseLanguageDTO() {
    LanguageDTO portugueseLangDTO = new LanguageDTO();
    portugueseLangDTO.setId("12df58f1-be26-410a-94ca-cfc90ac955a4");
    portugueseLangDTO.setName("Portuguese");
    portugueseLangDTO.setLocale("pt");
    portugueseLangDTO.setActive(true);
    return portugueseLangDTO;
  }

  public List<Language> createLanguages() {
    List<Language> languages = new ArrayList<>();
    languages.add(createEnglishLanguage());
    languages.add(createPortugueseLanguage());
    return languages;
  }

  public List<LanguageDTO> createLanguagesDTO() {
    List<LanguageDTO> languagesDTO = new ArrayList<>();
    languagesDTO.add(createEnglishLanguageDTO());
    languagesDTO.add(createPortugueseLanguageDTO());
    return languagesDTO;
  }

  public GroupDTO createGroupDTO() {
    GroupDTO groupDTO = new GroupDTO();
    groupDTO.setId("06b68e66-d4fa-4070-ae49-826be499eb41");
    groupDTO.setTitle("Application UI");
    groupDTO.setDescription("description");
    return groupDTO;
  }

  public Group createGroup() {
    Group group = new Group();
    group.setId("06b68e66-d4fa-4070-ae49-826be499eb41");
    group.setTitle("Application UI");
    group.setDescription("description");
    return group;
  }

  public List<Group> createGroups() {
    List<Group> groups = new ArrayList<>();
    groups.add(createGroup());

    Group group2 = createGroup();
    group2.setId("07a68e66-d4fa-4070-ae49-826be4111eb52");
    group2.setTitle("Application Reports");
    groups.add(group2);

    return groups;
  }

  public List<GroupDTO> createGroupsDTO() {
    List<GroupDTO> groupsDTO = new ArrayList<>();
    groupsDTO.add(createGroupDTO());

    GroupDTO groupDTO2 = createGroupDTO();
    groupDTO2.setId("07a68e66-d4fa-4070-ae49-826be4111eb52");
    groupDTO2.setTitle("Application Reports");
    groupsDTO.add(groupDTO2);

    GroupDTO groupDTO3 = createGroupDTO();
    groupDTO3.setId("12a68e66-d4fa-4070-ae49-826be4111eb10");
    groupDTO3.setTitle("Application Appendix");
    groupsDTO.add(groupDTO3);

    return groupsDTO;
  }


  public Key createKey() {
    Key key = new Key();
    key.setId("0f2f12f8-4902-4355-ae52-20ccf92db2f3");
    key.setGroup(createGroup());
    key.setName("attachment_desc");
    key.setData(createDataList());
    return key;
  }

  public KeyDTO createKeyDTO() {
    KeyDTO keyDTO = new KeyDTO();
    keyDTO.setId("0f2f12f8-4902-4355-ae52-20ccf92db2f3");
    keyDTO.setGroupId(createGroup().getId());
    keyDTO.setName("attachment_desc");

    Map<String, String> translations = new HashMap<>();
    translations.put("777119b0-bda0-4e87-9d3b-08d80e9bb9e8", "Add attachment description");
    translations.put("71df58f1-be26-410a-94ca-cfc90ac955a4", "Adicionar descrição do anexo");
    keyDTO.setTranslations(translations);

    return keyDTO;
  }

  public List<KeyDTO> createKeysDTO() {
    List<KeyDTO> keysDTO = new ArrayList<>();
    keysDTO.add(createKeyDTO());

    KeyDTO key2 = createKeyDTO();
    key2.setId("ec5efa0b-5f6e-4059-9ae4-d262180a61d5");
    key2.setName("change_password");
    Map<String, String> translations = new HashMap<>();
    translations.put("777119b0-bda0-4e87-9d3b-08d80e9bb9e8", "Change password");
    translations.put("71df58f1-be26-410a-94ca-cfc90ac955a4", "Mudar senha");

    keysDTO.add(key2);
    return keysDTO;
  }

  public List<Key> createKeys() {
    List<Key> keys = new ArrayList<>();
    keys.add(createKey());

    Key key2 = createKey();
    key2.setId("ec5efa0b-5f6e-4059-9ae4-d262180a61d5");
    key2.setName("change_password");

    keys.add(key2);
    return keys;
  }

  public Data createData() {
    Data data = new Data();
    data.setId("93efd573-e3e8-49e7-9740-8a341d0e5b84");
    data.setLanguage(createEnglishLanguage());
    data.setValue("Add attachment description");
    data.setLastUpdatedOn(1550527200000L);
    return data;
  }

  public List<Data> createDataList() {
    List<Data> dataList = new ArrayList<>();
    dataList.add(createData());

    Data data2 = createData();
    data2.setLanguage(createPortugueseLanguage());
    data2.setId("2d4a5be3-de1e-40d1-b5b8-eb386bd5d8c8");
    data2.setLastUpdatedOn(1550649743302L);
    data2.setValue("Adicionar descrição do anexo");
    dataList.add(data2);

    return dataList;
  }

  public List<Data> createEnglishDataList() {
    List<Data> englishDataList = new ArrayList<>();
    englishDataList.add(createData());
    return englishDataList;
  }

  public Template createTemplate() {
    Template template = new Template();
    template.setId("1a390032-821e-4266-a6af-8df40075228e");
    template.setName("Test template");
    template.setLanguage(createEnglishLanguage());
    template.setContent(templateContent);
    return template;
  }

  public TemplateDTO createTemplateDTO() {
    TemplateDTO templateDTO = new TemplateDTO();
    templateDTO.setId("1a390032-821e-4266-a6af-8df40075228e");
    templateDTO.setName("Test template");
    templateDTO.setLanguageId(createEnglishLanguage().getId());
    templateDTO.setContent(templateContent);
    return templateDTO;
  }

  public List<Template> createTemplates() {
    List<Template> templates = new ArrayList<>();
    templates.add(createTemplate());

    Template template2 = createTemplate();
    template2.setId("60dffc2f-f54f-4645-8e0d-678f7b859e29");
    template2.setName("Test template");
    template2.setLanguage(createPortugueseLanguage());
    template2.setContent(templateContent);
    templates.add(template2);

    return templates;
  }

  public List<TemplateDTO> createTemplatesDTO() {
    List<TemplateDTO> templatesDTO = new ArrayList<>();
    templatesDTO.add(createTemplateDTO());

    TemplateDTO templateDTO2 = createTemplateDTO();
    templateDTO2.setId("60dffc2f-f54f-4645-8e0d-678f7b859e29");
    templateDTO2.setName("Test template");
    templateDTO2.setLanguageId(createPortugueseLanguage().getId());
    templateDTO2.setContent(templateContent);
    templatesDTO.add(templateDTO2);

    return templatesDTO;
  }

  public Map<String, String> createTranslations() {
    Map<String, String> translations = new HashMap<>();
    translations.put(createKey().getName(), createData().getValue());
    translations.put("change_password", "Change Password");
    return translations;
  }

  public byte[] getLanguageByteArray() {
    Path resourceDirectory = Paths.get("src/test/resources/eng_translations.xls");

    ClassLoader classLoader = getClass().getClassLoader();
    try {
      return Files.readAllBytes(resourceDirectory);
    } catch (IOException e) {
      throw new LanguageProcessingException(
          "Error loading Excel file for language " + createEnglishLanguage().getId());
    }
  }
}
