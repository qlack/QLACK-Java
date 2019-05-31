package com.eurodyn.qlack.fuse.acv.service;

import com.eurodyn.qlack.common.exception.QDoesNotExistException;
import com.eurodyn.qlack.fuse.acv.dto.VersionDTO;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.javers.core.Javers;
import org.javers.core.commit.Commit;
import org.javers.core.commit.CommitMetadata;
import org.javers.repository.jql.JqlQuery;
import org.javers.repository.jql.QueryBuilder;
import org.javers.shadow.Shadow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides functionality to persist and retrieve versions of a given entity object.
 *
 * @author European Dynamics SA
 */
@Service
public class VersioningService {

  public static final String COMMIT_MESSAGE_KEY = "commit_message";

  private final Javers javers;

  @Autowired
  public VersioningService(Javers javers) {
    this.javers = javers;
  }

  /**
   * Persists a current state of a given domain object, creating so a createVersion of this object.
   *
   * @param author the author who commits the changes
   * @param obj the object to be committed
   * @param commitMessage a message regarding the commit
   * @return the number of the persisted version
   */
  public long createVersion(@NonNull String author, @NonNull Object obj, String commitMessage) {

    return createVersion(author, obj, commitMessage, new HashMap<>());
  }

  // TODO investigate if not to exposed any create operation

  /**
   * Persists a current state of a given domain object, creating so a createVersion of this object.
   *
   * @param author the author who commits the changes
   * @param obj the object to be committed
   * @param commitMessage a message regarding the commit
   * @param commitProperties additional commit properties
   * @return
   */
  public long createVersion(@NonNull String author, @NonNull Object obj, String commitMessage,
    @NonNull Map<String, String> commitProperties) {

    commitProperties.put(COMMIT_MESSAGE_KEY, commitMessage);
    Commit commit = javers.commit(author, obj, commitProperties);

    return commit.getId().getMajorId();
  }

  /**
   * Queries for an object's versions.
   *
   * @param object the object to retrieve versions for
   * @return A list of VersionDTO's, ordered in reverse chronological order
   */
  public List<VersionDTO> findVersions(@NonNull Object object) {

    JqlQuery query = QueryBuilder.byInstance(object).build();

    return findShadowsAndConvertToVersions(query);
  }

  /**
   * Returns a specific version of the requested object.
   * <br><br>
   * Throws a QDoesNotExistException when the requested version does not exist.
   *
   * @param object the object to retrieve the specific versions for
   * @param version the version to retrieve
   * @param <T> the class of the object to retrieve the version for
   * @return the object in the requested version
   */
  public <T> T retrieveVersion(T object, long version) {

    JqlQuery query = QueryBuilder.byInstance(object).build();

    List<T> versions = javers.findShadowsAndStream(query)
      .filter(shadow -> shadow.getCommitId().getMajorId() == version)
      .map(shadow -> (T) shadow.get()).collect(Collectors.toList());

    if (versions.size() != 1) {
      throw new QDoesNotExistException("version doesn't exist");
    }

    return versions.get(0);
  }

  /**
   * Returns the latest version of the requested object.
   * <br><br>
   * Throws a QDoesNotExistException when the requested version does not exist.
   *
   * @param object the object to retrieve the latest version for
   * @param <T> the class of the object to retrieve the latest version for
   * @return the latest version of the requested object
   */
  public <T> T retrieveLatestVersion(T object) {

    JqlQuery query = QueryBuilder.byInstance(object).build();

    Optional<Shadow<Object>> versions = javers.findShadowsAndStream(query).findFirst();

    if (versions.isPresent()) {
      return (T) versions.get().get();
    } else {
      throw new QDoesNotExistException("none version exists");
    }

  }

  private List<VersionDTO> findShadowsAndConvertToVersions(JqlQuery query) {
    List<VersionDTO> versions = javers.findShadowsAndStream(query)
      .map(this::convertToVersionDTO).collect(Collectors.toList());

    return Collections.unmodifiableList(versions);
  }

  private VersionDTO convertToVersionDTO(Shadow shadow) {

    CommitMetadata metadata = shadow.getCommitMetadata();

    return VersionDTO.builder().author(metadata.getAuthor())
      .commitDate(metadata.getCommitDateInstant())
      .commitMessage(metadata.getProperties().get(COMMIT_MESSAGE_KEY))
      .version(shadow.getCommitId().getMajorId())
      .build();
  }

}
