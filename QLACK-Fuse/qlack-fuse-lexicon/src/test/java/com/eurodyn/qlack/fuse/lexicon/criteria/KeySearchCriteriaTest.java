package com.eurodyn.qlack.fuse.lexicon.criteria;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@RunWith(MockitoJUnitRunner.class)
public class KeySearchCriteriaTest {

  private KeySearchCriteria keySearchCriteria;

  @Before
  public void init() {
    keySearchCriteria = new KeySearchCriteria();
  }

  @Test
  public void groupIdTest() {
    String groupId = "groupId";
    keySearchCriteria.setGroupId(groupId);
    assertEquals(groupId, keySearchCriteria.getGroupId());
  }

  @Test
  public void keyNameTest() {
    String keyName = "keyName";
    keySearchCriteria.setKeyName(keyName);
    assertEquals(keyName, keySearchCriteria.getKeyName());
  }

  @Test
  public void ascendingTest() {
    keySearchCriteria.setAscending(true);
    assertEquals(true, keySearchCriteria.isAscending());
  }

  @Test
  public void pageableTest() {
    Pageable pageable = PageRequest.of(10, 10);
    keySearchCriteria.setPageable(pageable);
    assertEquals(pageable, keySearchCriteria.getPageable());
  }

}
