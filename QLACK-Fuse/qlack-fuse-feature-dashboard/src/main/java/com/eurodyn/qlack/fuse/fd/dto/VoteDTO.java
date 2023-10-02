package com.eurodyn.qlack.fuse.fd.dto;

import com.eurodyn.qlack.fuse.fd.util.Reaction;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VoteDTO implements Serializable {

  /**
   * The Vote id
   */
  private String id;

  /**
   * The id of the person who voted.
   */
  private String voterId;

  /**
   * The related ThreadMessage id
   */
  private String threadId;

  /**
   * The reaction
   */
  private Reaction reaction;


  /**
   * To String method to print the textual representation of the Vote
   *
   * @return Text format of Vote.
   */
  @Override
  public String toString() {
    return "VoteDTO{" +
        "id='" + id + '\'' +
        "voterId='" + voterId + '\'' +
        ", threadId='" + threadId + '\'' +
        ", reaction=" + reaction +
        '}';
  }

  /**
   * Used for comparison.
   * @param o comparing vote.
   * @return true if the objects are equal.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    VoteDTO voteDTO = (VoteDTO) o;

    if (getId() != null ? !getId().equals(voteDTO.getId()) : voteDTO.getId() != null) {
      return false;
    }
    if (getVoterId() != null ? !getVoterId().equals(voteDTO.getVoterId())
        : voteDTO.getVoterId() != null) {
      return false;
    }
    if (getThreadId() != null ? !getThreadId().equals(voteDTO.getThreadId())
        : voteDTO.getThreadId() != null) {
      return false;
    }
    return getReaction() == voteDTO.getReaction();
  }

  /**
   * Used for comparison.
   * @return hashCode Result
   */
  @Override
  public int hashCode() {
    int result = getId() != null ? getId().hashCode() : 0;
    result = 31 * result + (getVoterId() != null ? getVoterId().hashCode() : 0);
    result = 31 * result + (getThreadId() != null ? getThreadId().hashCode() : 0);
    result = 31 * result + (getReaction() != null ? getReaction().hashCode() : 0);
    return result;
  }

}

