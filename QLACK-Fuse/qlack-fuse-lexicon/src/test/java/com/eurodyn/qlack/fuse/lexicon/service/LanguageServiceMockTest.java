package com.eurodyn.qlack.fuse.lexicon.service;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doThrow;

import com.eurodyn.qlack.fuse.lexicon.InitTestValues;
import com.eurodyn.qlack.fuse.lexicon.exception.LanguageProcessingException;
import com.eurodyn.qlack.fuse.lexicon.mapper.LanguageMapper;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
import com.eurodyn.qlack.fuse.lexicon.model.Language;
import com.eurodyn.qlack.fuse.lexicon.repository.KeyRepository;
import com.eurodyn.qlack.fuse.lexicon.repository.LanguageRepository;
import com.eurodyn.qlack.fuse.lexicon.util.WorkbookUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@RunWith(PowerMockRunner.class)
//@PrepareForTest({StringUtils.class, WorkbookUtil.class})
public class LanguageServiceMockTest {
/*
  @InjectMocks
  private LanguageService languageService;

  @Mock
  private KeyRepository keyRepository;

  @Mock
  private LanguageRepository languageRepository;

  @Mock
  private KeyService keyService;

  @Mock
  private GroupService groupService;

  @Mock
  private LanguageMapper languageMapper;

  private InitTestValues initTestValues;

  private Language language;

  private byte[] lgXl;

  @Before
  public void init() {
    languageService = new LanguageService(keyService, groupService,
      languageRepository, keyRepository, languageMapper);
    initTestValues = new InitTestValues();
    language = initTestValues.createEnglishLanguage();
    lgXl = initTestValues.getLanguageByteArray();
  }

  @Test
  public void uploadLanguageNullGroupsTest() throws IOException {
    PowerMockito.mockStatic(StringUtils.class);
    PowerMockito.when(StringUtils.isNotBlank(anyString())).thenAnswer((Boolean) -> false);
    List<Group> groups = initTestValues.createGroups();
    List<String> groupsIds = new ArrayList<>();
    for (Group group : groups) {
      when(groupService.findByTitle(group.getTitle())).thenReturn(group);
      groupsIds.add(group.getId());
    }
    languageService.uploadLanguage(language.getId(), lgXl);

    Workbook wb = WorkbookFactory
      .create(new BufferedInputStream(new ByteArrayInputStream(lgXl)));
    for (int si = 0; si < wb.getNumberOfSheets(); si++) {
      Map<String, String> translations = new HashMap<>();
      Sheet sheet = wb.getSheetAt(si);
      String groupName = sheet.getSheetName();
      if (StringUtils.isNotBlank(groupName)) {
        verify(groupService, times(1)).findByTitle(groupName);
      }

      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
        String keyName = sheet.getRow(i).getCell(0).getStringCellValue();
        String keyValue = sheet.getRow(i).getCell(1).getStringCellValue();
        translations.put(keyName, keyValue);
      }
      verify(keyService, times(1))
        .updateTranslationsForLanguageByKeyName(language.getId(), null,
          translations);
    }
  }

  @Test(expected = LanguageProcessingException.class)
  public void downloadLanguageIoExceptionTest() throws Exception {
    PowerMockito.mockStatic(WorkbookUtil.class);
    Workbook wb = PowerMockito.mock(HSSFWorkbook.class);
    PowerMockito.when(WorkbookUtil.createHssfWorkbook()).thenAnswer((HSSFWorkbook) -> wb);
    doThrow(new IOException()).when(wb).write(any(OutputStream.class));
    when(languageRepository.fetchById(language.getId())).thenReturn(language);
    languageService.downloadLanguage(language.getId());
  }

  @Test
  public void downloadLanguageCloseWorkbookIoExceptionTest() throws Exception {
    PowerMockito.mockStatic(WorkbookUtil.class);
    Workbook wb = PowerMockito.mock(HSSFWorkbook.class);
    PowerMockito.when(WorkbookUtil.createHssfWorkbook()).thenAnswer((HSSFWorkbook) -> wb);
    doThrow(new IOException()).when(wb).close();
    when(languageRepository.fetchById(language.getId())).thenReturn(language);
    assertNotNull(languageService.downloadLanguage(language.getId()));
  }
*/
}
