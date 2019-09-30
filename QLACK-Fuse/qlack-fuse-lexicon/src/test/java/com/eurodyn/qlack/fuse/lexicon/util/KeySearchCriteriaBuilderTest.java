package com.eurodyn.qlack.fuse.lexicon.util;

import com.eurodyn.qlack.fuse.lexicon.criteria.KeySearchCriteria;
import com.eurodyn.qlack.fuse.lexicon.criteria.KeySearchCriteria.KeySearchCriteriaBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class KeySearchCriteriaBuilderTest {

  private KeySearchCriteriaBuilder keySearchCriteriaBuilder;

  @Before
  public void init() {
    keySearchCriteriaBuilder = KeySearchCriteria.KeySearchCriteriaBuilder
        .createCriteria();
  }

  @Test
  public void withNameLikeTest() {
    keySearchCriteriaBuilder.withNameLike("name%");
  }

  @Test
  public void inGroupTest() {
    keySearchCriteriaBuilder.inGroup("groupIn");
  }

  @Test
  public void setPageSizeWithPageNumTest() {
    keySearchCriteriaBuilder.setPageSizeWithPageNum(10, 10);
  }

  @Test
  public void buildTest() {
    keySearchCriteriaBuilder.build();
  }

}
