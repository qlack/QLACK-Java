package com.eurodyn.qlack.fuse.cm;

import com.eurodyn.qlack.fuse.cm.dto.FileDTO;
import com.eurodyn.qlack.fuse.cm.dto.FolderDTO;
import com.eurodyn.qlack.fuse.cm.dto.VersionDTO;
import com.eurodyn.qlack.fuse.cm.enums.NodeType;
import com.eurodyn.qlack.fuse.cm.model.Node;
import com.eurodyn.qlack.fuse.cm.model.NodeAttribute;
import com.eurodyn.qlack.fuse.cm.util.CMConstants;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * @author European Dynamics
 */
public class InitTestValues {

  public Node createNode(String id) {
    Node node = new Node();
    node.setId(id == null ? "219d51f2-6f78-4de8-bbf6-92091b34a7e0" : id);
    node.setType(NodeType.FOLDER);
    node.setAttributes(createAttributes(node));
    node.setChildren(new ArrayList<>());
    return node;
  }

  public List<NodeAttribute> createAttributes(Node node) {
    List<NodeAttribute> attributes = new ArrayList<>();

    NodeAttribute name = new NodeAttribute(CMConstants.ATTR_NAME, "test folder" + node.getId(), node);
    NodeAttribute lockable = new NodeAttribute(CMConstants.LOCKABLE, "true", node);
    NodeAttribute createdBy = new NodeAttribute(CMConstants.ATTR_CREATED_BY, "User#1", node);

    attributes.add(name);
    attributes.add(lockable);
    attributes.add(createdBy);

    return attributes;
  }

  public FolderDTO createFolderDTO(String nodeDTOId) {
    FolderDTO folderDTO = new FolderDTO();
    folderDTO.setId(nodeDTOId == null ? "219d51f2-6f78-4de8-bbf6-92091b34a7e0" : nodeDTOId);
    folderDTO.setName("test folder");
    folderDTO.setLockable(true);
    folderDTO.setCreatedBy("User#1");
    folderDTO.setChildren(new HashSet<>());
    return folderDTO;
  }

  public FileDTO createFileDTO() {
    FileDTO fileDTO = new FileDTO();
    fileDTO.setId("462823df-a508-4b47-804b-b16cc858db5c");
    fileDTO.setName("test file");
    fileDTO.setCreatedBy("User#1");
    fileDTO.setMimetype("some type");
    fileDTO.setSize(12345678L);
    return fileDTO;
  }

  public VersionDTO createVersionDTO() {
    VersionDTO versionDTO = new VersionDTO();
    versionDTO.setFilename("TEST_NAME");
    versionDTO.setId("fab6cd60-b078-4eb5-852a-d96565206584 ");
    return versionDTO;
  }
}
