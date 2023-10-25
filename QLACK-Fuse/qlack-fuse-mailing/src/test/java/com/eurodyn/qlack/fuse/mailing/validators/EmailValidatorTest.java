package com.eurodyn.qlack.fuse.mailing.validators;

import static org.junit.jupiter.api.Assertions.*;
import com.eurodyn.qlack.fuse.mailing.InitTestValues;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class EmailValidatorTest {

  @InjectMocks
  private EmailValidator emailValidator;

  private EmailDTO emailDTO;
  private InitTestValues initTestValues;

  @BeforeEach
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
