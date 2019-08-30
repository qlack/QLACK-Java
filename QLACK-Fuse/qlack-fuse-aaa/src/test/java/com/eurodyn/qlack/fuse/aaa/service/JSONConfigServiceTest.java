package com.eurodyn.qlack.fuse.aaa.service;

import com.eurodyn.qlack.fuse.aaa.mappers.UserGroupMapper;
import com.eurodyn.qlack.fuse.aaa.repository.UserGroupRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import sun.misc.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JSONConfigServiceTest {

  @Mock private UserGroupService userGroupService;
  @Mock private OpTemplateService templateService;
  @Mock private OperationService operationService;
  @Mock private UserGroupRepository userGroupRepository;
  @Mock private UserRepository userRepository;
  @Mock private UserGroupMapper userGroupMapper;

  private JSONConfigService jsonConfigService;
  private String configFile = "samples/qlack-aaa-config.json";

  @Before
  public void init(){
    jsonConfigService = new JSONConfigService(userGroupService, templateService, operationService);
    userGroupService = new UserGroupService(userGroupRepository, userRepository, userGroupMapper);
  }

  @Test
  public void testInitWithFile() throws IOException, URISyntaxException {
    jsonConfigService.initWithFile(configFile);
    verify(templateService, times(2)).getTemplateByName(any());
    verify(operationService, times(2)).getOperationByName(any());
  }
}
