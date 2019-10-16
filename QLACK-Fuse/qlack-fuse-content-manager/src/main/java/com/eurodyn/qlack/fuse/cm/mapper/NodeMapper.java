package com.eurodyn.qlack.fuse.cm.mapper;

import com.eurodyn.qlack.fuse.cm.dto.BreadcrumbPartDTO;
import com.eurodyn.qlack.fuse.cm.dto.FileDTO;
import com.eurodyn.qlack.fuse.cm.dto.FolderDTO;
import com.eurodyn.qlack.fuse.cm.dto.NodeDTO;
import com.eurodyn.qlack.fuse.cm.enums.NodeType;
import com.eurodyn.qlack.fuse.cm.enums.RelativesType;
import com.eurodyn.qlack.fuse.cm.model.Node;
import com.eurodyn.qlack.fuse.cm.model.NodeAttribute;
import com.eurodyn.qlack.fuse.cm.util.CMConstants;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface NodeMapper extends CMBaseMapper<Node, NodeDTO> {

  @Mapping(source = "attributes", target = "name", qualifiedByName = "mapName")
  @Mapping(source = "attributes", target = "lockable", qualifiedByName = "mapLockable")
  @Mapping(source = "attributes", target = "versionable", qualifiedByName = "mapVersionable")
  @Mapping(source = "attributes", target = "createdBy", qualifiedByName = "mapCreatedBy")
  @Mapping(source = "attributes", target = "lastModifiedOn", qualifiedByName = "mapLastModifiedOn")
  @Mapping(source = "attributes", target = "lastModifiedBy", qualifiedByName = "mapLastModifiedBy")
  @Mapping(source = "attributes", target = "lockedOn", qualifiedByName = "mapLockedOn")
  @Mapping(source = "attributes", target = "lockedBy", qualifiedByName = "mapLockedBy")
  @Mapping(source = "parent", target = "parentId", qualifiedByName = "mapParent")
  NodeDTO mapToDTO(Node node, @Context boolean findPath);

  @Mapping(source = "attributes", target = "name", qualifiedByName = "mapName")
  @Mapping(source = "attributes", target = "lockable", qualifiedByName = "mapLockable")
  @Mapping(source = "attributes", target = "versionable", qualifiedByName = "mapVersionable")
  @Mapping(source = "attributes", target = "createdBy", qualifiedByName = "mapCreatedBy")
  @Mapping(source = "attributes", target = "lastModifiedOn", qualifiedByName = "mapLastModifiedOn")
  @Mapping(source = "attributes", target = "lastModifiedBy", qualifiedByName = "mapLastModifiedBy")
  @Mapping(source = "attributes", target = "lockedOn", qualifiedByName = "mapLockedOn")
  @Mapping(source = "attributes", target = "lockedBy", qualifiedByName = "mapLockedBy")
  @Mapping(target = "children", qualifiedByName = "mapChildren")
  @Mapping(source = "parent", target = "parentId", qualifiedByName = "mapParent")
  FolderDTO mapToFolderDTO(Node node, @Context RelativesType relativesType,
      @Context boolean findPath);

  @Mapping(source = "attributes", target = "name", qualifiedByName = "mapName")
  @Mapping(source = "attributes", target = "lockable", qualifiedByName = "mapLockable")
  @Mapping(source = "attributes", target = "versionable", qualifiedByName = "mapVersionable")
  @Mapping(source = "attributes", target = "createdBy", qualifiedByName = "mapCreatedBy")
  @Mapping(source = "attributes", target = "lastModifiedOn", qualifiedByName = "mapLastModifiedOn")
  @Mapping(source = "attributes", target = "lastModifiedBy", qualifiedByName = "mapLastModifiedBy")
  @Mapping(source = "attributes", target = "lockedOn", qualifiedByName = "mapLockedOn")
  @Mapping(source = "attributes", target = "lockedBy", qualifiedByName = "mapLockedBy")
  @Mapping(source = "parent", target = "parentId", qualifiedByName = "mapParent")
  FileDTO mapToFileDTO(Node node, @Context boolean findPath);

  @Mapping(target = "parent", ignore = true)
  Node mapToEntity(NodeDTO nodeDTO, @Context Node parent);

  default List<NodeDTO> mapToDTO(List<Node> nodes) {
    List<NodeDTO> nodesDTO = new ArrayList<>();
    nodes.forEach(node -> nodesDTO.add(this.mapToDTO(node, false)));
    return nodesDTO;
  }

  @AfterMapping
  default void setType(NodeDTO dto, @MappingTarget Node entity) {
    if (dto instanceof FolderDTO) {
      entity.setType(NodeType.FOLDER);
    } else if (dto instanceof FileDTO) {
      entity.setType(NodeType.FILE);
    }
  }

  @AfterMapping
  default void setParent(@MappingTarget Node entity, @Context Node parent) {
    if (parent != null) {
      entity.setParent(parent);
    }
  }

  @AfterMapping
  default <T extends NodeDTO> void mapPath(Node node, @MappingTarget T dto,
      @Context boolean findPath) {

    List<BreadcrumbPartDTO> path = new ArrayList<>();

    if (findPath) {
      while (node.getParent() != null) {
        BreadcrumbPartDTO part = new BreadcrumbPartDTO();
        part.setId(node.getId());
        if (node.getAttribute(CMConstants.ATTR_NAME) != null) {
          part.setName(node.getAttribute(CMConstants.ATTR_NAME).getValue());
        }
        path.add(part);
        node = node.getParent();
      }
    }

    dto.setPath(path);
  }

  @Named("mapName")
  default String mapName(List<NodeAttribute> attributes) {
    for (NodeAttribute nodeAttribute : attributes) {
      if (nodeAttribute.getName().equals(CMConstants.ATTR_NAME)) {
        return nodeAttribute.getValue();
      }
    }
    return null;
  }

  @Named("mapLockable")
  default boolean mapLockable(List<NodeAttribute> attributes) {
    for (NodeAttribute nodeAttribute : attributes) {
      if (nodeAttribute.getName().equals(CMConstants.LOCKABLE)) {
        return Boolean.valueOf(nodeAttribute.getValue());
      }
    }
    return false;
  }

  @Named("mapVersionable")
  default boolean mapVersionable(List<NodeAttribute> attributes) {
    for (NodeAttribute nodeAttribute : attributes) {
      if (nodeAttribute.getName().equals(CMConstants.VERSIONABLE)) {
        return Boolean.valueOf(nodeAttribute.getValue());
      }
    }
    return false;
  }

  @Named("mapCreatedBy")
  default String mapCreatedBy(List<NodeAttribute> attributes) {
    for (NodeAttribute nodeAttribute : attributes) {
      if (nodeAttribute.getName().equals(CMConstants.ATTR_CREATED_BY)) {
        return nodeAttribute.getValue();
      }
    }
    return null;
  }

  @Named("mapLastModifiedOn")
  default long mapLastModifiedOn(List<NodeAttribute> attributes) {
    for (NodeAttribute nodeAttribute : attributes) {
      if (nodeAttribute.getName().equals(CMConstants.ATTR_LAST_MODIFIED_ON)) {
        return Long.parseLong(nodeAttribute.getValue());
      }
    }
    return 0;
  }

  @Named("mapLastModifiedBy")
  default String mapLastModifiedBy(List<NodeAttribute> attributes) {
    for (NodeAttribute nodeAttribute : attributes) {
      if (nodeAttribute.getName().equals(CMConstants.ATTR_LAST_MODIFIED_BY)) {
        return nodeAttribute.getValue();
      }
    }
    return null;
  }

  @Named("mapLockedOn")
  default long mapLockedOn(List<NodeAttribute> attributes) {
    for (NodeAttribute nodeAttribute : attributes) {
      if (nodeAttribute.getName().equals(CMConstants.ATTR_LOCKED_ON)) {
        return Long.parseLong(nodeAttribute.getValue());
      }
    }
    return 0;
  }

  @Named("mapLockedBy")
  default String mapLockedBy(List<NodeAttribute> attributes) {
    for (NodeAttribute nodeAttribute : attributes) {
      if (nodeAttribute.getName().equals(CMConstants.ATTR_LOCKED_BY)) {
        return nodeAttribute.getValue();
      }
    }
    return null;
  }

  @Named("mapChildren")
  default Set<NodeDTO> mapChildren(List<Node> children, @Context RelativesType relativesType) {
    Set<NodeDTO> childrenDTO = new HashSet<>();
    if (relativesType.equals(RelativesType.EAGER)) {
      children.forEach(node -> childrenDTO.add(mapToDTO(node, true)));
    }
    return childrenDTO;
  }

  /**
   * Maps the Node Parent value
   *
   * @param parent the Node parent
   * @return the id of the parent
   */
  @Named("mapParent")
  default String mapParent(Node parent) {
    if (parent == null) {
      return null;
    }
    String id = parent.getId();
    if (id == null) {
      return null;
    }
    return id;
  }
}
