package com.eurodyn.qlack.fuse.aaa.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.model.VerificationToken;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import com.eurodyn.qlack.fuse.aaa.repository.VerificationTokenRepository;
import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * @author European Dynamics
 */

@RunWith(MockitoJUnitRunner.class)
public class VerificationServiceTest {

  @InjectMocks
  private VerificationService verificationService;

  private VerificationTokenRepository verificationTokenRepository = mock(
      VerificationTokenRepository.class);
  private UserRepository userRepository = mock(UserRepository.class);
  private VerificationToken verificationToken;
  private InitTestValues initTestValues;
  private User user;
  private String userID;

  @Before
  public void init() {
    verificationService = new VerificationService(verificationTokenRepository, userRepository);
    initTestValues = new InitTestValues();
    user = initTestValues.createUser();
    verificationToken = initTestValues.createVerificationToken();
    userID = user.getId();
  }

  @Test
  public void testCreateVerificationToken() {
    when(userRepository.findById(userID)).thenReturn(Optional.of(user));
    verificationService.createVerificationToken(userID, verificationToken.getExpiresOn());
    verify(verificationTokenRepository, times(1)).save(any());
  }


  @Test
  public void testCreateVerificationTokenWithData() {
    when(userRepository.findById(userID)).thenReturn(Optional.of(user));
    verificationService.createVerificationToken(userID, verificationToken.getExpiresOn(),
        verificationToken.getData());
    verify(verificationTokenRepository, times(1)).save(any());
  }

  @Test
  public void testVerifyToken() {
    when(verificationTokenRepository.findById(verificationToken.getId()))
        .thenReturn(Optional.of(verificationToken));
    String verifiedUserId = verificationService.verifyToken(verificationToken.getId());
    assertEquals(userID, verifiedUserId);
  }

  @Test
  public void testDeleteToken() {
    verificationService.deleteToken(verificationToken.getId());
    verify(verificationTokenRepository, times(1)).deleteById(verificationToken.getId());
  }

  @Test
  public void testDeleteExpired() {
    verificationService.deleteExpired();
    verify(verificationTokenRepository, times(1)).deleteByExpiresOnBefore(anyLong());
  }

  @Test
  public void testGetTokenPayload() {
    when(verificationTokenRepository.findById(verificationToken.getId()))
        .thenReturn(Optional.of(verificationToken));
    String foundPayload = verificationService.getTokenPayload(verificationToken.getId());
    assertEquals(verificationToken.getData(), foundPayload);
  }

  @Test
  public void testGetTokenPayloadNull() {
    when(verificationTokenRepository.findById(verificationToken.getId()))
        .thenReturn(Optional.empty());
    String foundPayload = verificationService.getTokenPayload(verificationToken.getId());
    assertNull(foundPayload);
  }

  @Test
  public void testGetTokenUser() {
    when(verificationTokenRepository.findById(verificationToken.getId()))
        .thenReturn(Optional.of(verificationToken));
    String foundUser = verificationService.getTokenUser(verificationToken.getId());
    assertEquals(verificationToken.getUser().getId(), foundUser);
  }

  @Test
  public void testGetTokenUserNull() {
    when(verificationTokenRepository.findById(verificationToken.getId()))
        .thenReturn(Optional.empty());
    String foundUser = verificationService.getTokenUser(verificationToken.getId());
    assertNull(foundUser);
  }
}
