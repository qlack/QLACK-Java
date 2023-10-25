package com.eurodyn.qlack.fuse.lexicon.service;

import java.net.URL;

import com.eurodyn.qlack.fuse.lexicon.exception.LexiconYMLProcessingException;
import com.eurodyn.qlack.fuse.lexicon.repository.ApplicationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import java.io.IOException;

@ExtendWith(MockitoExtension.class)
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
  private ClassLoader classLoader;

  @Mock
  private URL yamlFile;

  @BeforeEach
  public void init() {
    lexiconConfigService = new LexiconConfigService(groupService,
      languageService, keyService,
      applicationRepository);
    ReflectionTestUtils
      .setField(lexiconConfigService, "classLoader", classLoader);
  }

  @Test
  @SuppressWarnings("squid:S2699")
  public void initNullEntriesTest() throws IOException {
    when(classLoader.getResources("qlack-lexicon-config.yaml"))
      .thenReturn(null);
    lexiconConfigService.init();
  }

  @Test
  @SuppressWarnings("squid:S2699")
  public void initIoExceptionTest() throws IOException {
    when(classLoader.getResources("qlack-lexicon-config.yaml"))
      .thenThrow(new IOException());
    lexiconConfigService.init();
  }

  @Test
  public void initNullYamlFilesTest(){
    assertThrows(LexiconYMLProcessingException.class, () -> {
      when(yamlFile.openStream()).thenThrow(new IOException());
      lexiconConfigService.updateTranslations(yamlFile, "qlack-lexicon-config.yaml");
    });
  }

}
