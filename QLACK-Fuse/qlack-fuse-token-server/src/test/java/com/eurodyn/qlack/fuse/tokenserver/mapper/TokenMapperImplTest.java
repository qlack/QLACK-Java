package com.eurodyn.qlack.fuse.tokenserver.mapper;

import com.eurodyn.qlack.fuse.tokenserver.InitTestValues;
import com.eurodyn.qlack.fuse.tokenserver.dto.TokenDTO;
import com.eurodyn.qlack.fuse.tokenserver.mapper.TokenMapperImpl;
import com.eurodyn.qlack.fuse.tokenserver.model.Token;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class TokenMapperImplTest {

    @InjectMocks
    private TokenMapperImpl tokenMapper;

    private InitTestValues initTestValues;

    private Token  token;

    private List<Token> tokens;

    private TokenDTO tokenDTO;

    private List<TokenDTO> tokenDTOS;

    @Before
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
        assertEquals(null, tokenDTO);
    }

    @Test
    public void mapToDTOListNullTest() {
        List<TokenDTO> tokenDTOS = tokenMapper.mapToDTO((List<Token>) null);
        assertEquals(null, tokenDTOS);
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
        assertEquals(null, tokenMapper.mapToEntity((TokenDTO) null));

        token = tokenMapper.mapToEntity((TokenDTO) null);
        assertEquals(null, token);
    }


}
