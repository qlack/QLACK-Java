package com.eurodyn.qlack.fuse.mailing.mappers;

import java.util.Date;
import java.util.List;

import com.eurodyn.qlack.fuse.mailing.model.MailingModel;

public interface MailingMapper<E extends MailingModel, D > {
	
	/**
	 * Maps an entity to a DTO.
	 *
	 * @param entity
	 *            the source entity
	 * @return the mapped DTO
	 */
	D mapToDTO(E entity);

	/**
	 * Maps a list of entities to a list of DTO's.
	 *
	 * @param entity
	 *            the source entities list
	 * @return the mapped list of DTO's
	 */
	List<D> mapToDTO(List<E> entity);

	/**
	 * Maps a DTO to an entity.
	 *
	 * @param dto
	 *            the source DTO
	 * @return the mapped entity
	 */
	E mapToEntity(D dto);

	/**
	 * Maps a list of DTO's to a list of entities.
	 *
	 * @param dto
	 *            the source DTO's list
	 * @return the mapped list of entities
	 */
	List<E> mapToEntity(List<D> dto);
	
	
	default java.lang.Long map(java.util.Date value){
		return (value != null) ? value.getTime() : null;
	}

	default java.util.Date mapToDTO(java.lang.Long value) {
		return (value != null) ? new Date(value) : null;
	}
}
