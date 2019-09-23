package com.eurodyn.qlack.fuse.mailing.util;


import com.eurodyn.qlack.fuse.mailing.dto.ContactDTO;
import com.eurodyn.qlack.fuse.mailing.dto.DistributionListDTO;
import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import com.eurodyn.qlack.fuse.mailing.dto.InternalAttachmentDTO;
import com.eurodyn.qlack.fuse.mailing.dto.InternalMessageDTO;
import com.eurodyn.qlack.fuse.mailing.model.Contact;
import com.eurodyn.qlack.fuse.mailing.model.DistributionList;
import com.eurodyn.qlack.fuse.mailing.model.Email;
import com.eurodyn.qlack.fuse.mailing.model.InternalAttachment;
import com.eurodyn.qlack.fuse.mailing.model.InternalMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

/**
 * This is utility class used for converting DTO's to entities and vice versa.
 *
 * @author European Dynamics SA.
 */
public class ConverterUtil {

  /**
   * Converts the entity DistributionList to data transfer object DistributionListDTO
   *
   * @param entity DistributionList entity.
   * @return DistributionListDTO Data transfer object, null if entity is null.
   */
  public static DistributionListDTO dlistConvert(DistributionList entity) {
    if (entity == null) {
      return null;
    }

    DistributionListDTO dto = new DistributionListDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setDescription(entity.getDescription());
    dto.setCreatedBy(entity.getCreatedBy());
    dto.setCreatedOn(entity.getCreatedOn());

    return dto;
  }

  /**
   * Converts the entity data transfer object DistributionListDTO to DistributionList.
   *
   * @param dto Data transfer object
   * @return DistributionList entity, null if DTO is null.
   */
  public static DistributionList dlistConvert(DistributionListDTO dto) {
    if (dto == null) {
      return null;
    }

    DistributionList entity = new DistributionList();
    entity.setName(dto.getName());
    entity.setDescription(dto.getDescription());

    return entity;
  }

  /**
   * Converts the entity data transfer object ContactDTO to Contact.
   * @param dto
   * @return
   */
  public static Contact contactConvert(ContactDTO dto) {
    Contact entity = new Contact();
    entity.setEmail(dto.getEmail());
    entity.setFirstName(dto.getFirstName());
    entity.setLastName(dto.getLastName());
    entity.setLocale(dto.getLocale());
    entity.setUserId(dto.getUserID());

    return entity;
  }

  /**
   * Creates a list of e-mails.
   *
   * @param emails String of e-mails separated by token .
   * @return list of e-mails.
   */
  public static List<String> createRecepientlist(String emails) {
    List<String> contacts = new ArrayList<>();

    StringTokenizer st = new StringTokenizer(emails, ",");
    while (st.hasMoreElements()) {
      String next = (String) st.nextElement();
      contacts.add(next.trim());
    }

    return contacts.isEmpty() ? null : contacts;
  }

  /**
   * Create e-mails string separated by token
   *
   * @param emails List of e-mails.
   * @return String of token separated e-mails.
   */
//  public static String createRecepientlist(List<String> emails) {
//
//    StringBuilder emailAddress = new StringBuilder();
//    if (emails != null && !emails.isEmpty()) {
//      for (String email : emails) {
//        if (emailAddress.length() > 0) {
//          emailAddress.append(",");
//        }
//        emailAddress.append(email);
//      }
//    }
//
//    return emailAddress.length() > 0 ? emailAddress.toString() : null;
//  }

  
  /**
   * Converts InternalMessageDTO DTO to InternalMessage without attachments.
   *
   * @param dto internal message data transfer object.
   * @return InternalMessage entity.
   */
  public static InternalMessage internalMessageConvert(InternalMessageDTO dto) {
    if (dto == null) {
      return null;
    }

    InternalMessage entity = new InternalMessage();

    entity.setSubject(dto.getSubject());
    entity.setMessage(dto.getMessage());
    entity.setMailFrom(dto.getMailFrom());
    entity.setMailTo(dto.getMailTo());
    entity.setDateSent(dto.getDateSent().getTime());
    entity.setDateReceived(dto.getDateReceived().getTime());

    return entity;
  }

  /**
   * Converts the InternalAttachment entity to data transfer object.
   *
   * @param dto internal message data transfer object.
   * @return InternalAttachment entity.
   */
  public static InternalAttachment internalAttachmentConvert(InternalAttachmentDTO dto) {
    if (dto == null) {
      return null;
    }

    InternalAttachment entity = new InternalAttachment();

    entity.setContentType(dto.getContentType());
    entity.setData(dto.getData());
    entity.setFilename(dto.getFilename());
    entity.setFormat(dto.getFormat());

    return entity;
  }

  /**
   * Converts InternalMessage entity to InternalMessageDTO.
   *
   * @param entity InternalMessage
   * @return InternalMessageDTO
   */
  public static InternalMessageDTO internalMessageConvert(InternalMessage entity) {
    if (entity == null) {
      return null;
    }

    InternalMessageDTO dto = new InternalMessageDTO();
    dto.setId(entity.getId());
    dto.setSubject(entity.getSubject());
    dto.setMessage(entity.getMessage());
    dto.setMailFrom(entity.getMailFrom());
    dto.setMailTo(entity.getMailTo());
    dto.setDateSent(entity.getDateSent());
    dto.setDateReceived(entity.getDateReceived());
    dto.setStatus(entity.getStatus());
    dto.setDeleteType(entity.getDeleteType());

    Set<InternalAttachment> entityAttachments = entity.getAttachments();

    List<InternalAttachmentDTO> dtoAttachments = new ArrayList<>();
    if (entityAttachments != null && !entityAttachments.isEmpty()) {
      for (InternalAttachment attachment : entityAttachments) {
        dtoAttachments.add(internalAttachmentConvert(attachment));
      }
    }
    dto.setAttachments(dtoAttachments);

    return dto;
  }

  /**
   * Converts the InternalAttachment entity to DTO.
   *
   * @param entity Internal attachment.
   * @return InternalAttachment entity.
   */
  public static InternalAttachmentDTO internalAttachmentConvert(InternalAttachment entity) {
    if (entity == null) {
      return null;
    }

    InternalAttachmentDTO dto = new InternalAttachmentDTO();

    dto.setId(entity.getId());
    dto.setMessagesId(entity.getMessages().getId());
    dto.setContentType(entity.getContentType());
    dto.setData(entity.getData());
    dto.setFilename(entity.getFilename());
    dto.setFormat(entity.getFormat());

    return dto;
  }

  /**
   * Converts list of InternalMessage entities to list data transfer object.
   *
   * @param internalMessageList list of InternalMessage entities.
   * @return list of data transfer object.
   */
  public static List<InternalMessageDTO> internalMessageConvertList(
      List<InternalMessage> internalMessageList) {
    List<InternalMessageDTO> messagesDtoList = new ArrayList<>();

    for (InternalMessage internalMessage : internalMessageList) {
      InternalMessageDTO dto = internalMessageConvert(internalMessage);
      messagesDtoList.add(dto);
    }

    return messagesDtoList;
  }

  /**
   * Converts the Email entity to DTO.
   *
   * @param entity Email.
   * @return EmailDTO
   */
  public static EmailDTO emailConvert(Email entity) {
    if (entity == null) {
      return null;
    }

    EmailDTO dto = new EmailDTO();
    dto.setId(entity.getId());
    dto.setDateSent(entity.getDateSent());
    dto.setStatus(entity.getStatus());
    dto.setBody(entity.getBody());
    dto.setFromEmail(entity.getFromEmail());
    dto.setServerResponse(entity.getServerResponse());
    dto.setSubject(entity.getSubject());
    dto.setStatus(entity.getStatus());
    dto.setCharset(entity.getCharset());

    return dto;
  }

}
