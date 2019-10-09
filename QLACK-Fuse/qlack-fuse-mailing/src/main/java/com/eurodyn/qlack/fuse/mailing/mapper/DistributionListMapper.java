package com.eurodyn.qlack.fuse.mailing.mapper;

import com.eurodyn.qlack.fuse.mailing.dto.DistributionListDTO;
import com.eurodyn.qlack.fuse.mailing.model.DistributionList;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapping interface for <tt>DistributionList</tt> entities and DTOs
 *
 * @author European Dynamics SA.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DistributionListMapper extends
    MailingMapper<DistributionList, DistributionListDTO> {

}
