package com.eurodyn.qlack.fuse.lexicon.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.eurodyn.qlack.fuse.lexicon.InitTestValues;
import com.eurodyn.qlack.fuse.lexicon.criteria.KeySearchCriteria.SortType;
import com.eurodyn.qlack.fuse.lexicon.service.KeyService.TranslationKV;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TranslationKVTest {

  private TranslationKV translationKVOne;

  private TranslationKV translationKVTwo;

  private TranslationKV translationKVAsc;

  private TranslationKV translationKVDesc;

  private TranslationKV translationKVAllFieldsAsc;

  private TranslationKV translationKVAllFieldsDesc;

  @Before
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
    assertFalse(translationKVOne.equals(null));
    assertFalse((translationKVOne.equals(new InitTestValues())));
    assertFalse((translationKVOne.equals(translationKVDesc)));
    assertFalse((translationKVDesc.equals(translationKVOne)));
    assertTrue((translationKVDesc.equals(translationKVAsc)));
    assertTrue((translationKVOne.equals(translationKVOne)));
  }

  @Test
  public void hashCodeTest() {
    assertNotNull(translationKVOne.hashCode());
  }


}
