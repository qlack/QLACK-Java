package com.eurodyn.qlack.fuse.cm.mapper;

import com.eurodyn.qlack.fuse.cm.dto.NodeAttributeDTO;
import com.eurodyn.qlack.fuse.cm.model.Node;
import com.eurodyn.qlack.fuse.cm.model.NodeAttribute;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NodeAttributeMapper extends
  CMBaseMapper<NodeAttribute, NodeAttributeDTO> {

  @Override
  @Mapping(target = "nodeId", source = "node", qualifiedByName = "mapNode")
  NodeAttributeDTO mapToDTO(NodeAttribute nodeAttribute);

  /**
   * Maps the Node value
   *
   * @param node the Node of the attribute
   * @return the id of the Node
   */
  @Named("mapNode")
  default String mapNode(Node node) {
    if (node == null) {
      return null;
    }
    String id = node.getId();
    return id;
  }

}
