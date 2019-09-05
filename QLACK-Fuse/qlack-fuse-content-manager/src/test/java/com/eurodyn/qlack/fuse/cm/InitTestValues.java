package com.eurodyn.qlack.fuse.cm;

import com.eurodyn.qlack.fuse.cm.dto.BinChunkDTO;
import com.eurodyn.qlack.fuse.cm.dto.FileDTO;
import com.eurodyn.qlack.fuse.cm.dto.FolderDTO;
import com.eurodyn.qlack.fuse.cm.dto.NodeAttributeDTO;
import com.eurodyn.qlack.fuse.cm.dto.NodeDTO;
import com.eurodyn.qlack.fuse.cm.dto.VersionDTO;
import com.eurodyn.qlack.fuse.cm.enums.NodeType;
import com.eurodyn.qlack.fuse.cm.model.Node;
import com.eurodyn.qlack.fuse.cm.model.NodeAttribute;
import com.eurodyn.qlack.fuse.cm.model.Version;
import com.eurodyn.qlack.fuse.cm.model.VersionAttribute;
import com.eurodyn.qlack.fuse.cm.model.VersionBin;
import com.eurodyn.qlack.fuse.cm.util.CMConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

  public NodeDTO createNodeDTO(String id) {
    NodeDTO nodeDTO = new NodeDTO();
    nodeDTO.setId(id == null ? "219d51f2-6f78-4de8-bbf6-92091b34a7e0" : id);
    nodeDTO.setName("nodeName");
    return nodeDTO;
  }

  public List<NodeAttribute> createAttributes(Node node) {
    List<NodeAttribute> attributes = new ArrayList<>();

    NodeAttribute name = new NodeAttribute(CMConstants.ATTR_NAME, "test folder" + node.getId(),
        node);
    NodeAttribute lockable = new NodeAttribute(CMConstants.LOCKABLE, "true", node);
    NodeAttribute createdBy = new NodeAttribute(CMConstants.ATTR_CREATED_BY, "User#1", node);

    attributes.add(name);
    attributes.add(lockable);
    attributes.add(createdBy);

    return attributes;
  }

  public Set<NodeAttributeDTO> createNodeAttributesDTOSet(NodeAttributeDTO nodeAttributeDTO){
    Set<NodeAttributeDTO> nodeAttributeDTOSet = new HashSet<>();
    nodeAttributeDTOSet.add(nodeAttributeDTO);
    return nodeAttributeDTOSet;
  }

  public List<NodeAttributeDTO> createNodeAttributesDTOList(NodeAttributeDTO nodeAttributeDTO){
    List<NodeAttributeDTO> nodeAttributeDTOList = new ArrayList<>();
    nodeAttributeDTOList.add(nodeAttributeDTO);
    return nodeAttributeDTOList;
  }

  public NodeAttributeDTO createNodeAttributeDTO() {

    NodeAttributeDTO nodeAttributeDTO = new NodeAttributeDTO();
    nodeAttributeDTO.setName("nodeName");
    nodeAttributeDTO.setValue("Attribute#1");
    nodeAttributeDTO.setNodeId("nodeAttributeId");
    return nodeAttributeDTO;
  }

  public NodeAttribute createNodeAttribute() {

    NodeAttribute nodeAttribute = new NodeAttribute();
    nodeAttribute.setName("nodeName");
    nodeAttribute.setValue("Attribute#1");
    nodeAttribute.setNode(createNode("id"));
    return nodeAttribute;
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
    versionDTO.setFilename("test file");
    versionDTO.setId("fab6cd60-b078-4eb5-852a-d96565206584");
    versionDTO.setAttributes(new HashSet<>());
    versionDTO.setName("First Version");
    versionDTO.setLastModifiedOn(1559030049171L);
    return versionDTO;
  }

  public Version createVersion() {
    Version version = new Version();
    version.setFilename("test file");
    version.setId("fab6cd60-b078-4eb5-852a-d96565206584 ");
    version.setAttributes(new ArrayList<>());
    version.setName("First Version");
    version.setFilename("test file");
    return version;
  }

  public List<Version> createVersions() {
    List<Version> versions = new ArrayList<>();

    Version version = createVersion();
    version.setName("Second Version");
    version.setId("afb6cd60-a134-1c11-112b-d96565207870");

    versions.add(createVersion());
    versions.add(version);

    return versions;
  }

  public List<VersionDTO> createVersionsDTO() {
    List<VersionDTO> versionsDTO = new ArrayList<>();

    VersionDTO versionDTO = createVersionDTO();
    versionDTO.setName("Second Version");
    versionDTO.setId("afb6cd60-a134-1c11-112b-d96565207870");
    versionDTO.setLastModifiedOn(1559031149171L);

    versionsDTO.add(createVersionDTO());
    versionsDTO.add(versionDTO);

    return versionsDTO;
  }

  public List<VersionAttribute> createVersionAttributes(Version version) {
    List<VersionAttribute> versionAttributes = new ArrayList<>();

    VersionAttribute attribute = new VersionAttribute();
    attribute.setName(CMConstants.LOCKABLE);
    attribute.setValue("false");
    attribute.setVersion(version);

    versionAttributes.add(attribute);

    return versionAttributes;
  }

  public List<NodeDTO> createListNodeDTO(){
    List<NodeDTO> nodeDTOS = new ArrayList<>();
    nodeDTOS.add(createNodeDTO("nodeDTOId"));

    return nodeDTOS;
  }

  public List<Node> createListNode(){
    List<Node> nodeList = new ArrayList<>();
    nodeList.add(createNode("nodeId"));

    return nodeList;
  }

  public VersionBin createVersionBin(){
    VersionBin versionBin = new VersionBin();
    versionBin.setId("binId");
    versionBin.setVersion(createVersion());
    return versionBin;
  }

  public List<VersionBin> createVersionBinList(){
    List<VersionBin> versionBinList = new ArrayList<>();
    versionBinList.add(createVersionBin());
    return versionBinList;
  }

  public BinChunkDTO createBinChunkDTO(){
    BinChunkDTO binChunkDTO = new BinChunkDTO();
    binChunkDTO.setVersionID("versionId");
    binChunkDTO.setBinContent(new byte[1024]);
    return binChunkDTO;
  }

  public List<BinChunkDTO> createBinChunkDTOList(){
    List<BinChunkDTO> binChunkDTOList = new ArrayList<>();
    binChunkDTOList.add(createBinChunkDTO());
    return binChunkDTOList;
  }

}
