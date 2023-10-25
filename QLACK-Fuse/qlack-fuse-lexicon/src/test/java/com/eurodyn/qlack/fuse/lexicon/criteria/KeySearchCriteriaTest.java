package com.eurodyn.qlack.fuse.lexicon.criteria;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class KeySearchCriteriaTest {

  private KeySearchCriteria keySearchCriteria;

  @BeforeEach
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
    assertTrue(keySearchCriteria.isAscending());
  }

  @Test
  public void pageableTest() {
    Pageable pageable = PageRequest.of(10, 10);
    keySearchCriteria.setPageable(pageable);
    assertEquals(pageable, keySearchCriteria.getPageable());
  }

}
