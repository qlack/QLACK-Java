package com.eurodyn.qlack.fuse.mailing.util;

/**
 * Constants to be used in Mailing.
 *
 * @author European Dynamics SA.
 */
public class MailConstants {

  public static final String COMMA_TOKEN = ",";
  public static final String SEMI_COLON_TOKEN = ";";

  public static final String INACTIVE_STATUS = "INACTIVE";
  public static final String ACTIVE_STATUS = "ACTIVE";

  public static final String DAILY_RECURRING_OPTION = "DAILY";
  public static final String WEEKLY_RECURRING_OPTION = "WEEKLY";

  public static final String MARK_READ = "READ";
  public static final String MARK_UNREAD = "UNREAD";
  public static final String MARK_REPLIED = "REPLIED";

  public static final String INBOX_FOLDER_TYPE = "INBOX";
  public static final String SENT_FOLDER_TYPE = "SENT";

  public enum EMAIL_STATUS {
    QUEUED, SENT, FAILED, CANCELED
  }
}
