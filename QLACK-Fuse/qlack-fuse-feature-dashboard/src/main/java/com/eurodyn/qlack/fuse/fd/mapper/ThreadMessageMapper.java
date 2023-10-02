package com.eurodyn.qlack.fuse.fd.mapper;

import static org.mapstruct.NullValueCheckStrategy.ALWAYS;

import com.eurodyn.qlack.fuse.fd.dto.ThreadMessageDTO;
import com.eurodyn.qlack.fuse.fd.model.ThreadMessage;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * An interface ThreadMessageMapper that is used to map the Thread objects with
 * their values.
 *
 * @author European Dynamics SA
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,  nullValueCheckStrategy = ALWAYS)
public interface ThreadMessageMapper extends FeatureDashboardMapper<ThreadMessage, ThreadMessageDTO> {

}
