package com.eurodyn.qlack.fuse.fileupload.repository;

import com.eurodyn.qlack.fuse.fileupload.model.DBFile;
import com.eurodyn.qlack.fuse.fileupload.model.DBFilePK;
import com.eurodyn.qlack.fuse.fileupload.model.QDBFile;
import com.querydsl.core.types.Predicate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.lang.NonNull;

public interface DBFileRepository extends JpaRepository<DBFile, DBFilePK>,
  QuerydslPredicateExecutor<DBFile> {


  @Override
  @NonNull
  List<DBFile> findAll(@NonNull Predicate predicate);

  @Override
  @NonNull
  List<DBFile> findAll(@NonNull Predicate predicate, @NonNull Sort sort);

  /**
   * Retrieves a chunk from the database
   *
   * @param id The file/chunk id
   * @param chunkOrder The chunk order number
   * @return the file
   */
  default DBFile getChunk(String id, Long chunkOrder) {
    QDBFile qdbFile = QDBFile.dBFile;
    Predicate predicate = qdbFile.dbFilePK.id.eq(id)
      .and(qdbFile.dbFilePK.chunkOrder.eq(chunkOrder));
    Optional<DBFile> optional = findOne(predicate);
    return optional.orElse(null);
  }

  /**
   * Deletes a file (including all of its chunks) from the database
   *
   * @param id The file id
   * @return the number of the deleted objects
   */
  default long deleteById(String id) {
    QDBFile qdbFile = QDBFile.dBFile;
    Predicate predicate = qdbFile.dbFilePK.id.eq(id);

    List<DBFile> dbFiles = findAll(predicate);
    dbFiles.stream().forEach(this::delete);

    return dbFiles.size();
  }

}
