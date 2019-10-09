package com.eurodyn.qlack.fuse.audit.mapper;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import com.eurodyn.qlack.fuse.audit.dto.AuditBaseDTO;
import java.util.List;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;

/**
 * Mapping interface for <tt>AuditBase</tt> entities and DTOs
 *
 * @author European Dynamics SA.
 */
public interface AuditBaseMapper<E extends QlackBaseModel, D extends AuditBaseDTO> {

  /**
   * Maps an entity to a DTO.
   *
   * @param entity the source entity
   * @return the mapped DTO
   */
  D mapToDTO(E entity);

  /**
   * Maps a list of entities to a list of DTO's.
   *
   * @param entity the source entities list
   * @return the mapped list of DTO's
   */
  List<D> mapToDTO(List<E> entity);

  /**
   * Maps a DTO to an entity.
   *
   * @param dto the source DTO
   * @return the mapped entity
   */
  E mapToEntity(D dto);

  /**
   * Maps a DTO to an existing entity.
   *
   * @param dto the source DTO
   * @param entity the origin entity
   */
  void mapToExistingEntity(D dto, @MappingTarget E entity);

  /**
   * Maps a list of DTO's to a list of entities.
   *
   * @param dto the source DTO's list
   * @return the mapped list of entities
   */
  List<E> mapToEntity(List<D> dto);

  /**
   * Maps a Spring {@link Page} of entities to a Spring {@link Page} of DTOs.
   *
   * @param all source object
   * @return the mapped object
   */
  default Page<D> map(Page<E> all) {
    return all.map(this::mapToDTO);
  }
}
