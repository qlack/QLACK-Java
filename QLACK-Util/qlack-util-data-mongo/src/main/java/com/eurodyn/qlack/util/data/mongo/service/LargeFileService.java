package com.eurodyn.qlack.util.data.mongo.service;

import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.gridfs.GridFsCriteria.whereMetaData;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Large file support using GridFS for MongoDB.
 */
@Service
@Transactional
@Validated
public class LargeFileService {

  private final GridFsOperations gridFsOperations;
  private static final String PROPERTY_FILENAME = "filename";
  private static final String PROPERTY_ID = "id";

  /**
   * Default constructor.
   *
   * @param gridFsOperations An injected instance of Spring's {@link GridFsOperations}.
   */
  public LargeFileService(GridFsOperations gridFsOperations) {
    this.gridFsOperations = gridFsOperations;
  }

  /**
   * Reads a file from GridFS and returns it as a byte[].
   *
   * @param file The GridFS file to read.
   */
  private byte[] getFile(GridFSFile file) throws IOException {
    return Files.readAllBytes(new GridFsResource(file).getFile().toPath());
  }

  /**
   * Writes a file to GridFS.
   *
   * @param file The file to write.
   * @param metadata The metadata to accompany the file.
   * @return Returns the {@link ObjectId} of the newly created file.
   */
  public <T> ObjectId save(MultipartFile file, T metadata) throws IOException {
    return gridFsOperations
        .store(file.getInputStream(), file.getOriginalFilename(), file.getContentType(), metadata);
  }

  /**
   * Writes a file to GridFS.
   *
   * @param file The file to write.
   * @return Returns the {@link ObjectId} of the newly created file.
   */
  public ObjectId save(MultipartFile file) throws IOException {
    return gridFsOperations
        .store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
  }

  /**
   * Finds a file in GridFS by filename.
   *
   * @param filename The filename to lookup by.
   */
  public byte[] getFileByFilename(String filename) throws IOException {
    return getFile(gridFsOperations.findOne(query(whereMetaData(PROPERTY_FILENAME).is(filename))));
  }

  /**
   * Finds a file in GridFS by id.
   *
   * @param id The id to lookup by.
   */
  public byte[] getFileById(String id) throws IOException {
    return getFile(gridFsOperations.findOne(query(whereMetaData(PROPERTY_ID).is(id))));
  }

  /**
   * Finds a file in GridFS by metadata.
   *
   * @param metadataKey The metadata key to look for.
   * @param metadataValue The metadata value for the given key to look for.
   *
   */
  public <T> byte[] getFileByMetadata(String metadataKey, T metadataValue) throws IOException {
    return getFile(gridFsOperations.findOne(query(whereMetaData(metadataKey).is(metadataValue))));
  }

}
