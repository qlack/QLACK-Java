package com.eurodyn.qlack.fuse.aaa.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.eurodyn.qlack.fuse.aaa.mapper.UserGroupMapper;
import com.eurodyn.qlack.fuse.aaa.repository.UserGroupRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JSONConfigServiceTest {

  @Mock
  private UserGroupService userGroupService;
  @Mock
  private OpTemplateService templateService;
  @Mock
  private OperationService operationService;
  @Mock
  private UserGroupRepository userGroupRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private UserGroupMapper userGroupMapper;

  private JSONConfigService jsonConfigService;
  final private String configFile = "samples/qlack-aaa-config.json";

  @BeforeEach
  public void init() {
    jsonConfigService = new JSONConfigService(userGroupService, templateService,
      operationService);
    userGroupService = new UserGroupService(userGroupRepository, userRepository,
      userGroupMapper);
  }

  @Test
  public void testInitWithFile(){
    jsonConfigService.initWithFile(configFile);
    verify(templateService, times(2)).getTemplateByName(any());
    verify(operationService, times(2)).getOperationByName(any());
  }
}
