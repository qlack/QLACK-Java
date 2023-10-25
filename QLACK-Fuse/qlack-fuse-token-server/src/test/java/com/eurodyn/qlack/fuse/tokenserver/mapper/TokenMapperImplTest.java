package com.eurodyn.qlack.fuse.tokenserver.mapper;

import static org.junit.jupiter.api.Assertions.*;
import com.eurodyn.qlack.fuse.tokenserver.InitTestValues;
import com.eurodyn.qlack.fuse.tokenserver.dto.TokenDTO;
import com.eurodyn.qlack.fuse.tokenserver.model.Token;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TokenMapperImplTest {

  @InjectMocks
  private TokenMapperImpl tokenMapper;

  private InitTestValues initTestValues;

  private Token token;

  private List<Token> tokens;

  private TokenDTO tokenDTO;

  private List<TokenDTO> tokenDTOS;

  @BeforeEach
  public void init() {
    initTestValues = new InitTestValues();
    token = initTestValues.createToken();
    tokenDTO = initTestValues.createTokenDTO();
    tokens = new ArrayList<Token>();
    tokenDTOS = new ArrayList<TokenDTO>();
  }

  @Test
  public void mapToDTOTest() {
    Token token = initTestValues.createToken();
    TokenDTO tokenDTO = tokenMapper.mapToDTO(token);
    assertEquals(token.getCreatedBy(), tokenDTO.getCreatedBy());

  }

  @Test
  public void mapToDTONullTest() {
    tokenDTO = tokenMapper.mapToDTO((Token) null);
    assertNull(tokenDTO);
  }

  @Test
  public void mapToDTOListNullTest() {
    List<TokenDTO> tokenDTOS = tokenMapper.mapToDTO((List<Token>) null);
    assertNull(tokenDTOS);
  }

  @Test
  public void mapToDTOListTest() {
    List<Token> tokens = new ArrayList<>();
    tokens.add(initTestValues.createToken());
    List<TokenDTO> tokenDTOS = tokenMapper.mapToDTO(tokens);

    assertEquals(tokens.size(), tokenDTOS.size());
  }

  @Test
  public void mapToEntityTest() {
    tokenDTO = initTestValues.createTokenDTO();
    token = tokenMapper.mapToEntity(tokenDTO);
    assertEquals(token.getCreatedBy(), tokenDTO.getCreatedBy());

  }

  @Test
  public void mapToEntityNullTest() {
    assertNull(tokenMapper.mapToEntity(null));

    token = tokenMapper.mapToEntity(null);
    assertNull(token);
  }


}
