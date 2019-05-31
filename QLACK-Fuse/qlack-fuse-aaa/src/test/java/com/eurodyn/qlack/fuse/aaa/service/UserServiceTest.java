package com.eurodyn.qlack.fuse.aaa.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eurodyn.qlack.fuse.aaa.InitTestValues;
import com.eurodyn.qlack.fuse.aaa.dto.SessionDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserAttributeDTO;
import com.eurodyn.qlack.fuse.aaa.dto.UserDTO;
import com.eurodyn.qlack.fuse.aaa.mappers.SessionMapper;
import com.eurodyn.qlack.fuse.aaa.mappers.UserAttributeMapper;
import com.eurodyn.qlack.fuse.aaa.mappers.UserMapper;
import com.eurodyn.qlack.fuse.aaa.model.QSession;
import com.eurodyn.qlack.fuse.aaa.model.QUser;
import com.eurodyn.qlack.fuse.aaa.model.Session;
import com.eurodyn.qlack.fuse.aaa.model.User;
import com.eurodyn.qlack.fuse.aaa.model.UserAttribute;
import com.eurodyn.qlack.fuse.aaa.repository.SessionRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserAttributeRepository;
import com.eurodyn.qlack.fuse.aaa.repository.UserRepository;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    private AccountingService accountingService = mock(AccountingService.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private UserAttributeRepository userAttributeRepository = mock(UserAttributeRepository.class);

    private SessionRepository sessionRepository = mock(SessionRepository.class);

    private PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    @Spy
    private SessionMapper sessionMapper;

    @Spy
    private UserMapper userMapper;

    @Spy
    private UserAttributeMapper userAttributeMapper;

    private LdapUserUtil ldapUserUtil;

    private InitTestValues initTestValues;

    private QUser qUser;

    private QSession qSession;

    private User user;

    private UserDTO userDTO;

    private List<User> users;

    private List<UserDTO> usersDTO;

    @Before
    public void init(){
        initTestValues = new InitTestValues();
        userService = new UserService(accountingService, ldapUserUtil,
                userRepository, userAttributeRepository,
                sessionRepository, null, userMapper,
                sessionMapper, userAttributeMapper, null, passwordEncoder, null);
        qUser = new QUser("user");
        qSession = new QSession(("session"));
        user = initTestValues.createUser();
        userDTO = initTestValues.createUserDTO();
        users = initTestValues.createUsers();
        usersDTO = initTestValues.createUsersDTO();
    }

    @Test
    public void testCreateUserWithoutUserAttributes(){
        UserDTO userDTO = initTestValues.createUserDTO();
        userDTO.setUserAttributes(new HashSet<>());
        User user = initTestValues.createUser();
        user.setUserAttributes(new ArrayList<>());
        when(userMapper.mapToEntity(userDTO)).thenReturn(user);

        String userId = userService.createUser(userDTO);
        assertEquals(user.getId(), userId);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testCreateUser(){
        when(userMapper.mapToEntity(userDTO)).thenReturn(user);

        String userId = userService.createUser(userDTO);
        assertEquals(user.getId(), userId);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testUpdateUserWithoutUserAttibutes(){
        UserDTO userDTO = initTestValues.createUserDTO();
        userDTO.setUserAttributes(new HashSet<>());
        User user = initTestValues.createUser();
        user.setUserAttributes(new ArrayList<>());

        when(userRepository.fetchById(userDTO.getId())).thenReturn(user);
        userService.updateUser(userDTO, true, false);

        verify(userMapper, times(1)).mapToExistingEntity(userDTO, user);
        verify(userAttributeRepository, never()).findByUserIdAndName(any(), any());
    }

    private void testUpdateUserWithUserAttributes(boolean createIfMissing){
        UserDTO userDTO = initTestValues.createUserDTO();

        userDTO.setUsername("updated username");

        int index = 0;
        for (Iterator<UserAttributeDTO> iter = userDTO.getUserAttributes().iterator(); iter.hasNext();) {
            UserAttributeDTO u = iter.next();
            u.setData("updated " + u.getData());

            when(userAttributeRepository.findByUserIdAndName(user.getId(), u.getName())).thenReturn(user.getUserAttributes().get(index));
            index++;
        }

        UserAttributeDTO userAttributeDTO = new UserAttributeDTO();
        userAttributeDTO.setId("b69381e6-a465-4137-8c82-6f54f07b0a7f");
        userAttributeDTO.setName("email");
        userAttributeDTO.setData("user@qlack.eurodyn.com");
        userAttributeDTO.setContentType("text");
        userDTO.getUserAttributes().add(userAttributeDTO);

        when(userRepository.fetchById(userDTO.getId())).thenReturn(user);
        userService.updateUser(userDTO, true, createIfMissing);

        verify(userMapper, times(1)).mapToExistingEntity(userDTO, user);
    }

    @Test
    public void testUpdateUserWithoutNewUserAttributes(){
        testUpdateUserWithUserAttributes(false);
        verify(userAttributeRepository, times(2)).save(any());
    }

    @Test
    public void testUpdateUserWithNewUserAttributes(){
        testUpdateUserWithUserAttributes(true);
        verify(userAttributeRepository, times(3)).save(any());
    }

    @Test
    public void testDeleteUser(){
        User user2 = initTestValues.createUser();

        when(userRepository.fetchById(user.getId())).thenReturn(user2);
        userService.deleteUser(user.getId());

        verify(userRepository, times(1)).delete(user2);
    }

    @Test
    public void testGetUserById(){
        when(userRepository.fetchById(userDTO.getId())).thenReturn(user);
        when(userMapper.mapToDTO(user)).thenReturn(userDTO);
        UserDTO foundUser = userService.getUserById(userDTO.getId());
        assertEquals(userDTO, foundUser);
    }

    public Collection<String> getUsersById(){
        Collection<String> userIds = new ArrayList<>();
        for(int i=0; i<users.size(); i++){
            userIds.add(users.get(i).getId());
            when(userMapper.mapToDTO(users.get(i))).thenReturn(usersDTO.get(i));
        }

        when(userRepository.findAll(qUser.id.in(userIds))).thenReturn(users);

        return userIds;
    }

    @Test
    public void testGetUsersById(){
        Collection<String> userIds = getUsersById();
        Set<UserDTO> foundUsers = userService.getUsersById(userIds);

        assertEquals(new HashSet<>(usersDTO), foundUsers);
    }

    @Test
    public void testGetUsersByIdAsHash(){
        Collection<String> userIds = getUsersById();
        Map<String, UserDTO> foundUsers = userService.getUsersByIdAsHash(userIds);
        Map<String, UserDTO> userHashMap = new HashMap<>();
        for (UserDTO u: usersDTO){
            userHashMap.put(u.getId(), u);
        }

        assertEquals(userHashMap, foundUsers);
    }

    @Test
    public void testGetUserByName(){
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(userMapper.mapToDTO(user)).thenReturn(userDTO);

        UserDTO foundUser = userService.getUserByName(user.getUsername());
        assertEquals(foundUser, userDTO);
    }

    @Test
    public void testUpdateUserStatus(){
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        userService.updateUserStatus(user.getId(), (byte)0);

        verify(userRepository, times(1)).fetchById(user.getId());
    }

    @Test
    public void testGetUserStatus(){
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        userService.getUserStatus(user.getId());

        verify(userRepository, times(1)).fetchById(user.getId());
    }

    @Test
    public void testIsSuperAdmin(){
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        boolean isSuperAdmin = userService.isSuperadmin(user.getId());

        assertEquals(isSuperAdmin, user.isSuperadmin());
    }

    @Test
    public void testIsExternal(){
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        boolean isExternal = userService.isExternal(user.getId());

        assertEquals(isExternal, user.isExternal());
    }

    @Test
    public void testLogin(){
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        when(userMapper.mapToDTO(user)).thenReturn(userDTO);

        UserDTO loggedUser = userService.login(user.getId(), UUID.randomUUID().toString(), false);
        assertEquals(loggedUser, userDTO);
        verify(accountingService, never()).terminateSession(any());
    }

    @Test
    public void testLoginAndTerminateSessions(){
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        when(userMapper.mapToDTO(user)).thenReturn(userDTO);

        UserDTO loggedUser = userService.login(user.getId(), UUID.randomUUID().toString(), true);
        assertEquals(loggedUser, userDTO);
        verify(accountingService, times(2)).terminateSession(any());
    }

    @Test
    public void testLogout(){
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        for (Session s: user.getSessions()){
            userService.logout(user.getId(), s.getApplicationSessionId());
            verify(accountingService, times(1)).terminateSession(s.getId());
        }
    }

    @Test
    public void testLogoutAll(){
        when(sessionRepository.findAll(qSession.terminatedOn.isNull())).thenReturn(user.getSessions());
        when(userRepository.fetchById(user.getId())).thenReturn(user);
        userService.logoutAll();
        verify(accountingService, times(2)).terminateSession(any());
    }

    @Test
    public void testIsUserAlreadyLoggedIn(){
        List<SessionDTO> sessionsDTO = initTestValues.createSessionsDTO(user.getId());
        when(sessionRepository.findAll(qSession.user.id.eq(user.getId()).and(qSession.terminatedOn.isNull()),
                Sort.by("createdOn").ascending())).thenReturn(user.getSessions());
        when(sessionMapper.mapToDTO(user.getSessions())).thenReturn(sessionsDTO);

        List<SessionDTO> foundSessionsDTO = userService.isUserAlreadyLoggedIn(user.getId());
        assertEquals(foundSessionsDTO, sessionsDTO);
    }
}
