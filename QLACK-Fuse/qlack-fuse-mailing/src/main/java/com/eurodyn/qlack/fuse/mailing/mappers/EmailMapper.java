package com.eurodyn.qlack.fuse.mailing.mappers;

import com.eurodyn.qlack.fuse.mailing.dto.EmailDTO;
import com.eurodyn.qlack.fuse.mailing.model.Email;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Mapping interface for <tt>Email</tt> entities and DTOs
 *
 * @author European Dynamics SA.
 */
@Mapper(componentModel = "spring")
public interface EmailMapper extends MailingMapper<Email, EmailDTO> {

  /**
   * Maps a DTO to entity and also maps the recipients if
   * <tt>includeRecipients</tt> is set to true.
   */
  default Email mapToEntityWithRecipients(EmailDTO dto, boolean includeRecipients) {
    Email email = mapToEntity(dto);
    if (includeRecipients) {
      email.setToEmails(mapListToCsv(dto.getToEmails()));
      email.setCcEmails(mapListToCsv(dto.getCcEmails()));
      email.setBccEmails(mapListToCsv(dto.getBccEmails()));
      email.setReplyToEmails(mapListToCsv(dto.getReplyToEmails()));
    }
    return email;
  }

  /**
   * Converts a List of String values to a Comma Separated Value (CSV) String.
   *
   * @param emails a list of emails
   * @return emails as a csv string
   */
  default String mapListToCsv(List<String> emails) {
    StringBuilder emailAddress = new StringBuilder();
    if (emails != null && !emails.isEmpty()) {
      for (String email : emails) {
        if (emailAddress.length() > 0) {
          emailAddress.append(",");
        }
        emailAddress.append(email);
      }
    }
    return emailAddress.length() > 0 ? emailAddress.toString() : null;
  }

  /**
   * Converts a Comma Separated Value (CSV) String to a List of String values
   *
   * @param emails emails as a csv string
   * @return a list of emails
   */
  default List<String> mapCsvToList(String emails) {
    List<String> contacts = new ArrayList<>();
    if (emails == null) {
      return null;
    }

    StringTokenizer st = new StringTokenizer(emails, ",");
    while (st.hasMoreElements()) {
      String next = (String) st.nextElement();
      contacts.add(next);
    }
    return contacts.isEmpty() ? null : contacts;
  }

  /**
   * Maps an entity to DTO and also maps the recipients if
   * <tt>includeRecipients</tt> is set to true.
   */
  default EmailDTO mapToDTOWithRecipients(Email email, boolean includeRecipients) {

    EmailDTO maildto = mapToDTO(email);
    if (includeRecipients) {
      maildto.setToEmails(mapCsvToList(email.getToEmails()));
      maildto.setBccEmails(mapCsvToList(email.getBccEmails()));
      maildto.setCcEmails(mapCsvToList(email.getCcEmails()));
      maildto.setReplyToEmails(mapCsvToList(email.getReplyToEmails()));
    }
    return maildto;
  }

}
