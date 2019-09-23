package com.eurodyn.qlack.fuse.mailing.mappers;

import org.mapstruct.Mapper;

import com.eurodyn.qlack.fuse.mailing.dto.DistributionListDTO;
import com.eurodyn.qlack.fuse.mailing.model.DistributionList;

/**
 * Mapping interface for <tt>DistributionList</tt> entities and DTOs
 *
 * @author European Dynamics SA.
 */
@Mapper(componentModel = "spring")
public interface DistributionListMapper extends MailingMapper<DistributionList, DistributionListDTO> {

}
