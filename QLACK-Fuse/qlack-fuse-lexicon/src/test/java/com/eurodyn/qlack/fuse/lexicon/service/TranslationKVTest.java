package com.eurodyn.qlack.fuse.lexicon.service;

import static org.junit.jupiter.api.Assertions.*;
import com.eurodyn.qlack.fuse.lexicon.InitTestValues;
import com.eurodyn.qlack.fuse.lexicon.criteria.KeySearchCriteria.SortType;
import com.eurodyn.qlack.fuse.lexicon.service.KeyService.TranslationKV;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TranslationKVTest {

  private TranslationKV translationKVOne;

  private TranslationKV translationKVTwo;

  private TranslationKV translationKVAsc;

  private TranslationKV translationKVDesc;

  private TranslationKV translationKVAllFieldsAsc;

  private TranslationKV translationKVAllFieldsDesc;

  @BeforeEach
  public void test() {
    translationKVOne = new TranslationKV("key1", "value1");
    translationKVTwo = new TranslationKV("key1", "value2");
    translationKVAsc = new TranslationKV(SortType.ASCENDING);
    translationKVDesc = new TranslationKV(SortType.DESCENDING);
    translationKVAllFieldsAsc = new TranslationKV("key1", "value3",
      SortType.ASCENDING);
    translationKVAllFieldsDesc = new TranslationKV("key1", "value4",
      SortType.DESCENDING);
  }

  @Test
  public void compareTest() {
    assertEquals(-1,
      translationKVAsc.compare(translationKVOne, translationKVTwo));
    assertEquals(1,
      translationKVDesc.compare(translationKVOne, translationKVTwo));
  }

  @Test
  public void compareToTest() {
    assertEquals(2, translationKVAllFieldsAsc.compareTo(translationKVOne));
    assertEquals(-3, translationKVAllFieldsDesc.compareTo(translationKVOne));
  }

  @Test
  public void equalsTest() {
    assertNotNull(translationKVOne);
    assertNotEquals(new InitTestValues(), translationKVOne);
    assertNotEquals(translationKVDesc, translationKVOne);
    assertNotEquals(translationKVOne, translationKVDesc);
    assertEquals(translationKVAsc, translationKVDesc);
  }

  @Test
  public void hashCodeTest() {
    assertTrue(translationKVOne.hashCode() > 0);
  }


}
