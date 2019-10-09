package com.eurodyn.qlack.fuse.mailing.validators;

import static org.junit.Assert.assertTrue;

import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class EmailValidatorTest {

  @InjectMocks
  private EmailValidator emailValidator;

  private EmailDTO emailDTO;
  private InitTestValues initTestValues;

  @Before
  public void init() {
    initTestValues = new InitTestValues();
    emailDTO = initTestValues.createEmailDTO();
  }

  @Test
  public void isValidTest() {
    assertTrue(emailValidator.isValid(emailDTO));
  }

  @Test
  public void isValidEmptyToEmailsTest() {
    emailDTO.setToEmails(null);
    assertTrue(emailValidator.isValid(emailDTO));
  }

  @Test
  public void isValidEmptyCcTest() {
    emailDTO.setToEmails(null);
    emailDTO.setCcEmails(null);
    assertTrue(emailValidator.isValid(emailDTO));
  }
}
