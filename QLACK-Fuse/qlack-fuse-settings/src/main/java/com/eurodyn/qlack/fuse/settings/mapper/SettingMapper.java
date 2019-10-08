package com.eurodyn.qlack.fuse.settings.mapper;

import com.eurodyn.qlack.fuse.settings.dto.GroupDTO;
import com.eurodyn.qlack.fuse.settings.dto.SettingDTO;
import com.eurodyn.qlack.fuse.settings.model.Setting;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapping interface for <tt>Setting</tt> entities and DTOs
 *
 * @author European Dynamics SA.
 */
@Mapper(componentModel = "spring")
public interface SettingMapper {

  /**
   * Maps a list of {@link Setting} objects to a list of {@link SettingDTO} objects
   *
   * @param o the list of {@link Setting} objects
   * @return a list of {@link SettingDTO}
   */
  List<SettingDTO> map(List<Setting> o);

  /**
   * Maps a {@link Setting} to a {@link SettingDTO}
   *
   * @param o the {@link Setting} object
   * @return a {@link SettingDTO} object
   */
  SettingDTO map(Setting o);

  /**
   * Maps the group property of {@link Setting} class to the name property of the {@link GroupDTO}
   * class
   *
   * @param o the {@link Setting} object
   * @return a {@link GroupDTO} object
   */
  @Mapping(source = "group", target = "name")
  GroupDTO mapToGroupDTO(Setting o);

  /**
   * Maps a list of {@link Setting} objects to a listof {@link GroupDTO} objects
   *
   * @param o the list of {@link Setting} objects
   * @return a list of {@link GroupDTO} objects
   */
  List<GroupDTO> mapToGroupDTO(List<Setting> o);

  /**
   * Maps a {@link SettingDTO} to entity. The properties 'id', 'dbversion' are excluded from
   * mapping.
   *
   * @param dto the {@link SettingDTO} object
   * @return a {@link Setting} object
   */
  @Mapping(source = "id", target = "id", ignore = true)
  @Mapping(target = "dbversion", ignore = true)
  Setting mapToEntity(SettingDTO dto);
}
