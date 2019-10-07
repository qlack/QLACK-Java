package com.eurodyn.qlack.fuse.mailing.validators;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class EmailValidatorTest {

  @InjectMocks private EmailValidator emailValidator;

  private EmailDTO emailDTO;
  private InitTestValues initTestValues;

  @Before
  public void init(){
    initTestValues = new InitTestValues();
    emailDTO = initTestValues.createEmailDTO();
  }

  @Test
  public void isValidTest(){
    assertTrue(emailValidator.isValid(emailDTO));
  }

  @Test
  public void isValidEmptyToEmailsTest(){
    emailDTO.setToEmails(null);
    assertTrue(emailValidator.isValid(emailDTO));
  }

  @Test
  public void isValidEmptyCcTest(){
    emailDTO.setToEmails(null);
    emailDTO.setCcEmails(null);
    assertTrue(emailValidator.isValid(emailDTO));
  }
}
