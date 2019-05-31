package com.eurodyn.qlack.fuse.fileupload.service;

import com.eurodyn.qlack.fuse.fileupload.dto.DBFileDTO;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.FileNotFoundException;
import java.util.List;


public interface FileUpload {

  /**
   * Checks if a chunk has already been uploaded
   *
   * @param id The file id
   * @param chunkNumber The chunk order number
   * @return true if chunk has already been uploaded, false if not
   */
  boolean checkChunk(String id, Long chunkNumber);

  /**
   * Uploads a chunk/file.
   *
   * @param dbFileDTO the file
   * @return true if file has been uploaded, false if not
   */
  boolean upload(DBFileDTO dbFileDTO);

  void deleteByID(String fileID);

  DBFileDTO getByID(String fileID);

  /**
   * Retrieves a specific chunk of a required file
   *
   * @param fileID the ID of the file from which a chunk will be retrieved
   * @param chunkNbr The number of the chunk
   *
   * @return ChunkGetResponse The response which will contain the retrieved chunk
   * @throws FileNotFoundException FileNotFoundException
   */
  DBFileDTO getByIDAndChunk(String fileID, long chunkNbr) throws FileNotFoundException;

  List<DBFileDTO> listFiles(boolean includeBinaryContent);

  /**
   * Cleans up file-chunks which have been uploaded but never reclaimed/deleted.
   *
   * @param deleteBefore The EPOCH before which all files get deleted.
   */
  void cleanupExpired(long deleteBefore);

  /**
   * Cleans up file-chunks which have been uploaded but never reclaimed/deleted. An implementation
   * of this method should calculate the EPOCH before which all files get deleted internally. Since
   * no arguments are passed, this method can be called with via Spring {@link Scheduled}
   * annotation.
   */
  void cleanupExpired();

  void deleteByIDForConsole(String fileID);

  DBFileDTO getByIDForConsole(String fileID);

  List<DBFileDTO> listFilesForConsole(boolean includeBinary);
}
