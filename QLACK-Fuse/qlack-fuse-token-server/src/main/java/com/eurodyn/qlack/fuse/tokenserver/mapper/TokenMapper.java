package com.eurodyn.qlack.fuse.tokenserver.mapper;

import com.eurodyn.qlack.fuse.tokenserver.dto.TokenDTO;
import com.eurodyn.qlack.fuse.tokenserver.model.Token;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TokenMapper {

  List<TokenDTO> mapToDTO(List<Token> o);

  TokenDTO mapToDTO(Token o);

  @Mapping(source = "id", target = "id", ignore = true)
  Token mapToEntity(TokenDTO dto);
}