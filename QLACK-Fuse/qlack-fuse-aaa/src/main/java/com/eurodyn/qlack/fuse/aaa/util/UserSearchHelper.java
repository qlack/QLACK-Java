package com.eurodyn.qlack.fuse.aaa.util;

import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;

/**
 * @author European Dynamics S.A.
 */
public class UserSearchHelper {

  private UserDTO userDTO;


//  public UserSearchHelper(User userEntity, Object sortCriterion) {
//    //sortCriterion is ignored since it is only included in the query
//    //for compatibility with certain DBs.
//    userDTO = ConverterUtil.userToUserDTO(userEntity);
//  }


  public UserDTO getUserDTO() {
    return userDTO;
  }
}
