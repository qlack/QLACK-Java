package com.eurodyn.qlack.fuse.cm.mappers;

import com.eurodyn.qlack.fuse.cm.dto.NodeAttributeDTO;
import com.eurodyn.qlack.fuse.cm.model.NodeAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NodeAttributeMapper extends CMBaseMapper<NodeAttribute, NodeAttributeDTO> {

  @Override
  @Mapping(source = "node.id", target = "nodeId")
  NodeAttributeDTO mapToDTO(NodeAttribute nodeAttribute);
}
