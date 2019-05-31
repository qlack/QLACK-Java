package com.eurodyn.qlack.fuse.acv.service;

import com.eurodyn.qlack.fuse.acv.dto.ChangeDTO;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.javers.core.Javers;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides functionality to compare two objects of the same class.
 *
 * @author European Dynamics SA
 */
@Service
public class CompareService {

  private final Javers javers;

  private final VersioningService versioningService;

  @Autowired
  public CompareService(Javers javers, VersioningService versioningService) {
    this.javers = javers;
    this.versioningService = versioningService;
  }

  /**
   * Compares two objects and returns if they have changes or not.
   *
   * @param obj1 the first object to compare
   * @param obj2 the second object to compare
   * @param <T> the type of the objects to compare, so to ensure that the objects are of the same
   * class
   * @return true if the objects they have changes, else false
   */
  public <T> boolean hasChanges(@NonNull T obj1, @NonNull T obj2) {
    return javers.compare(obj1, obj2).hasChanges();
  }

  /**
   * Compares two objects and returns the changes.
   *
   * @param obj1 the first object to compare
   * @param obj2 the second object to compare
   * @param <T> the type of the objects to compare, so to ensure that the objects are of the same
   * class
   * @return a list with the changes between the two comparing objects
   */
  public <T> List<ChangeDTO> compare(@NonNull T obj1, @NonNull T obj2) {

    Diff result = javers.compare(obj1, obj2);

    List<ChangeDTO> changes = result.getChangesByType(ValueChange.class).parallelStream()
        .map(this::convertToChangeDTO).collect(Collectors.toList());

    //TODO added/removed objects

    return Collections.unmodifiableList(changes);
  }

  /**
   * Compares two object versions and returns the changes.
   * <br><br>
   * Throws a QDoesNotExistException when one of the requested versions doesn't exist.
   *
   * @param object the object to find the requested versions.
   * @param version1 number of the first version to compare with
   * @param version2 number of the second version to compare with
   * @return a list with the changes between the two comparing objects
   */
  public List<ChangeDTO> compareVersions(Object object, long version1, long version2) {

    return compare(versioningService.retrieveVersion(object, version1),
        versioningService.retrieveVersion(object, version2));
  }

  /**
   * Compares an object with a specific version and returns the changes.
   * <br><br>
   * Throws a QDoesNotExistException when the requested version doesn't exist.
   *
   * @param object the object to compare with the latest version.
   * @param version number of the version to compare with
   * @return a list with the changes between the two comparing objects
   */
  public List<ChangeDTO> compareObjectWithVersion(Object object, long version) {

    return compare(versioningService.retrieveVersion(object, version), object);
  }

  /**
   * Compares an object with the latest version and returns the changes.
   * <br><br>
   * Throws a QDoesNotExistException when no committed version exists.
   *
   * @param object the object to compare with the latest version.
   * @return a list with the changes between the two comparing objects
   */
  public List<ChangeDTO> compareObjectWithLatestVersion(Object object) {

    Object latestVersion = versioningService.retrieveLatestVersion(object);
    return compare(latestVersion, object);
  }

  private ChangeDTO convertToChangeDTO(ValueChange change) {
    ChangeDTO dto = new ChangeDTO();
    dto.setPropertyName(change.getPropertyNameWithPath());
    dto.setFrom(change.getLeft());
    dto.setTo(change.getRight());
    return dto;
  }

}