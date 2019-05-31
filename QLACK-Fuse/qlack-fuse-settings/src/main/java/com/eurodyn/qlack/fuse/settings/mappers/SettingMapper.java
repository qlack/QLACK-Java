package com.eurodyn.qlack.fuse.settings.mappers;

import com.eurodyn.qlack.fuse.settings.dto.GroupDTO;
import com.eurodyn.qlack.fuse.settings.dto.SettingDTO;
import com.eurodyn.qlack.fuse.settings.model.Setting;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SettingMapper {

  List<SettingDTO> map(List<Setting> o);

  SettingDTO map(Setting o);

  @Mapping(source = "group", target = "name")
  GroupDTO mapToGroupDTO(Setting o);

  List<GroupDTO> mapToGroupDTO(List<Setting> o);

  @Mapping(source = "id", target = "id", ignore = true)
  @Mapping(target = "dbversion", ignore = true)
  Setting mapToEntity(SettingDTO dto);
}
