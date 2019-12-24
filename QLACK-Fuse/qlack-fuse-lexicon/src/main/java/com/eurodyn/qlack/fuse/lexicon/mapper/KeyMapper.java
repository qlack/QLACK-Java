package com.eurodyn.qlack.fuse.lexicon.mapper;

import com.eurodyn.qlack.fuse.lexicon.dto.KeyDTO;
import com.eurodyn.qlack.fuse.lexicon.model.Data;
import com.eurodyn.qlack.fuse.lexicon.model.Group;
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

/**
 * An interface Mapper for Key to map its object with its value
 *
 * @author European Dynamics SA
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface KeyMapper extends LexiconMapper<Key, KeyDTO> {

  /**
   * Maps an entity Key to DTO
   *
   * @param key the Key entity
   * @param addTranslations an addTranslations check value
   * @return a mapped DTO
   */
  @Mapping(target = "groupId", source = "group", qualifiedByName = "mapGroup")
  @Mapping(target = "translations", source = "data", qualifiedByName = "mapData")
  KeyDTO mapToDTO(Key key, @Context boolean addTranslations);

  /**
   * Maps the Group value
   *
   * @param group the group of the key
   * @return the id of the Group
   */
  @Named("mapGroup")
  default String mapGroup(Group group) {
    if (group == null) {
      return null;
    }
    return group.getId();
  }

  /**
   * Maps tha Data
   *
   * @param data a list of lexicon data
   * @param addTranslations checking value
   * @return a new HashMap of data
   */
  @Named("mapData")
  default Map<String, String> mapData(List<Data> data,
    @Context boolean addTranslations) {
    if (addTranslations && Objects.nonNull(data)) {
      return data.stream()
        .collect(Collectors.toMap(a -> a.getLanguage().getLocale(),
          Data::getValue));
    }
    return new HashMap<>();
  }
}
