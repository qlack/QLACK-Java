package com.eurodyn.qlack.fuse.lexicon.service;

import com.eurodyn.qlack.fuse.lexicon.dto.GroupDTO;
import com.eurodyn.qlack.fuse.lexicon.dto.KeyDTO;
import com.eurodyn.qlack.fuse.lexicon.dto.LanguageDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Application;
import com.eurodyn.qlack.fuse.lexicon.repository.ApplicationRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LexiconConfigServiceTest {

  @InjectMocks private LexiconConfigService lexiconConfigService;
  @Mock private GroupService groupService;
  @Mock private LanguageService languageService;
  @Mock private KeyService keyService;
  @Mock private ApplicationRepository applicationRepository;
  @Mock private ApplicationContext applicationContext;
  @Mock private LanguageDTO languageDTO;
  @Mock private GroupDTO groupDTO;
  @Mock private KeyDTO keyDTO;

  private List<Application> applicationList = new ArrayList<>();
  private URL yamlUrl;

  @Before
  public void init() throws IOException {
    lexiconConfigService = new LexiconConfigService(groupService, languageService, keyService,
        applicationRepository, applicationContext);
    Enumeration<URL> entries = this.getClass().getClassLoader().getResources("qlack-lexicon-config.yaml");
    yamlUrl = entries.nextElement();
  }

  @Test
  public void initTest() {
    when(languageService.getLanguageByLocale("en")).thenReturn(languageDTO);
    when(groupService.getGroupByTitle(any())).thenReturn(groupDTO);
    lexiconConfigService.init();
    verify(applicationRepository, times(1)).findBySymbolicName(any());
  }

  @Test
  public void initUpdateKeysTest() {
    when(languageService.getLanguageByLocale("en")).thenReturn(languageDTO);
    when(groupService.getGroupByTitle(any())).thenReturn(groupDTO);
    when(keyService.getKeyByName(any(), any(), eq(true))).thenReturn(keyDTO);
    lexiconConfigService.init();
    verify(applicationRepository, times(1)).findBySymbolicName(any());
  }

  @Test
  public void initTestAppId() throws IOException {
    Application application = new Application();
    application.setChecksum(DigestUtils.md5Hex(yamlUrl.openStream()));
    applicationList.add(application);

    when(applicationContext.getId()).thenReturn("appId");
    when(applicationRepository.findBySymbolicName("appId")).thenReturn(applicationList);
    lexiconConfigService.init();
    verify(applicationRepository, times(1)).findBySymbolicName(any());
  }
}
