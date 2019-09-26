package com.eurodyn.qlack.fuse.rules.model;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class KnowledgeBaseLibraryTest {

  private KnowledgeBaseLibrary knowledgeBaseLibrary;

  @Before
  public void init() {
    knowledgeBaseLibrary = new KnowledgeBaseLibrary();
  }

  @Test
  public void libraryTest() {
    byte[] library = "library".getBytes();
    knowledgeBaseLibrary.setLibrary(library);
    assertEquals(library, knowledgeBaseLibrary.getLibrary());
  }

  @Test
  public void baseTest() {
    KnowledgeBase knowledgeBase = new KnowledgeBase();
    knowledgeBaseLibrary.setBase(knowledgeBase);
    assertEquals(knowledgeBase, knowledgeBaseLibrary.getBase());
  }

}
