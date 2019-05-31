package com.eurodyn.qlack.fuse.mailing.mappers;

import org.mapstruct.Mapper;

import com.eurodyn.qlack.fuse.mailing.dto.DistributionListDTO;
import com.eurodyn.qlack.fuse.mailing.model.DistributionList;

@Mapper(componentModel = "spring")
public interface DistributionListMapper extends MailingMapper<DistributionList, DistributionListDTO> {

}
