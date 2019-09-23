package com.eurodyn.qlack.fuse.mailing.repository;

import java.util.List;

import com.eurodyn.qlack.fuse.mailing.model.InternalAttachment;

/**
 * Repository interface for <tt>InternalAttachment</tt> entities
 *
 * @author European Dynamics SA.
 */
public interface InternalAttachmentRepository extends MailingRepository<InternalAttachment, String> {

	List<InternalAttachment> findByMessagesId(String messageId);
}
