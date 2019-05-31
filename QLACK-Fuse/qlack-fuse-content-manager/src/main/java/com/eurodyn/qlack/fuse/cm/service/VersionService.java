package com.eurodyn.qlack.fuse.cm.service;

import com.eurodyn.qlack.fuse.cm.dto.BinChunkDTO;
import com.eurodyn.qlack.fuse.cm.dto.VersionAttributeDTO;
import com.eurodyn.qlack.fuse.cm.dto.VersionDTO;
import com.eurodyn.qlack.fuse.cm.enums.NodeType;
import com.eurodyn.qlack.fuse.cm.exception.QIOException;
import com.eurodyn.qlack.fuse.cm.exception.QNodeLockException;
import com.eurodyn.qlack.fuse.cm.mappers.VersionMapper;
import com.eurodyn.qlack.fuse.cm.model.Node;
import com.eurodyn.qlack.fuse.cm.model.QVersion;
import com.eurodyn.qlack.fuse.cm.model.QVersionDeleted;
import com.eurodyn.qlack.fuse.cm.model.Version;
import com.eurodyn.qlack.fuse.cm.model.VersionAttribute;
import com.eurodyn.qlack.fuse.cm.model.VersionDeleted;
import com.eurodyn.qlack.fuse.cm.repository.NodeRepository;
import com.eurodyn.qlack.fuse.cm.repository.VersionRepository;
import com.eurodyn.qlack.fuse.cm.storage.StorageEngine;
import com.eurodyn.qlack.fuse.cm.storage.StorageEngineFactory;
import com.eurodyn.qlack.fuse.cm.util.CMConstants;
import com.eurodyn.qlack.fuse.cm.util.NodeAttributeStringBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.StoredProcedureQuery;
import javax.transaction.Transactional;
import lombok.extern.java.Log;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.exception.TikaException;
import org.apache.tika.io.TikaInputStream;
import org.apache.tika.metadata.Metadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author European Dynamics
 */

@Service
@Transactional
@Log
public class VersionService {

  private static final String DEFAULT_MIME_TYPE = "application/octet-stream";
  @PersistenceContext
  EntityManager em;
  @Autowired
  private ConcurrencyControlService concurrencyControlService;
  @Autowired
  private StorageEngineFactory storageEngineFactory;
  private StorageEngine storageEngine;
  private NodeRepository nodeRepository;
  private VersionRepository versionRepository;
  private VersionMapper versionMapper;
  private TikaConfig tika;
  private QVersion qVersion = QVersion.version;

  @Autowired
  public VersionService(NodeRepository nodeRepository, VersionRepository versionRepository,
    VersionMapper versionMapper) {
    this.nodeRepository = nodeRepository;
    this.versionRepository = versionRepository;
    this.versionMapper = versionMapper;
  }

  @PostConstruct
  public void init() throws TikaException, IOException {
    // If the storage engine has not been explicitly set, create one using
    // the default configuration from StorageEngineFactory.
    if (storageEngine == null) {
      storageEngine = storageEngineFactory.getEngine();
    }
    tika = new TikaConfig();
  }

  /**
   * Creates the version.
   *
   * @param fileID the file ID
   * @param cmVersion the cm version
   * @param filename the filename
   * @param content the content
   * @param userID the user ID
   * @param lockToken the lock token
   * @return the string
   * @throws QNodeLockException the q node lock exception
   */
  public String createVersion(String fileID, VersionDTO cmVersion, String filename, byte[] content, String userID, String lockToken)
    throws QNodeLockException {
    Node file = nodeRepository.findByIdAndType(fileID, NodeType.FILE);

    // Check whether there is a lock conflict with the current node.
    concurrencyControlService.isSelectedNodeLocked(fileID, lockToken,
      "The selected file is locked and an invalid lock token was passed; A new version cannot be created for this file.");

    // Check for ancestor node (folder) lock conflicts.
    if (file.getParent() != null) {
      concurrencyControlService.isParentNodeLocked(file.getParent().getId(), lockToken,
        "An ancestor folder is locked and an invalid lock token was passed; the folder cannot be created.");
    }

    Version version = new Version();
    version.setName(cmVersion.getName());
    version.setNode(file);
    version.setFilename(filename);

    if (content != null) {
      version.setMimetype(getMimeType(content));
    } else if (cmVersion.getMimetype() != null) {
      version.setMimetype(cmVersion.getMimetype());
    }

    if (content != null) {
      version.setSize(new Long(content.length));
    } else {
      version.setSize(cmVersion.getSize());
    }

    // Set created / last modified information
    version.setAttributes(new ArrayList<VersionAttribute>());
    long dateInMillis = Calendar.getInstance().getTimeInMillis();
    version.setCreatedOn(dateInMillis);
    version.getAttributes().add(new VersionAttribute(CMConstants.ATTR_CREATED_BY, userID, version));
    version.getAttributes().add(new VersionAttribute(CMConstants.ATTR_LAST_MODIFIED_ON, String.valueOf(dateInMillis), version));
    version.getAttributes().add(new VersionAttribute(CMConstants.ATTR_LAST_MODIFIED_BY, userID, version));

    // Set custom created version attributes
    if (cmVersion.getAttributes() != null) {
      for (VersionAttributeDTO versionAttributeDTO : cmVersion.getAttributes()) {
        version.getAttributes().add(new VersionAttribute(versionAttributeDTO.getName(), versionAttributeDTO.getValue(), version));
      }
    }

    versionRepository.save(version);

    if (content != null) {
      storageEngine.setVersionContent(version.getId(), content);
    }

    return version.getId();
  }

  /**
   * Updates the values, content as also the attributes of a specific version.
   *
   * @param fileID the file ID
   * @param versionDTO the version DTO
   * @param content the content
   * @param userID the user ID
   * @param updateAllAttributes A true value defines that all attributes should be updated/deleted. Only the custom created attributes are
   * allowed to be deleted. A false value updates only the mandatory attribute values
   * @param lockToken the lock token
   */
  void updateVersion(String fileID, VersionDTO versionDTO, byte[] content, String userID, boolean updateAllAttributes, String lockToken) {
    Node file = nodeRepository.fetchById(fileID);

    concurrencyControlService.isSelectedNodeLocked(fileID, lockToken,
      "File with ID " + file + " is locked and an invalid lock token was passed; the file version attributes cannot be updated.");

    Version version = versionRepository.findByNameAndNode(versionDTO.getName(), file);

    version.setName(versionDTO.getName());
    version.setNode(file);
    version.setFilename(versionDTO.getFilename());

    if (content != null) {
      version.setSize(new Long(content.length));
    } else {
      version.setSize(versionDTO.getSize());
    }

    if (content != null) {
      version.setMimetype(getMimeType(content));
    } else if (versionDTO.getMimetype() != null) {
      version.setMimetype(versionDTO.getMimetype());
    }

    List<VersionAttribute> attributeToRemove = new ArrayList<>();
    if (updateAllAttributes) {
      for (VersionAttribute attrEntity : version.getAttributes()) {
        if (!attrEntity.getName().equals(CMConstants.ATTR_LAST_MODIFIED_ON)
          && !attrEntity.getName().equals(CMConstants.ATTR_LAST_MODIFIED_BY)
          && !attrEntity.getName().equals(CMConstants.ATTR_CREATED_BY)) {
          if (versionDTO.getAttributes() != null) {
            if (
              versionDTO.getAttributes().stream().filter(versionAttributeDTO -> versionAttributeDTO.getName().equals(attrEntity.getName()))
                .findFirst().orElse(null) != null) {
              attributeToRemove.add(version.getAttribute(attrEntity.getName()));
              version.removeAttribute(attrEntity.getName());
            }
          }
        }
      }
    }

    version.getAttributes().removeAll(attributeToRemove);

    // Update last modified information, if not existing, create
    if (userID != null) {
      long timeInMillis = Calendar.getInstance().getTimeInMillis();
      version.setAttribute(CMConstants.ATTR_LAST_MODIFIED_ON, String.valueOf(timeInMillis));
      version.setAttribute(CMConstants.ATTR_LAST_MODIFIED_BY, userID);
    }

    // Set values to the existing custom created attributes or create new attributes
    if (versionDTO.getAttributes() != null) {
      Set<String> excludedAttributeNames = Stream
        .of(CMConstants.ATTR_LAST_MODIFIED_ON, CMConstants.ATTR_LAST_MODIFIED_BY, CMConstants.ATTR_CREATED_BY)
        .collect(Collectors.toSet());
      versionDTO.getAttributes().forEach(versionAttributeDTO -> {
        if (!excludedAttributeNames.contains(versionAttributeDTO.getName())) {
          version.setAttribute(versionAttributeDTO.getName(), versionAttributeDTO.getValue());
        }
      });
    }
  }

  /**
   * Delete version.
   *
   * @param versionId the version id
   * @param lockToken the lock token
   */
  public void deleteVersion(String versionId, String lockToken) {
    Version version = versionRepository.fetchById(versionId);
    Node file = version.getNode();

    concurrencyControlService.isSelectedNodeLocked(file.getId(), lockToken, "File with ID " + file + " is locked and an "
      + "invalid lock token was passed; the file version attributes cannot be deleted.");

    versionRepository.delete(version);
  }

  /**
   * Gets the version by id.
   *
   * @param versionId the version id
   * @return the version by id
   */
  public VersionDTO getVersionById(String versionId) {
    return versionMapper.mapToDTO(versionRepository.fetchById(versionId));
  }

  /**
   * Gets the file versions.
   *
   * @param fileID the file ID
   * @return the file versions
   */
  public List<VersionDTO> getFileVersions(String fileID) {
    Node node = nodeRepository.fetchById(fileID);
    return versionMapper.mapToDTO(versionRepository.findByNode(node));
  }

  /**
   * Gets the file latest version.
   *
   * @param fileID the file ID
   * @return the file latest version
   */
  public VersionDTO getFileLatestVersion(String fileID) {
    List<VersionDTO> fileVersions = getFileVersions(fileID);
    return fileVersions.stream().max(Comparator.comparing(VersionDTO::getLastModifiedOn)).orElse(null);
  }

  /**
   * Gets the bin content.
   *
   * @param fileID the file ID
   * @return the bin content
   */
  public byte[] getBinContent(String fileID) {
    return getBinContent(fileID, null);
  }

  /**
   * Gets the bin content.
   *
   * @param fileID the file ID
   * @param versionName the version name
   * @return the bin content
   */
  public byte[] getBinContent(String fileID, String versionName) {
    Node file = nodeRepository.fetchById(fileID);
    Version version = versionRepository.findByNameAndNode(versionName, file);

    try {
      return storageEngine.getVersionContent(version.getId());
    } catch (IOException e) {
      throw new QIOException(MessageFormat.format("Could not obtain content for file " + "{0}, version {1}", fileID, versionName));
    }
  }

  /**
   * Gets the file as zip.
   *
   * @param fileID the file ID
   * @param includeProperties the include properties
   * @return the file as zip
   */
  public byte[] getFileAsZip(String fileID, boolean includeProperties) {
    return getFileAsZip(fileID, null, includeProperties);
  }

  /**
   * Gets the file as zip.
   *
   * @param fileID the file ID
   * @param versionName the version name
   * @param includeProperties the include properties
   * @return the file as zip
   */
  public byte[] getFileAsZip(String fileID, String versionName, boolean includeProperties) {

    Node file = nodeRepository.fetchById(fileID);
    Version version = versionRepository.findByNameAndNode(versionName, file);

    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    ZipOutputStream zipFile = new ZipOutputStream(outStream);

    try {
      // Write binary content
      ZipEntry entry = new ZipEntry(version.getFilename());
      zipFile.putNextEntry(entry);
      zipFile.write(getBinContent(fileID, versionName));

      if (includeProperties) {
        // Write file properties
        entry = new ZipEntry(file.getAttribute(CMConstants.ATTR_NAME) + ".properties");
        zipFile.putNextEntry(entry);

        StringBuilder buf = new NodeAttributeStringBuilder().nodeAttributeBuilder(file);
        zipFile.write(buf.toString().getBytes());

        // Write version properties - written in a separate file since
        // there are some properties which exist both in the file and in
        // the version (ex. last modified on/by)
        entry = new ZipEntry(version.getName() + ".properties");
        zipFile.putNextEntry(entry);
        buf = new StringBuilder();
        // Include a created on property
        buf.append(CMConstants.CREATED_ON).append(" = ").append(file.getCreatedOn()).append("\n");
        for (VersionAttribute attribute : version.getAttributes()) {
          buf.append(attribute.getName());
          buf.append(" = ");
          buf.append(attribute.getValue());
          buf.append("\n");
        }

        zipFile.write(buf.toString().getBytes());
      }
      zipFile.close();
      outStream.close();
    } catch (IOException ex) {
      log.log(Level.SEVERE, ex.getLocalizedMessage(), ex);
      throw new QIOException(
        "Error writing ZIP for version " + versionName + " of file  with ID " + fileID);
    }

    return outStream.toByteArray();
  }

  /**
   * Sets the bin chunk.
   *
   * @param versionID the version ID
   * @param content the content
   * @param chunkIndex the chunk index
   * @return the string
   */
  public String setBinChunk(String versionID, byte[] content, int chunkIndex) {
    String binChunkID = storageEngine.setBinChunk(versionID, content, chunkIndex);

    if (chunkIndex == 1) {
      String mimeType = getMimeType(content);
      if (StringUtils.hasText(mimeType)) {
        Version version = versionRepository.fetchById(versionID);
        version.setMimetype(mimeType);
        versionRepository.save(version);
      }
    }

    return binChunkID;
  }

  /**
   * Gets the bin chunk.
   *
   * @param versionID the version ID
   * @param chunkIndex the chunk index
   * @return the bin chunk
   */
  public BinChunkDTO getBinChunk(String versionID, int chunkIndex) {
    return storageEngine.getBinChunk(versionID, chunkIndex);
  }

  /**
   * Update attribute.
   *
   * @param fileID the file ID
   * @param attributeName the attribute name
   * @param attributeValue the attribute value
   * @param userID the user ID
   * @param lockToken the lock token
   * @throws QNodeLockException the q node lock exception
   */
  public void updateAttribute(String fileID, String attributeName, String attributeValue, String userID, String lockToken)
    throws QNodeLockException {
    updateAttribute(fileID, attributeName, attributeValue, userID, lockToken, null);
  }

  /**
   * Update attribute.
   *
   * @param fileID the file ID
   * @param versionName the version name
   * @param attributeName the attribute name
   * @param attributeValue the attribute value
   * @param userID the user ID
   * @param lockToken the lock token
   * @throws QNodeLockException the q node lock exception
   */
  public void updateAttribute(String fileID, String attributeName, String attributeValue, String userID, String lockToken,
    String versionName)
    throws QNodeLockException {
    Node file = nodeRepository.fetchById(fileID);
    Version version = versionRepository.findByNameAndNode(versionName, file);

    concurrencyControlService.isSelectedNodeLocked(fileID, lockToken,
      "File with ID " + file + " is locked and an invalid lock token was passed; the file version attributes cannot be updated.");

    version.setAttribute(attributeName, attributeValue);

    if (userID != null) {
      Long timeInMillis = Calendar.getInstance().getTimeInMillis();
      version.setAttribute(CMConstants.ATTR_LAST_MODIFIED_ON, String.valueOf(timeInMillis));
      version.setAttribute(CMConstants.ATTR_LAST_MODIFIED_BY, userID);
    }

    versionRepository.save(version);
  }

  /**
   * Update attributes.
   *
   * @param fileID the file ID
   * @param attributes the attributes
   * @param userID the user ID
   * @param lockToken the lock token
   * @throws QNodeLockException the q node lock exception
   */
  public void updateAttributes(String fileID, Map<String, String> attributes, String userID, String lockToken) throws QNodeLockException {
    updateAttributes(fileID, attributes, userID, lockToken, null);
  }

  /**
   * Update attributes.
   *
   * @param fileID the file ID
   * @param versionName the version name
   * @param attributes the attributes
   * @param userID the user ID
   * @param lockToken the lock token
   * @throws QNodeLockException the q node lock exception
   */
  void updateAttributes(String fileID, Map<String, String> attributes, String userID, String lockToken, String versionName)
    throws QNodeLockException {
    Node file = nodeRepository.fetchById(fileID);
    Version version = versionRepository.findByNameAndNode(versionName, file);

    concurrencyControlService.isSelectedNodeLocked(fileID, lockToken,
      "File with ID " + file + " is locked and an invalid lock token was passed; the file version attributes cannot be updated.");

    for (String attributeName : attributes.keySet()) {
      version.setAttribute(attributeName, attributes.get(attributeName));
    }

    if (userID != null) {
      Long timeInMillis = Calendar.getInstance().getTimeInMillis();
      version.setAttribute(CMConstants.ATTR_LAST_MODIFIED_ON, String.valueOf(timeInMillis));
      version.setAttribute(CMConstants.ATTR_LAST_MODIFIED_BY, userID);
    }

    versionRepository.save(version);
  }

  /**
   * Delete attribute.
   *
   * @param fileID the file ID
   * @param attributeName the attribute name
   * @param userID the user ID
   * @param lockToken the lock token
   * @throws QNodeLockException the q node lock exception
   */
  public void deleteAttribute(String fileID, String attributeName, String userID, String lockToken) throws QNodeLockException {
    deleteAttribute(fileID, attributeName, userID, lockToken, null);
  }

  /**
   * Delete attribute.
   *
   * @param fileID the file ID
   * @param versionName the version name
   * @param attributeName the attribute name
   * @param userID the user ID
   * @param lockToken the lock token
   * @throws QNodeLockException the q node lock exception
   */
  public void deleteAttribute(String fileID, String attributeName, String userID, String lockToken, String versionName)
    throws QNodeLockException {
    Node file = nodeRepository.fetchById(fileID);
    Version version = versionRepository.findByNameAndNode(versionName, file);

    concurrencyControlService.isSelectedNodeLocked(fileID, lockToken,
      "File with ID " + file + " is locked and an invalid lock token was passed; the file version attributes cannot be deleted.");

    version.removeAttribute(attributeName);

    // Update last modified information
    if (userID != null) {
      long timeInMillis = Calendar.getInstance().getTimeInMillis();
      version.setAttribute(CMConstants.ATTR_LAST_MODIFIED_ON, String.valueOf(timeInMillis));
      version.setAttribute(CMConstants.ATTR_LAST_MODIFIED_BY, userID);
    }
  }

  /**
   * Cleanup FS.
   *
   * @param cycleLength the cycle length
   */
  public void cleanupFS(int cycleLength) {
    QVersionDeleted qVersionDeleted = QVersionDeleted.versionDeleted;
    List<VersionDeleted> vdList = new JPAQueryFactory(em).selectFrom(qVersionDeleted).limit(cycleLength)
      .setLockMode(LockModeType.PESSIMISTIC_WRITE).fetch();

    for (VersionDeleted vd : vdList) {
      storageEngine.deleteVersion(vd.getId());
      em.remove(vd);
    }
  }

  /**
   * Transfers the binary content from the flu_file temporary table to the cm_version_bin.
   *
   * @param attachmentID the ID of the chunks in the flu_file table. All the chunks of the same file have the same ID.
   * @param versionID The ID of the version to which is related with the binary content to be transfered.
   */
  public void transferFromFluToVersionBin(String attachmentID, String versionID) {
    StoredProcedureQuery query = em.createStoredProcedureQuery("flu_to_version_bin");
    query.registerStoredProcedureParameter("flu_file_ID", String.class, ParameterMode.IN);
    query.registerStoredProcedureParameter("version_ID", String.class, ParameterMode.IN);

    query.setParameter("flu_file_ID", attachmentID);
    query.setParameter("version_ID", versionID);
    query.executeUpdate();
  }

  /**
   * Retrieves the mime type of a provided binary content.
   *
   * @param fileContent The provided binary content.
   * @return The mime type of the content.
   */
  public String getMimeType(byte[] fileContent) {
    String retVal = DEFAULT_MIME_TYPE;
    InputStream stream = new ByteArrayInputStream(fileContent);
    try {
      retVal = tika.getDetector().detect(TikaInputStream.get(stream), new Metadata()).toString();
    } catch (IOException e) {
      log.log(Level.SEVERE, "Could not detect content-type.", e);
    } finally {
      try {
        stream.close();
      } catch (IOException e) {
        log.log(Level.SEVERE, e.getMessage(), e);
      }
    }
    return retVal;
  }


  /**
   * Sets the version content.
   *
   * @param versionID the version ID
   * @param content the content
   */
  public void replaceVersionContent(String versionID, byte[] content) {
    try {
      boolean deleted = storageEngine.deleteVersion(versionID);

      if (deleted) {
        storageEngine.setVersionContent(versionID, content);
      }
    } catch (Exception e) {
      log.log(Level.SEVERE, "Could not delete versionBin for version:" + versionID, e);
    }
  }

  /**
   * Retrieves the latest version by a given filename list under a node
   *
   * @param fileId the file id
   * @param filenameList the filename list
   * @return the versions by filename for file
   */
  public List<VersionDTO> getVersionsByFilenameForFile(String fileId, List<String> filenameList) {
    Predicate predicate = qVersion.filename.in(filenameList).and(qVersion.node.parent.id.eq(fileId));
    return versionMapper.mapToDTO(versionRepository.findAll(predicate));
  }


}
