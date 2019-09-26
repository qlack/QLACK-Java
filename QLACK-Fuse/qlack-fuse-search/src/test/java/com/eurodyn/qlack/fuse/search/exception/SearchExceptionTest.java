package com.eurodyn.qlack.fuse.search.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SearchExceptionTest {

  @Test
  public void defaultConstructorTest() {
    SearchException searchException = new SearchException();
    assertEquals(SearchException.class, searchException.getClass());
  }

  @Test
  public void constructorWithMessageTest() {
    String message = "exception message";
    SearchException searchException = new SearchException(message);
    assertEquals(message, searchException.getMessage());
  }

  @Test
  public void constructorWithMessageAndCauseTest() {
    String message = "exception message";
    Throwable throwable = new Throwable();
    SearchException searchException = new SearchException(message, throwable);
    assertEquals(throwable, searchException.getCause());
    assertEquals(message, searchException.getMessage());
  }

}
