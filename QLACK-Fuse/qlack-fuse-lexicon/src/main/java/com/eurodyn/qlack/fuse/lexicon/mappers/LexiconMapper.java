package com.eurodyn.qlack.fuse.lexicon.mappers;

import java.util.List;

/** A Generic Lexicon Mapper Interface
 * @param <E>
 * @param <D>
 *
 * @author European Dynamics SA
 */
public interface LexiconMapper<E, D> {

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
