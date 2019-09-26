package com.eurodyn.qlack.fuse.rules.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class KnowledgeBaseLibraryDTOTest {

  private KnowledgeBaseLibraryDTO knowledgeBaseLibraryDTO;

  @Before
  public void init() {
    knowledgeBaseLibraryDTO = new KnowledgeBaseLibraryDTO();
  }

  @Test
  public void libraryTest() {
    byte[] library = "library".getBytes();
    knowledgeBaseLibraryDTO.setLibrary(library);
    assertEquals(library, knowledgeBaseLibraryDTO.getLibrary());
  }

}
