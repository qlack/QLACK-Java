package com.eurodyn.qlack.fuse.lexicon.util;

import static org.junit.jupiter.api.Assertions.*;
import com.eurodyn.qlack.fuse.lexicon.criteria.KeySearchCriteria;
import com.eurodyn.qlack.fuse.lexicon.criteria.KeySearchCriteria.KeySearchCriteriaBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class KeySearchCriteriaBuilderTest {

  private KeySearchCriteriaBuilder keySearchCriteriaBuilder;

  @BeforeEach
  public void init() {
    keySearchCriteriaBuilder = KeySearchCriteria.KeySearchCriteriaBuilder
      .createCriteria();
  }

  @Test
  public void withNameLikeTest() {
    assertNotNull(keySearchCriteriaBuilder.withNameLike("name%"));
  }

  @Test
  public void inGroupTest() {
    assertNotNull(keySearchCriteriaBuilder.inGroup("groupIn"));
  }

  @Test
  public void setPageSizeWithPageNumTest() {
    assertNotNull(keySearchCriteriaBuilder.setPageSizeWithPageNum(10, 10));
  }

  @Test
  public void buildTest() {
    assertNotNull(keySearchCriteriaBuilder.build());
  }

}
