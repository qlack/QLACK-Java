package com.eurodyn.qlack.fuse.fd;

import com.eurodyn.qlack.fuse.fd.dto.ThreadMessageDTO;
import com.eurodyn.qlack.fuse.fd.dto.VoteDTO;
import com.eurodyn.qlack.fuse.fd.model.ThreadMessage;
import com.eurodyn.qlack.fuse.fd.model.Vote;
import com.eurodyn.qlack.fuse.fd.util.Reaction;
import com.eurodyn.qlack.fuse.fd.util.ThreadStatus;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


/**
 * @author European Dynamics
 */

public class InitTestValues {

  private ThreadMessage rootMessage;


  public ThreadMessage createThreadMessage() {
    rootMessage = new ThreadMessage();
    rootMessage.setBody(" message Body");
    rootMessage.setTitle("Test");
    rootMessage.setOwnershipMask("AAA");
    rootMessage.setAttributesMask("111");
    rootMessage.setAuthor("William Shakespeare");
    rootMessage.setCreatedOn(Instant.now());
    rootMessage.setDbversion(0L);
    rootMessage.setStatus(ThreadStatus.DRAFT);
    rootMessage.setId("11df58f1-2e26-4103-74ca-afc90ac955dd");
    return rootMessage;
  }


  public ThreadMessageDTO createThreadMessageDTO() {
    ThreadMessageDTO message = new ThreadMessageDTO();
    message.setBody(" message Body");
    message.setTitle("Test");
    message.setOwnershipMask("AAA");
    message.setAttributesMask("111");
    message.setAuthor("William Shakespeare");
    message.setCreatedOn(Instant.now());
    message.setStatus(ThreadStatus.DRAFT);
    message.setId("11df58f1-2e26-4103-74ca-afc90ac955dd");
    return message;
  }


  public ThreadMessage createThreadMessageByNum(String num) {
    ThreadMessage message = new ThreadMessage();
    message.setBody("Message Body of" + num);
    message.setTitle("Test " + num);
    message.setOwnershipMask("AAA");
    message.setAttributesMask("111");
    message.setAuthor("William Shakespeare");
    message.setCreatedOn(Instant.now());
    message.setStatus(ThreadStatus.DRAFT);
    message.setDbversion(0L);
    message.setId(num);
    return message;
  }


  public ThreadMessageDTO createThreadMessageDTOByNum(String num) {
    ThreadMessageDTO message = new ThreadMessageDTO();
    message.setBody("Message Body of" + num);
    message.setTitle("Test " + num);
    message.setOwnershipMask("AAA");
    message.setAttributesMask("111");
    message.setAuthor("William Shakespeare");
    message.setCreatedOn(Instant.now());
    message.setStatus(ThreadStatus.DRAFT);
    message.setId(num);
    return message;
  }


  public ThreadMessage createChildThreadMessage() {
    ThreadMessage childMessage = new ThreadMessage();

    childMessage.setId("66df58f1-2e26-4103-74ca-afc90ac955cc");
    childMessage.setBody(" message Body");
    childMessage.setTitle("Test");
    childMessage.setOwnershipMask("AAA");
    childMessage.setAttributesMask("111");
    childMessage.setAuthor("William Shakespeare");
    childMessage.setCreatedOn(Instant.now());
    childMessage.setDbversion(0L);
    childMessage.setStatus(ThreadStatus.DRAFT);
    childMessage.setParentThreadMessage(createThreadMessage());
    return childMessage;
  }

  public List<ThreadMessage> create10Messages() {
    List<ThreadMessage> messages = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      messages.add(createThreadMessageByNum(i + ""));
    }
    return messages;
  }


  public List<ThreadMessageDTO> create10DTOMessages() {
    List<ThreadMessageDTO> messages = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      messages.add(createThreadMessageDTOByNum(i + ""));
    }
    return messages;
  }

  public List<ThreadMessage> createThread() {
    createThreadMessageByNum("1000");
    return List.of(rootMessage);
  }


  public Vote createVote() {
    Vote vote = new Vote();
    vote.setCreatedOn(Instant.now());
    vote.setReaction(Reaction.LIKE);
    vote.setVoterId("1");
    vote.setThreadMessage(createThreadMessage());
    return vote;
  }

  public VoteDTO createVoteDTO() {
    VoteDTO vote = new VoteDTO();
    vote.setId("voteid");
    vote.setReaction(Reaction.LIKE);
    vote.setVoterId("1");
    vote.setThreadId("2");
    return vote;
  }

  public Vote createVoteByNum(String num) {
    Vote vote = new Vote();
    vote.setCreatedOn(Instant.now());
    vote.setReaction(Reaction.LIKE);
    vote.setVoterId("1");
    vote.setThreadMessage(createThreadMessage());
    return vote;
  }

  public VoteDTO createVoteDTOByNum(String num) {
    VoteDTO vote = new VoteDTO();
    vote.setReaction(Reaction.LIKE);
    vote.setVoterId(num);
    vote.setThreadId("Thread " + num);
    return vote;
  }


  public List<Vote> create10Votes() {
    List<Vote> votes = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      votes.add(createVoteByNum(i + ""));
    }
    return votes;
  }

  public List<VoteDTO> create10VoteDTO() {
    List<VoteDTO> dtos = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      dtos.add(createVoteDTOByNum(i + ""));
    }
    return dtos;
  }


  public Vote createVoteWithNullValues() {
    return new Vote();
  }


}

