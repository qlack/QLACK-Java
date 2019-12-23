package com.eurodyn.qlack.fuse.tokenserver.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.tokenserver.InitTestValues;
import com.eurodyn.qlack.fuse.tokenserver.dto.TokenDTO;
import com.eurodyn.qlack.fuse.tokenserver.exception.QTokenRevokedException;
import com.eurodyn.qlack.fuse.tokenserver.mapper.TokenMapper;
import com.eurodyn.qlack.fuse.tokenserver.model.QToken;
import com.eurodyn.qlack.fuse.tokenserver.model.Token;
import com.eurodyn.qlack.fuse.tokenserver.repository.TokenRepository;
import com.querydsl.core.types.Predicate;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

@RunWith(MockitoJUnitRunner.class)
public class TokenServerServiceTest {

  @InjectMocks
  private TokenServerService service;

  private TokenServerService mockService;

  @Mock
  private TokenMapper mapper;

  @Mock
  private TokenRepository repository;

  private InitTestValues initTestValues;

  private Token token;

  private TokenDTO tokenDTO;

  @Before
  public void init() {
    service = new TokenServerService(mapper, repository);
    mockService = spy(service);
    initTestValues = new InitTestValues();
    token = initTestValues.createToken();
    tokenDTO = initTestValues.createTokenDTO();
  }

  @Test
  public void cleanupExpiredSuccessfully() {
    service.cleanupExpired();
    verify(repository, times(1))
      .deleteAllByValidUntilIsBefore(any(Instant.class));
  }

  @Test
  public void cleanupRevokedSuccessfully() {
    service.cleanupRevoked();
    verify(repository, times(1)).deleteAllByRevokedIsTrue();
  }

  @Test
  public void findTokensSuccessfully() {
    Predicate predicate = QToken.token.revoked.eq(true);

    List<Token> tokens = new ArrayList<>();
    tokens.add(new Token());
    tokens.add(new Token());
    tokens.add(new Token());

    List<TokenDTO> expectedsTokenDTOs = new ArrayList<>();
    expectedsTokenDTOs.add(new TokenDTO());
    expectedsTokenDTOs.add(new TokenDTO());
    expectedsTokenDTOs.add(new TokenDTO());

    when(repository.findAll(predicate)).thenReturn(tokens);
    when(mapper.mapToDTO(tokens)).thenReturn(expectedsTokenDTOs);

    List<TokenDTO> actualTokens = service.findTokens(predicate);

    assertEquals(actualTokens, expectedsTokenDTOs);
    verify(repository, times(1)).findAll(predicate);
    verify(mapper, times(1)).mapToDTO(tokens);
  }

  @Test
  public void getTokenSuccessfully() {
    String tokenId = UUID.randomUUID().toString();

    token.setId(tokenId);

    when(repository.fetchById(tokenId)).thenReturn(token);
    when(mapper.mapToDTO(token)).thenReturn(tokenDTO);

    TokenDTO actualTokenDTO = service.findById(tokenId);

    assertEquals(tokenDTO, actualTokenDTO);
    verify(mapper, times(1)).mapToDTO(token);
    verify(repository, times(1)).fetchById(tokenId);
  }

  @Test
  public void createTokenSuccessfully() {
    String tokenId = UUID.randomUUID().toString();
    token.setId(tokenId);

    when(mapper.mapToEntity(tokenDTO)).thenReturn(token);
    when(repository.save(token)).thenReturn(token);

    String actualTokenId = service.createToken(tokenDTO);

    assertEquals(actualTokenId, tokenId);
    verify(mapper, times(1)).mapToEntity(tokenDTO);
    verify(repository, times(1)).save(any(Token.class));
  }

  @Test
  public void deleteTokenSuccessfully() {
    String tokenId = UUID.randomUUID().toString();
    service.deleteById(tokenId);
    verify(repository, times(1)).deleteById(tokenId);
  }

  @Test
  public void deleteTokensSuccessfully() {
    int tokenIdsSize = 10;
    List<String> tokenIds = Stream.generate(() -> UUID.randomUUID().toString())
      .limit(tokenIdsSize)
      .collect(Collectors.toList());

    service.deleteTokens(tokenIds);
    verify(repository, times(10)).deleteById(anyString());
  }

  @Test
  public void getValidUntilSuccessfully() {
    String tokenId = UUID.randomUUID().toString();
    Instant expectedValidUntil = token.getValidUntil()
      .plus(1, ChronoUnit.HOURS);
    token.setValidUntil(expectedValidUntil);
    token.setId(tokenId);

    when(repository.fetchById(tokenId)).thenReturn(token);

    Instant actualValidUntil = service.getValidUntil(tokenId);

    assertEquals(expectedValidUntil, actualValidUntil);
    verify(repository, times(1)).fetchById(tokenId);
  }

  @Test
  public void revokeTokenSuccessfully() {
    String tokenId = UUID.randomUUID().toString();
    token.setId(tokenId);

    when(repository.fetchById(tokenId)).thenReturn(token);
    service.revoke(tokenId);
    assertTrue(token.isRevoked());
  }

  @Test
  public void revokeTokensSuccessfully() {
    int tokenIdsSize = 10;
    List<String> tokenIds = Stream.generate(() -> UUID.randomUUID().toString())
      .limit(tokenIdsSize)
      .collect(Collectors.toList());

    doNothing().when(mockService).revoke(anyString());
    mockService.revoke(tokenIds);
    verify(mockService, times(tokenIdsSize)).revoke(anyString());

  }

  @Test(expected = QTokenRevokedException.class)
  public void revokeTokenThrowsExpectedException() {
    String tokenId = UUID.randomUUID().toString();

    Token token = new Token();
    token.setId(tokenId);
    token.setRevoked(true);

    when(repository.fetchById(tokenId)).thenReturn(token);
    service.revoke(tokenId);
  }

  @Test
  public void extendTokenValiditySuccessfully() {
    String tokenId = UUID.randomUUID().toString();
    Instant expectedValidUntil = token.getValidUntil()
      .plus(1, ChronoUnit.HOURS);

    token.setId(tokenId);

    when(repository.fetchById(tokenId)).thenReturn(token);
    service.extendValidity(tokenId, expectedValidUntil);
    assertEquals(expectedValidUntil, token.getValidUntil());
  }

  @Test(expected = QTokenRevokedException.class)
  public void extendTokenValidityThrowsExpectedException() {
    String tokenId = UUID.randomUUID().toString();
    Instant expectedValidUntil = token.getValidUntil()
      .plus(1, ChronoUnit.HOURS);
    token.setId(tokenId);
    token.setRevoked(true);

    when(repository.fetchById(tokenId)).thenReturn(token);
    service.extendValidity(tokenId, expectedValidUntil);
  }

  @Test
  public void extendTokenAutoExtendValiditySuccessfully() {
    String tokenId = UUID.randomUUID().toString();
    Instant autoExtendUntil = token.getValidUntil().plus(1, ChronoUnit.HOURS);
    token.setId(tokenId);

    when(repository.fetchById(tokenId)).thenReturn(token);
    service.extendAutoExtendValidity(tokenId, autoExtendUntil);
    assertEquals(autoExtendUntil, token.getAutoExtendUntil());
  }

  @Test(expected = QTokenRevokedException.class)
  public void extendTokenAutoExtendValidityThrowsExpectedException() {
    String tokenId = UUID.randomUUID().toString();
    Instant autoExtendUntil = token.getValidUntil().plus(1, ChronoUnit.HOURS);
    token.setId(tokenId);
    token.setRevoked(true);

    when(repository.fetchById(tokenId)).thenReturn(token);
    service.extendAutoExtendValidity(tokenId, autoExtendUntil);
  }

  @Test
  public void nonRevokedTokenAndNoExpiryIsValid() {
    String tokenId = UUID.randomUUID().toString();
    token.setId(tokenId);

    when(repository.fetchById(tokenId)).thenReturn(token);
    boolean actualResult = service.isValid(tokenId);
    assertTrue(actualResult);
  }

  @Test
  public void nonRevokedTokenAndNotExpiredIsValid() {
    String tokenId = UUID.randomUUID().toString();
    token.setId(tokenId);
    token.setValidUntil(token.getValidUntil().plus(1, ChronoUnit.HOURS));

    when(repository.fetchById(tokenId)).thenReturn(token);
    boolean actualResult = service.isValid(tokenId);
    assertTrue(actualResult);
  }

  @Test
  public void validTokenWithNonPositiveAutoExtendDuration() {
    String tokenId = UUID.randomUUID().toString();
    token.setId(tokenId);
    token.setValidUntil(token.getValidUntil().plus(1, ChronoUnit.HOURS));
    token.setAutoExtendDuration(0L);

    when(repository.fetchById(tokenId)).thenReturn(token);
    boolean actualResult = service.isValid(tokenId);
    assertTrue(actualResult);
  }

  @Test
  public void validTokenWithPositiveAutoExtendDuration() {
    String tokenId = UUID.randomUUID().toString();
    token.setId(tokenId);
    token.setValidUntil(token.getValidUntil().plus(1, ChronoUnit.HOURS));
    token.setAutoExtendDuration(1000L);
    Instant autoExtendUntil = Instant.now().plus(1, ChronoUnit.SECONDS);
    token.setAutoExtendUntil(autoExtendUntil);

    when(repository.fetchById(tokenId)).thenReturn(token);
    service.isValid(tokenId);
    assertEquals(autoExtendUntil, token.getValidUntil());
  }

  @Test
  public void nonRevokedTokenAndExpiredIsNotValid() {
    String tokenId = UUID.randomUUID().toString();
    token.setId(tokenId);
    token.setValidUntil(token.getValidUntil().minus(1, ChronoUnit.HOURS));

    when(repository.fetchById(tokenId)).thenReturn(token);
    boolean actualResult = service.isValid(tokenId);
    assertFalse(actualResult);
  }

  @Test
  @SuppressWarnings("squid:S2699")
  public void checkAndSendQueuedCleanupDisabledTest() {
    service.checkAndSendQueued();
  }

  @Test
  public void checkAndSendQueuedTest() {
    ReflectionTestUtils.setField(service, "enableCleanup", true);
    ReflectionTestUtils.setField(service, "cleanupExpired", true);
    ReflectionTestUtils.setField(service, "cleanupRevoked", true);
    service.checkAndSendQueued();
    verify(repository, times(1))
      .deleteAllByValidUntilIsBefore(any(Instant.class));
    verify(repository, times(1)).deleteAllByRevokedIsTrue();
  }

  @Test
  @SuppressWarnings("squid:S2699")
  public void checkAndSendQueuedDisabledTest() {
    ReflectionTestUtils.setField(service, "enableCleanup", true);
    ReflectionTestUtils.setField(service, "cleanupExpired", false);
    ReflectionTestUtils.setField(service, "cleanupRevoked", false);
    service.checkAndSendQueued();
  }

  @Test
  public void isValidScenarioTwoTest() {
    token.setValidUntil(null);
    when(repository.fetchById("id")).thenReturn(token);
    assertTrue(service.isValid("id"));
  }

  @Test
  public void isValidScenarioThreeTest() {
    token.setRevoked(true);
    when(repository.fetchById("id")).thenReturn(token);
    assertFalse(service.isValid("id"));
  }

  @Test
  public void isValidScenarioFourthTest() {
    token.setValidUntil(token.getValidUntil().plus(1, ChronoUnit.HOURS));
    token.setAutoExtendDuration(1L);
    token.setAutoExtendUntil(null);

    when(repository.fetchById("id")).thenReturn(token);
    assertTrue(service.isValid("id"));
  }

  @Test
  public void isValidScenarioFifthTest() {
    token.setValidUntil(token.getValidUntil().plus(1, ChronoUnit.HOURS));
    token.setAutoExtendDuration(1L);
    token.setAutoExtendUntil(Instant.now().plusMillis(10000L));

    when(repository.fetchById("id")).thenReturn(token);
    assertTrue(service.isValid("id"));
  }

}
