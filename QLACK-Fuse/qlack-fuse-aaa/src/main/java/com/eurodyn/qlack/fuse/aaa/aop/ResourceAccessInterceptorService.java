package com.eurodyn.qlack.fuse.aaa.aop;

import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * This service method contains methods related to the ResourceAccessInterceptor checks.
 *
 * @author European Dynamics SA
 */
@Service
@RequiredArgsConstructor
class ResourceAccessInterceptorService {

  private final UserRepository userRepository;

  /**
   * This methods retrieves the information of the user based on his/hers principal and caches the result.
   *
   * @param id the user id
   * @return the user information object
   */
  public User findUser(String id) {
    return userRepository.fetchById(id);
  }
}
