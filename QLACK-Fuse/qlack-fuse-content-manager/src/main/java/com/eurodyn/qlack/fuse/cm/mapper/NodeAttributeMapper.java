package com.eurodyn.qlack.fuse.cm.mapper;

import com.eurodyn.qlack.fuse.cm.dto.NodeAttributeDTO;
import com.eurodyn.qlack.fuse.cm.model.NodeAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NodeAttributeMapper extends CMBaseMapper<NodeAttribute, NodeAttributeDTO> {

  @Override
  @Mapping(source = "node.id", target = "nodeId")
  NodeAttributeDTO mapToDTO(NodeAttribute nodeAttribute);
}
