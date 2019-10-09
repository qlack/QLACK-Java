package com.eurodyn.qlack.fuse.mailing.mapper;

import com.eurodyn.qlack.fuse.mailing.dto.InternalAttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.model.InternalAttachment;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
 * Mapping interface for <tt>InternalAttachment</tt> entities and DTOs
 *
 * @author European Dynamics SA.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface InternalAttachmentMapper extends
    MailingMapper<InternalAttachment, InternalAttachmentDTO> {

}
