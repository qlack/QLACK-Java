package com.eurodyn.qlack.fuse.lexicon.mappers;

import com.eurodyn.qlack.fuse.lexicon.dto.KeyDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Data;
import com.eurodyn.qlack.fuse.lexicon.model.Key;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KeyMapper extends LexiconMapper<Key, KeyDTO> {

  @Mapping(target = "groupId", source = "group.id")
  @Mapping(target = "translations", source = "data", qualifiedByName = "mapData")
  KeyDTO mapToDTO(Key key, @Context boolean addTranslations);

  @Named("mapData")
  default Map<String, String> mapData(List<Data> data, @Context boolean addTranslations) {
    if (addTranslations) {
      if (Objects.nonNull(data)) {
        return data.stream().collect(Collectors.toMap(a -> a.getLanguage().getLocale(), a -> a.getValue()));
      }
    }
    return new HashMap<>();
  }
}
