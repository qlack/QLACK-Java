package com.eurodyn.qlack.fuse.rules.mapper;

import com.eurodyn.qlack.common.model.QlackBaseModel;
import java.util.List;

/**
 * Generic mapping interface for entities and DTOs of the <tt>qlack-fuse-rules</tt> module.
 *
 * @param <E> an entity class
 * @param <D> a DTO class
 * @author European Dynamics SA
 */
public interface RulesMapper<E extends QlackBaseModel, D> {

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
   * Maps a list of DTO's to a list of entities.
   *
   * @param dto the source DTO's list
   * @return the mapped list of entities
   */
  List<E> mapToEntity(List<D> dto);
}
