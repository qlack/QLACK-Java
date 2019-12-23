package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.mapper.UserDetailsMapper;
import com.eurodyn.qlack.fuse.aaa.mapper.UserGroupHasOperationMapper;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

/**
 * A Service class for Authenticate.
 *
 * @author European Dynamics SA
 */
@Log
@Primary
@Service
@Validated
@Transactional
public class AuthenticateService implements UserDetailsService {

  private final UserRepository userRepository;

  private final UserDetailsMapper userDetailsMapper;

  private final UserGroupHasOperationMapper userGroupHasOperationMapper;

  @Autowired
  public AuthenticateService(UserRepository userRepository,
    UserDetailsMapper userDetailsMapper,
    UserGroupHasOperationMapper userGroupHasOperationMapper) {
    this.userRepository = userRepository;
    this.userDetailsMapper = userDetailsMapper;
    this.userGroupHasOperationMapper = userGroupHasOperationMapper;
  }

  @Override
  public UserDetails loadUserByUsername(String username) {
    User user = userRepository.findByUsername(username);
    return userDetailsMapper.mapToDTO(user, userGroupHasOperationMapper);
  }
}
