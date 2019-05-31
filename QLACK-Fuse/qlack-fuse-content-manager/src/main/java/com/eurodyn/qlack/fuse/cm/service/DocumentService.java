package com.eurodyn.qlack.fuse.cm.service;

import com.eurodyn.qlack.fuse.cm.dto.CreateFileAndVersionStatusDTO;
import com.eurodyn.qlack.fuse.cm.dto.FileDTO;
import com.eurodyn.qlack.fuse.cm.dto.FolderDTO;
import com.eurodyn.qlack.fuse.cm.dto.NodeDTO;
import com.eurodyn.qlack.fuse.cm.dto.VersionDTO;
import com.eurodyn.qlack.fuse.cm.enums.NodeType;
import com.eurodyn.qlack.fuse.cm.enums.RelativesType;
import com.eurodyn.qlack.fuse.cm.exception.QAncestorFolderLockException;
import com.eurodyn.qlack.fuse.cm.exception.QFileNotFoundException;
import com.eurodyn.qlack.fuse.cm.exception.QIOException;
import com.eurodyn.qlack.fuse.cm.exception.QInvalidPathException;
import com.eurodyn.qlack.fuse.cm.exception.QNodeLockException;
import com.eurodyn.qlack.fuse.cm.mappers.NodeMapper;
import com.eurodyn.qlack.fuse.cm.model.Node;
import com.eurodyn.qlack.fuse.cm.model.NodeAttribute;
import com.eurodyn.qlack.fuse.cm.model.QNode;
import com.eurodyn.qlack.fuse.cm.model.QNodeAttribute;
import com.eurodyn.qlack.fuse.cm.repository.NodeRepository;
import com.eurodyn.qlack.fuse.cm.util.CMConstants;
import com.eurodyn.qlack.fuse.cm.util.NodeAttributeStringBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author European Dynamics
 */
@Service
@Transactional
public class DocumentService {

  @Autowired
  public ConcurrencyControlService concurrencyControlService;
  @Autowired
  public VersionService versionService;
  private NodeRepository nodeRepository;
  private NodeMapper nodeMapper;
  @PersistenceContext()
  private EntityManager em;

  @Autowired
  public DocumentService(NodeRepository nodeRepository, NodeMapper mapper) {
    this.nodeRepository = nodeRepository;
    this.nodeMapper = mapper;
  }

  private void renameNode(String nodeID, String newName, String userID, String lockToken, NodeType nodeType)
    throws QNodeLockException, QFileNotFoundException {
    String type = nodeType.name().toLowerCase();
    Node node = nodeRepository.fetchById(nodeID);
    if (node == null) {
      throw new QFileNotFoundException("The " + type + " you want to rename does not exist");
    }

    concurrencyControlService.isSelectedNodeLocked(nodeID, lockToken,
      type + " with ID " + nodeID + " is locked and an invalid lock token was passed; the " + type + " cannot be renamed.");

    node.setAttribute(CMConstants.ATTR_NAME, newName);

    // Update last modified information
    if (userID != null) {
      long timeInMillis = Calendar.getInstance().getTimeInMillis();
      node.setAttribute(CMConstants.ATTR_LAST_MODIFIED_ON, String.valueOf(timeInMillis));
      node.setAttribute(CMConstants.ATTR_LAST_MODIFIED_BY, userID);
    }

    nodeRepository.save(node);
  }

  private void addNodeAttributes(NodeDTO dto, Node nodeEntity, String userID) {
    nodeEntity.setAttributes(new ArrayList<>());
    nodeEntity.getAttributes().add(new NodeAttribute(CMConstants.ATTR_NAME, dto.getName(), nodeEntity));
    nodeEntity.getAttributes().add(new NodeAttribute(CMConstants.LOCKABLE, String.valueOf(dto.isLockable()), nodeEntity));
    nodeEntity.getAttributes().add(new NodeAttribute(CMConstants.VERSIONABLE, String.valueOf(dto.isVersionable()), nodeEntity));
    addCreationAndModificationAttributes(nodeEntity, userID);
  }

  private void addCreationAndModificationAttributes(Node nodeEntity, String userID) {
    long timeInMillis = Calendar.getInstance().getTimeInMillis();
    nodeEntity.setCreatedOn(timeInMillis);
    nodeEntity.getAttributes().add(new NodeAttribute(CMConstants.ATTR_CREATED_BY, userID, nodeEntity));
    nodeEntity.getAttributes().add(new NodeAttribute(CMConstants.ATTR_LAST_MODIFIED_ON, String.valueOf(timeInMillis), nodeEntity));
    nodeEntity.getAttributes().add(new NodeAttribute(CMConstants.ATTR_LAST_MODIFIED_BY, userID, nodeEntity));
    nodeRepository.save(nodeEntity);
  }

  /**
   * Creates a new folder under a specific parent folder.
   *
   * @param folder The FolderDTO of the folder to be created.
   * @param userID The ID of the logged in user who is creating the folder.
   * @param lockToken A lock token id, which will used to examine whether the user is allowed to create a folder under the specific
   * hierarchy (check for locked ascendant).
   * @return The id of the newly created folder conflict
   * @throws QNodeLockException If the folder/node cannot be created under the specific hierarchy since an ascendant is already locked
   */
  public String createFolder(FolderDTO folder, String userID, String lockToken) throws QNodeLockException {
    if (folder.getParentId() != null) {
      Node parent = nodeRepository.fetchById(folder.getParentId());
      if (parent != null)
      // Check for ancestor node (folder) lock conflicts.
      {
        concurrencyControlService.isParentNodeLocked(parent.getId(), lockToken,
          "An ancestor folder is locked and an invalid lock token was passed; the folder cannot be created.");
      }
    }

    Node folderEntity = nodeMapper.mapToEntity(folder);
    //folderEntity.setParent(parent);
    addNodeAttributes(folder, folderEntity, userID);

    return folderEntity.getId();
  }

  /**
   * Deletes a folder .
   *
   * @param folderID The ID of the folder to be deleted.
   * @param lockToken A lock token id, which will used to examine whether the user is allowed to delete the folder under the specific
   * hierarchy. It checks for locked ascendant or if the folder has been locked by other user.
   * @throws QNodeLockException If the folder/node cannot be deleted under the specific hierarchy since an ascendant or the folder itself
   * is already locked
   * @throws QFileNotFoundException If the node to be deleted does not exist.
   */
  public void deleteFolder(String folderID, String lockToken) throws QNodeLockException, QFileNotFoundException {
    Node node = nodeRepository.fetchById(folderID);
    if (node == null) {
      throw new QFileNotFoundException("The folder you want to delete does not exist");
    }

    // Check whether there is a lock conflict with the current node.
    concurrencyControlService.isSelectedNodeLocked(folderID, lockToken,
      "The selected folder is locked and an invalid lock token was passed; the folder cannot be deleted.");

    // Check for ancestor node (folder) lock conflicts.
    if (node.getParent() != null) {
      concurrencyControlService.isParentNodeLocked(node.getParent().getId(), lockToken,
        "An ancestor folder is locked and an invalid lock token was passed; the folder cannot be deleted.");
    }

    // Check for descendant node lock conflicts
    concurrencyControlService.isDescendantNodeLocked(folderID, lockToken,
      "An descendant node is locked and an invalid lock token was passed; the folder cannot be deleted.");

    nodeRepository.delete(node);
  }

  /**
   * Rename folder.
   *
   * @param folderID the folder ID
   * @param newName the new name
   * @param userID the user id
   * @param lockToken the lock token
   * @throws QNodeLockException the q node lock exception
   * @throws QFileNotFoundException the q file not found exception
   */
  public void renameFolder(String folderID, String newName, String userID, String lockToken)
    throws QNodeLockException, QFileNotFoundException {
    renameNode(folderID, newName, userID, lockToken, NodeType.FOLDER);
  }

  /**
   * Finds an return a folder with the specific ID along with is children (optionally).
   *
   * @param folderID The ID of the folder to be retrieved.
   * @param lazyRelatives When true it will not compute the relatives (ancestors/descendats) of the required folder.
   * @param findPath When true the directory path until the required folder will be computed.
   * @return The FolderDTO which contains all the information about the folder.
   */
  public FolderDTO getFolderByID(String folderID, boolean lazyRelatives, boolean findPath) {
    //return folderDTOMapper.mapToDTO(nodeRepository.fetchById(folderID), lazyRelatives);
    RelativesType relativesType = lazyRelatives ? RelativesType.LAZY : RelativesType.EAGER;
    return nodeMapper.mapToFolderDTO(nodeRepository.fetchById(folderID), relativesType, findPath);
  }

  /**
   * Gets the content of a folder node in a zip file. This method retrieves the binary contents of all files included in the folder node
   * specified as well as their attributes if the includeAttributes argument is true. The contents of files included in other folders
   * contained by the folder specified are also retrieved in case the isDeep argument is true.
   *
   * @param folderID The ID of the folder the content of which is to be retrieved
   * @param includeProperties If true then a separate properties file will be created inside the final zip file for each node included in
   * the result, containing the nodes' properties in the form: propertyName = propertyValue
   * @param isDeep If true then the whole tree commencing by the specified folder will be traversed in order to be included in the final
   * result. Otherwise only the file nodes which are direct children of the specified folder will be included in the result.
   * @return The binary content (and properties if applicable) of the specified folder as a byte array representing a zip file.
   */
  public byte[] getFolderAsZip(String folderID, boolean includeProperties, boolean isDeep) {
    byte[] retVal = null;
    Node folder = nodeRepository.findByIdAndType(folderID, NodeType.FOLDER);
    if (folder == null) {
      throw new QFileNotFoundException("The folder you want to download does not exist");
    }

    String nodeName = folder.getAttribute(CMConstants.ATTR_NAME).getValue();

    boolean hasEntries = false;
    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    ZipOutputStream out = new ZipOutputStream(outStream);

    try {
      for (Node child : folder.getChildren()) {
        if (child.getType() == NodeType.FILE) {
          byte[] fileRetVal = versionService.getFileAsZip(child.getId(), includeProperties);
          if (fileRetVal != null) {
            hasEntries = true;
            ZipEntry entry =
              new ZipEntry(child.getAttribute(CMConstants.ATTR_NAME).getValue() + ".zip");
            out.putNextEntry(entry);
            out.write(fileRetVal, 0, fileRetVal.length);
          }
        } else if ((child.getType() == NodeType.FOLDER) && isDeep) {
          byte[] folderRetVal = getFolderAsZip(child.getId(), includeProperties, isDeep);
          if (folderRetVal != null) {
            hasEntries = true;
            ZipEntry entry =
              new ZipEntry(child.getAttribute(CMConstants.ATTR_NAME).getValue() + ".zip");
            out.putNextEntry(entry);
            out.write(folderRetVal, 0, folderRetVal.length);
          }
        }
      }

      if (includeProperties) {
        hasEntries = true;
        ZipEntry entry = new ZipEntry(nodeName + ".properties");
        out.putNextEntry(entry);

        StringBuilder buf = new NodeAttributeStringBuilder().nodeAttributeBuilder(folder);
        out.write(buf.toString().getBytes());
      }
      if (hasEntries) {
        out.close();
        retVal = outStream.toByteArray();
      }
    } catch (IOException ex) {
      throw new QIOException("Error writing ZIP for folder  with ID " + folderID);
    }

    return retVal;
  }

  // **********************
  // File functionalities
  // **********************

  /**
   * Creates the file.
   *
   * @param file the file
   * @param userID the user ID
   * @param lockToken the lock token
   * @return the string
   * @throws QNodeLockException the q node lock exception
   */
  public String createFile(FileDTO file, String userID, String lockToken) throws QNodeLockException {
    Node parent = null;
    if (file.getParentId() != null) {
      parent = nodeRepository.findByIdAndType(file.getParentId(), NodeType.FOLDER);
    }

    // Check for ancestor node (folder) lock conflicts.
    if (parent != null) {
      NodeDTO ancConflict =
        concurrencyControlService.getAncestorFolderWithLockConflict(parent.getId(), lockToken);
      // In case a conflict was found an exception is thrown
      if (ancConflict != null && ancConflict.getId() != null) {
        throw new QAncestorFolderLockException(
          "An ancestor folder is locked" + " and an"
            + " invalid lock token was passed; the file cannot be created.",
          ancConflict.getId(), ancConflict.getName());
      }
    }

    Node fileEntity = nodeMapper.mapToEntity(file, parent);
    fileEntity.setMimetype(file.getMimetype());
    fileEntity.setSize(file.getSize());
    addNodeAttributes(file, fileEntity, userID);
    return fileEntity.getId();
  }

  /**
   * Deletes a file .
   *
   * @param fileID The ID of the file to be deleted.
   * @param lockToken A lock token id, which will used to examine whether the user is allowed to delete the file, under the specific
   * hierarchy. It checks for locked ascendant or if the file itself has been locked by other user.
   * @throws QNodeLockException If the node cannot be deleted under the specific hierarchy since an ascendant or the folder itself is
   * already locked
   * @throws QFileNotFoundException If the node to be deleted does not exist.
   */
  public void deleteFile(String fileID, String lockToken) throws QNodeLockException, QFileNotFoundException {
    Node node = nodeRepository.fetchById(fileID);
    if (node == null) {
      throw new QFileNotFoundException("The file to delete does not exist");
    }

    // Check whether there is a lock conflict with the current node.
    concurrencyControlService.isSelectedNodeLocked(fileID, lockToken, ""
      + "The selected file is locked and an invalid lock token was passed; the file cannot be deleted.");

    // Check for ancestor node (folder) lock conflicts.
    if (node.getParent() != null) {
      concurrencyControlService.isParentNodeLocked(node.getParent().getId(), lockToken,
        "An ancestor folder is locked and an invalid lock token was passed; the file cannot be deleted.");
    }

    nodeRepository.delete(node);
  }

  /**
   * Rename file.
   *
   * @param fileID the file ID
   * @param newName the new name
   * @param userID the user ID
   * @param lockToken the lock token
   * @throws QNodeLockException the q node lock exception
   * @throws QFileNotFoundException the q file not found exception
   */
  public void renameFile(String fileID, String newName, String userID, String lockToken) throws QNodeLockException, QFileNotFoundException {
    renameNode(fileID, newName, userID, lockToken, NodeType.FILE);
  }

  /**
   * Finds an return a file with the specific ID along with its versions (optionally).
   *
   * @param fileID The ID of the folder to be retrieved.
   * @param includeVersions When true all versions of the file are included.
   * @param findPath When true the directory path until the required folder will be computed.
   * @return The FileDTO which contains all the information about the required file
   */
  public FileDTO getFileByID(String fileID, boolean includeVersions, boolean findPath) {
    RelativesType relativesType = includeVersions ? RelativesType.EAGER : RelativesType.LAZY;
    FileDTO retVal = nodeMapper.mapToFileDTO(nodeRepository.findByIdAndType(fileID, NodeType.FILE), findPath);
    if (includeVersions) {
      retVal.setVersions(versionService.getFileVersions(fileID));
    }

    return retVal;
  }

  // **********************
  // Common functionalities
  // **********************

  /**
   * Gets the node by ID.
   *
   * @param nodeID the node ID
   * @return the node by ID
   */
  public NodeDTO getNodeByID(String nodeID) {
    return nodeMapper.mapToDTO(nodeRepository.fetchById(nodeID), true);
  }

  /**
   * Retrieves a list of nodes of a specified parent. In addition the method can return a list of nodes of the given parent having the
   * attributes passed as a parameter
   *
   * @param parentId the parent id
   * @param attributes the attributes
   * @return the node by attributes
   */
  public List<NodeDTO> getNodeByAttributes(String parentId, Map<String, String> attributes) {
    StringBuilder sbQuery = new StringBuilder("SELECT n FROM Node n ");

    if (attributes != null && !attributes.isEmpty()) {

      int i = 0;

      for (@SuppressWarnings("unused")
        Map.Entry<String, String> entry : attributes.entrySet()) {
        i++;
        sbQuery.append("INNER JOIN  n.attributes attr").append(i).append(" WITH ( ");
        sbQuery.append("attr").append(i).append(".name = :attr_").append(i).append(" AND attr")
          .append(i).append(".value = :value_").append(i).append(")");
      }
    }

    sbQuery.append(" WHERE n.parent.id = :parentId ORDER BY n.createdOn ASC");

    Query query = em.createQuery(sbQuery.toString());
    query.setParameter("parentId", parentId);

    if (attributes != null) {
      int i = 0;

      for (Map.Entry<String, String> entry : attributes.entrySet()) {
        i++;
        query.setParameter("attr_" + i, entry.getKey());
        query.setParameter("value_" + i, entry.getValue());
      }
    }

    @SuppressWarnings("unchecked")
    List<Node> nodes = query.getResultList();

    return nodeMapper.mapToDTO(nodes);
  }

  /**
   * Gets the parent.
   *
   * @param nodeID the node ID
   * @param lazyRelatives the lazy relatives
   * @return the parent
   */
  public FolderDTO getParent(String nodeID, boolean lazyRelatives) {
    Node node = nodeRepository.fetchById(nodeID);
    //return folderDTOMapper.mapToDTO(node.getParent(), lazyRelatives);
    RelativesType relativesType = lazyRelatives ? RelativesType.LAZY : RelativesType.EAGER;
    return nodeMapper.mapToFolderDTO(node.getParent(), relativesType, false);
  }

  /**
   * Gets the ancestors.
   *
   * @param nodeID the node ID
   * @return the ancestors
   */
  public List<FolderDTO> getAncestors(String nodeID) {
    List<FolderDTO> retVal;
    Node node = nodeRepository.fetchById(nodeID);
    if (node.getParent() == null) {
      return new ArrayList<>();
    } else {
      retVal = getAncestors(node.getParent().getId());
    }

    //retVal.add(folderDTOMapper.mapToDTO(node.getParent(), true));
    retVal.add(nodeMapper.mapToFolderDTO(node.getParent(), RelativesType.LAZY, false));
    return retVal;
  }

  /**
   * Creates the attribute.
   *
   * @param nodeId the node id
   * @param attributeName the attribute name
   * @param attributeValue the attribute value
   * @param userId the user id
   * @param lockToken the lock token
   * @return the string
   * @throws QNodeLockException the q node lock exception
   * @throws QFileNotFoundException the q file not found exception
   */
  public String createAttribute(String nodeId, String attributeName, String attributeValue, String userId, String lockToken)
    throws QNodeLockException, QFileNotFoundException {
    Node node = nodeRepository.fetchById(nodeId);
    if (node == null) {
      throw new QFileNotFoundException(
        "The node, which attribute should be created does not exist.");
    }

    concurrencyControlService.isSelectedNodeLocked(nodeId, lockToken,
      "Node with ID " + nodeId + " is locked and an invalid lock token was passed; the file attributes cannot be updated.");

    NodeAttribute attribute = new NodeAttribute(attributeName, attributeValue, node);
    node.getAttributes().add(attribute);

    // Set created / last modified information
    if (userId != null) {
      long timeInMillis = Calendar.getInstance().getTimeInMillis();
      node.setCreatedOn(timeInMillis);
      node.setAttribute(CMConstants.ATTR_CREATED_BY, userId);
      node.setAttribute(CMConstants.ATTR_LAST_MODIFIED_ON, String.valueOf(timeInMillis));
      node.setAttribute(CMConstants.ATTR_LAST_MODIFIED_BY, userId);
    }

    nodeRepository.save(node);

    return attribute.getId();
  }

  /**
   * Update attribute.
   *
   * @param nodeID the node ID
   * @param attributeName the attribute name
   * @param attributeValue the attribute value
   * @param userID the user ID
   * @param lockToken the lock token
   * @throws QNodeLockException the q node lock exception
   * @throws QFileNotFoundException the q file not found exception
   */
  public void updateAttribute(String nodeID, String attributeName, String attributeValue, String userID, String lockToken)
    throws QNodeLockException, QFileNotFoundException {
    Node node = nodeRepository.fetchById(nodeID);
    if (node == null) {
      throw new QFileNotFoundException(
        "The node, which attribute should be updated does not exist.");
    }

    concurrencyControlService.isSelectedNodeLocked(nodeID, lockToken,
      "Node with ID " + nodeID + " is locked and an invalid lock token was passed; the file attributes cannot be updated.");

    node.setAttribute(attributeName, attributeValue);

    // Update last modified information
    if (userID != null) {
      long timeInMillis = Calendar.getInstance().getTimeInMillis();
      node.setAttribute(CMConstants.ATTR_LAST_MODIFIED_ON, String.valueOf(timeInMillis));
      node.setAttribute(CMConstants.ATTR_LAST_MODIFIED_BY, userID);
    }
    nodeRepository.save(node);
  }

  /**
   * Update attributes.
   *
   * @param nodeID the node ID
   * @param attributes the attributes
   * @param userID the user ID
   * @param lockToken the lock token
   * @throws QNodeLockException the q node lock exception
   */
  void updateAttributes(String nodeID, Map<String, String> attributes, String userID, String lockToken) throws QNodeLockException {
    Node node = nodeRepository.fetchById(nodeID);
    if (node == null) {
      throw new QFileNotFoundException("The node to update does not exist.");
    }

    concurrencyControlService.isSelectedNodeLocked(nodeID, lockToken,
      "Node with ID " + nodeID + " is locked and an invalid lock token was passed; the file attributes cannot be updated.");

    for (String attributeName : attributes.keySet()) {
      node.setAttribute(attributeName, attributes.get(attributeName));
    }

    // Update last modified information
    if (userID != null) {
      long timeInMillis = Calendar.getInstance().getTimeInMillis();
      node.setAttribute(CMConstants.ATTR_LAST_MODIFIED_ON, String.valueOf(timeInMillis));
      node.setAttribute(CMConstants.ATTR_LAST_MODIFIED_BY, userID);
    }
  }

  /**
   * Delete attribute.
   *
   * @param nodeID the node ID
   * @param attributeName the attribute name
   * @param userID the user ID
   * @param lockToken the lock token
   * @throws QNodeLockException the q node lock exception
   * @throws QFileNotFoundException the q file not found exception
   */
  public void deleteAttribute(String nodeID, String attributeName, String userID, String lockToken)
    throws QNodeLockException, QFileNotFoundException {

    Node node = nodeRepository.fetchById(nodeID);
    if (node == null) {
      throw new QFileNotFoundException("The node, which attributes should be deleted does not exist.");
    }

    concurrencyControlService.isSelectedNodeLocked(nodeID, lockToken,
      "Node with ID " + nodeID + " is locked and an invalid lock token was passed; the file attributes cannot be deleted.");

    node.removeAttribute(attributeName);

    // Update last modified information
    if (userID != null) {
      long timeInMillis = Calendar.getInstance().getTimeInMillis();
      node.setAttribute(CMConstants.ATTR_LAST_MODIFIED_ON, String.valueOf(timeInMillis));
      node.setAttribute(CMConstants.ATTR_LAST_MODIFIED_BY, userID);
    }

    nodeRepository.save(node);
  }

  /**
   * Copy.
   *
   * @param nodeID the node ID
   * @param newParentID the new parent ID
   * @param userID the user ID
   * @param lockToken the lock token
   * @return the string
   */
  public String copy(String nodeID, String newParentID, String userID, String lockToken) {
    Node node = nodeRepository.fetchById(nodeID);
    if (node == null) {
      throw new QFileNotFoundException("The node, to be copied does not exist.");
    }

    Node newParent = nodeRepository.findByIdAndType(newParentID, NodeType.FOLDER);
    concurrencyControlService.isSelectedNodeLocked(newParentID, lockToken,
      "Node with ID " + newParentID + " is locked and an invalid lock token was passed; a new node cannot be copied into it.");

    checkCyclicPath(nodeID, newParent);

    return copyNode(node, newParent, userID);
  }

  private String copyNode(Node node, Node newParent, String userID) {
    Node newNode = new Node();
    newNode.setType(node.getType());
    newNode.setParent(newParent);
    List<NodeAttribute> newAttributes = new ArrayList<>();
    newNode.setAttributes(newAttributes);

    // Copy attributes except created/modified/locked information
    for (NodeAttribute attribute : node.getAttributes()) {
      switch (attribute.getName()) {
        case CMConstants.ATTR_CREATED_BY:
        case CMConstants.ATTR_LAST_MODIFIED_BY:
        case CMConstants.ATTR_LAST_MODIFIED_ON:
        case CMConstants.ATTR_LOCKED_BY:
        case CMConstants.ATTR_LOCKED_ON:
          break;
        default:
          newNode.getAttributes()
            .add(new NodeAttribute(attribute.getName(), attribute.getValue(), newNode));
          break;
      }
    }

    // Set created / last modified information
    addCreationAndModificationAttributes(newNode, userID);

    for (Node child : node.getChildren()) {
      copyNode(child, newNode, userID);
    }

    return newNode.getId();
  }

  private void checkCyclicPath(String nodeID, Node newParent) {
    Node checkedNode = newParent;
    while (checkedNode != null) {
      if (checkedNode.getId().equals(nodeID)) {
        throw new QInvalidPathException(
          "Cannot move node with ID " + nodeID + " under node with ID " + newParent.getId()
            + " since this will create a cyclic path.");
      }
      checkedNode = checkedNode.getParent();
    }
  }

  /**
   * Move.
   *
   * @param nodeID the node ID
   * @param newParentID the new parent ID
   * @param userID the user ID
   * @param lockToken the lock token
   */
  public void move(String nodeID, String newParentID, String userID, String lockToken) {
    Node node = nodeRepository.fetchById(nodeID);
    if (node == null) {
      throw new QFileNotFoundException("The node, to be moved does not exist.");
    }

    Node newParent = nodeRepository.findByIdAndType(newParentID, NodeType.FOLDER);

    concurrencyControlService.isSelectedNodeLocked(nodeID, lockToken,
      "Node with ID " + nodeID + " is locked and an invalid lock token was passed; it cannot be moved.");

    concurrencyControlService.isParentNodeLocked(newParentID, lockToken,
      "Node with ID " + newParentID + " is locked and an invalid lock token was passed; a new node cannot be moved into it.");

    checkCyclicPath(nodeID, newParent);
    node.setParent(newParent);
    nodeRepository.save(node);
  }

  /**
   * Checks whether a file or folder with the same name, already exists in a specified directory.
   *
   * @param name The name of the new file which should be checked to find out if a duplicate name exists
   * @param parentNodeID The ID of the folder within which a file is a specified name is searched
   * @return true if the specified file name is unique in the folder.
   */
  public boolean isFileNameUnique(String name, String parentNodeID) {
    QNode qNode = QNode.node;
    QNodeAttribute qNodeAttribute = new QNodeAttribute("nodeAttribute");
    boolean isFileNameUnique = true;
    // Query retrieving the nodes (folder/file) which have a required
    // node attribute name.

    JPAQuery<Node> q = new JPAQueryFactory(em)
      .selectFrom(qNode).innerJoin(qNode.attributes, qNodeAttribute)
      .where(qNode.parent.id.eq(parentNodeID).and(qNodeAttribute.name.eq(CMConstants.ATTR_NAME)).and(qNodeAttribute.value.eq(name)));
    // Get the count of nodes which the specific name in odrer to find out whether the name is not unique
    long count = q.fetchCount();
    // In case the number is larger than zero it mean that the file name already exist
    if (count > 0) {
      isFileNameUnique = false;
    }
    return isFileNameUnique;
  }


  /**
   * Checks whether the folder or file names are unique in the specified directory and returns a lists on the duplicate.
   *
   * @param fileNames The file names which will be checked id it is unique in a provided directory.
   * @param parentId The id of the parent folder within which duplicate file and folder names are searched.
   * @return a lists on the duplicates.
   */
  private List<String> duplicateFileNamesInDirectory(List<String> fileNames, String parentId) {
    QNode qNode = QNode.node;
    QNodeAttribute qNodeAttribute = new QNodeAttribute("nodeAttribute");

    // Selects all the nodes that their name is contained in a list of strings
    JPAQuery<Node> q = new JPAQueryFactory(em).selectFrom(qNode).innerJoin(qNode.attributes, qNodeAttribute)
      .where(qNode.parent.id.eq(parentId).and(qNodeAttribute.name.eq(CMConstants.ATTR_NAME))
        .and(qNodeAttribute.value.in(fileNames)));

    List<Node> nodeResultList = q.fetchResults().getResults();
    List<String> namesList = new ArrayList<>();

    for (Node node : nodeResultList) {
      NodeDTO nodeDTO = nodeMapper.mapToDTO(node, true);
      namesList.add(nodeDTO.getName());
    }

    return namesList;
  }

  /**
   * Creates a new file as well as a new version for the specific file.
   *
   * @param file The FileDTO which contain all the new file information
   * @param cmVersion The new version.
   * @param content The binary content of the new version. It is optional, so null can be used instead.
   * @param userID The user ID of the creator.
   * @param lockToken The lock token to be used so as to avoid lock conflicts.
   * @return CreateFileAndVersionStatusDTO which contains the ids of the newly created file and version.
   */
  public CreateFileAndVersionStatusDTO createFileAndVersion(FileDTO file, VersionDTO cmVersion, byte[] content, String userID,
    String lockToken) {

    CreateFileAndVersionStatusDTO status = new CreateFileAndVersionStatusDTO();
    String newFileID = createFile(file, userID, lockToken);
    status.setFileID(newFileID);
    status.setVersionID(versionService.createVersion(newFileID, cmVersion, file.getName(), content, userID, lockToken));
    return status;
  }
}
