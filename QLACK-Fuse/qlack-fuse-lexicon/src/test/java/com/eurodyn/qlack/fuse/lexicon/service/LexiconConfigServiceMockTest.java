package com.eurodyn.qlack.fuse.lexicon.service;

import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.lexicon.exception.LexiconYMLProcessingException;
import com.eurodyn.qlack.fuse.lexicon.repository.ApplicationRepository;
import java.io.IOException;
import java.net.URL;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class LexiconConfigServiceMockTest {

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
  private ApplicationContext applicationContext;

  @Mock
  private ClassLoader classLoader;

  @Mock
  private URL yamlFile;

  @Before
  public void init() {
    lexiconConfigService = new LexiconConfigService(groupService,
        languageService, keyService,
        applicationRepository, applicationContext);
    ReflectionTestUtils
        .setField(lexiconConfigService, "classLoader", classLoader);
  }

  @Test
  public void initNullEntriesTest() throws IOException {
    when(classLoader.getResources("qlack-lexicon-config.yaml"))
        .thenReturn(null);
    lexiconConfigService.init();
  }

  @Test
  public void initIoExceptionTest() throws IOException {
    when(classLoader.getResources("qlack-lexicon-config.yaml"))
        .thenThrow(new IOException());
    lexiconConfigService.init();
  }

  @Test(expected = LexiconYMLProcessingException.class)
  public void initNullYamlFilesTest() throws IOException {
    when(yamlFile.openStream()).thenThrow(new IOException());
    lexiconConfigService.updateTranslations(yamlFile);
  }

}
