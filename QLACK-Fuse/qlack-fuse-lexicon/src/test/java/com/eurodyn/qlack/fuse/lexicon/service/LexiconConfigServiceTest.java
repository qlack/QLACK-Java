package com.eurodyn.qlack.fuse.lexicon.service;

import com.eurodyn.qlack.fuse.lexicon.dto.GroupDTO;
import com.eurodyn.qlack.fuse.lexicon.dto.KeyDTO;
import com.eurodyn.qlack.fuse.lexicon.dto.LanguageDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Application;
import com.eurodyn.qlack.fuse.lexicon.repository.ApplicationRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LexiconConfigServiceTest {

  @InjectMocks
  private LexiconConfigService lexiconConfigService;
  @Mock
  private GroupService groupService;
  @Mock
  private LanguageService languageService;
  @Mock
  private KeyService keyService;
  @Mock
  private ApplicationRepository applicationRepository;
  @Mock
  private LanguageDTO languageDTO;
  @Mock
  private GroupDTO groupDTO;
  @Mock
  private KeyDTO keyDTO;

  final private List<Application> applicationList = new ArrayList<>();
  private URL yamlUrl;
  final private Set<GroupDTO> groupDTOSet = new HashSet<>();

  @BeforeEach
  public void init() {
    lexiconConfigService = new LexiconConfigService(groupService, languageService, keyService, applicationRepository);
  }

  private URL createUrl() throws IOException {
    return createUrl("qlack-lexicon-config.yaml");
  }

  private URL createUrl(String url) throws IOException {
    Enumeration<URL> entries = this.getClass().getClassLoader()
        .getResources(url);
    return entries.nextElement();
  }

  @Test
  public void initTest() {
    when(languageService.getLanguageByLocale("en")).thenReturn(languageDTO);
    when(groupService.getGroupByTitle(any())).thenReturn(groupDTO);
    lexiconConfigService.init();
    verify(applicationRepository, times(1)).findBySymbolicName(any());
  }

  @Test
  public void initShouldDeleteTest() throws IOException {
    when(languageService.getLanguageByLocale("en")).thenReturn(languageDTO);
    when(groupService.getGroupByTitle(any())).thenReturn(groupDTO);
    lexiconConfigService
        .updateTranslations(createUrl("qlack-lexicon-config-keys.yaml"), "qlack-lexicon-config-keys.yaml");
    verify(applicationRepository, times(1)).findBySymbolicName(any());
  }

  @Test
  public void initForceUpdateTest() throws IOException {
    when(languageService.getLanguageByLocale("en")).thenReturn(languageDTO);
    when(groupService.getGroupByTitle(any())).thenReturn(groupDTO);
    lexiconConfigService
        .updateTranslations(createUrl("qlack-lexicon-config-keys.yaml"), "qlack-lexicon-config-keys.yaml");
    verify(applicationRepository, times(1)).findBySymbolicName(any());
  }

  @Test
  public void initUpdateKeysTest() throws IOException {
    groupDTOSet.add(groupDTO);
    when(languageService.getLanguageByLocale("en")).thenReturn(languageDTO);
    when(groupService.getGroupByTitle(any())).thenReturn(groupDTO);
    when(keyService.getKeyByName(any(), any(), eq(true))).thenReturn(keyDTO);
    when(groupService.getRemainingGroups(any())).thenReturn(groupDTOSet);
    lexiconConfigService
        .updateTranslations(createUrl("qlack-lexicon-config-keys.yaml"), "qlack-lexicon-config-keys.yaml");
    verify(applicationRepository, times(1)).findBySymbolicName(any());
  }

  @Test
  public void initUpdateKeysForceDeleteTest() throws IOException {
    groupDTOSet.add(groupDTO);
    when(languageService.getLanguageByLocale("en")).thenReturn(languageDTO);
    when(groupService.getGroupByTitle(any())).thenReturn(groupDTO);
    when(keyService.getKeyByName(any(), any(), eq(true))).thenReturn(keyDTO);
    lexiconConfigService
        .updateTranslations(createUrl("qlack-lexicon-config.yaml"), "qlack-lexicon-config-keys.yaml");

    when(keyService.getKeyByName(any(), any(), eq(true))).thenReturn(null);
    lexiconConfigService
        .updateTranslations(createUrl("qlack-lexicon-config2.yaml"), "qlack-lexicon-config2.yaml");
    verify(applicationRepository, times(2)).findBySymbolicName(any());
  }

  @Test
  public void initUpdateKeysLastBranchTest() throws IOException {
    KeyDTO keyDTO1 = new KeyDTO();
    groupDTOSet.add(groupDTO);
    Map<String, String> translationKeys = new HashMap<>();
    translationKeys.put("generic_key", "Welcome");
    translationKeys.put("en", "en");
    keyDTO1.setTranslations(translationKeys);
    when(languageService.getLanguageByLocale("en")).thenReturn(languageDTO);
    when(languageDTO.getId()).thenReturn("en");
    when(groupService.getGroupByTitle(any())).thenReturn(groupDTO);
    when(keyService.getKeyByName(any(), any(), eq(true))).thenReturn(keyDTO1);

    lexiconConfigService
        .updateTranslations(createUrl("qlack-lexicon-config.yaml"), "qlack-lexicon-config.yaml");

    when(keyService.getKeyByName(any(), any(), eq(true))).thenReturn(keyDTO);
    lexiconConfigService
        .updateTranslations(createUrl("qlack-lexicon-config.yaml"), "qlack-lexicon-config.yaml");

    verify(applicationRepository, times(2)).findBySymbolicName(any());
  }

  @Test
  public void initUpdateKeysNullTest() {
    groupDTOSet.add(groupDTO);
    languageDTO.setId(null);
    when(languageService.getLanguageByLocale("en")).thenReturn(languageDTO);
    when(groupService.getGroupByTitle(any())).thenReturn(groupDTO);
    when(keyService.getKeyByName(any(), any(), eq(true))).thenReturn(keyDTO);
    lexiconConfigService.init();
    verify(applicationRepository, times(1)).findBySymbolicName(any());
  }

  @Test
  public void initAppIdTest() throws IOException {
    yamlUrl = createUrl();
    Application application = new Application();
    application.setChecksum(DigestUtils.md5Hex(yamlUrl.openStream()));
    applicationList.add(application);

    when(applicationRepository.findBySymbolicName("qlack-lexicon-config.yaml"))
        .thenReturn(applicationList);
    lexiconConfigService.init();
    verify(applicationRepository, times(1)).findBySymbolicName(any());
  }

  @Test
  public void initAppCheckSumTest() throws IOException {
    Application application = new Application();
    application.setChecksum("randomChecksum");
    applicationList.add(application);

    when(languageService.getLanguageByLocale(any())).thenReturn(languageDTO);
    when(applicationRepository.findBySymbolicName("qlack-lexicon-config-more-data.yaml"))
        .thenReturn(applicationList);
    when(groupService.getRemainingGroups(any())).thenReturn(groupDTOSet);
    lexiconConfigService
        .updateTranslations(createUrl("qlack-lexicon-config-more-data.yaml"), "qlack-lexicon-config-more-data.yaml");
    verify(applicationRepository, times(1)).findBySymbolicName(any());
  }

  @Test
  public void initNoGroupTest() throws IOException {
    lexiconConfigService
        .updateTranslations(createUrl("qlack-lexicon-config-no-data.yaml"), "qlack-lexicon-config-no-data.yaml");
    verify(applicationRepository, times(1)).findBySymbolicName(any());
  }
  

}
