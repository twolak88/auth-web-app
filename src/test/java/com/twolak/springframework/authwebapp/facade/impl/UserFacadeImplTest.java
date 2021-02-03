/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twolak.springframework.authwebapp.facade.impl;

import com.twolak.springframework.authwebapp.config.Globals;
import com.twolak.springframework.authwebapp.domain.Role;
import com.twolak.springframework.authwebapp.domain.User;
import com.twolak.springframework.authwebapp.facade.UserFacade;
import com.twolak.springframework.authwebapp.services.UserService;
import com.twolak.springframework.authwebapp.web.mappers.CycleAvoidingMappingContext;
import com.twolak.springframework.authwebapp.web.mappers.RoleMapper;
import com.twolak.springframework.authwebapp.web.mappers.UserMapper;
import com.twolak.springframework.authwebapp.web.model.RoleDto;
import com.twolak.springframework.authwebapp.web.model.UserDto;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author twolak
 */
@ExtendWith(MockitoExtension.class)
public class UserFacadeImplTest {
    
    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final long ID_3 = 3L;
    private static final String USERNAME = "thomas";
    private static final String PASSWORD = "1234";
    
    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private UserService userService;

    private UserFacade userFacade;
    
    @BeforeEach
    public void setUp() {
        this.userFacade = new UserFacadeImpl(userMapper, userService, roleMapper, new CycleAvoidingMappingContext());
    }

    @Test
    public void testSaveUser() {
        UserDto userDto = UserDto.builder().id(ID_1).userName(USERNAME).build();
        User convertedUser = User.builder().id(ID_1).userName(USERNAME).build();
        User savedUser = User.builder().id(ID_1).userName(USERNAME).build();
        UserDto savedUserDto = UserDto.builder().id(ID_1).userName(USERNAME).build();

        Mockito.when(this.userService.saveUser(ArgumentMatchers.any(User.class))).thenReturn(savedUser);
        Mockito.when(this.userMapper.userDtoToUser(ArgumentMatchers.any(UserDto.class), 
                ArgumentMatchers.any(CycleAvoidingMappingContext.class))).thenReturn(convertedUser);
        Mockito.when(this.userMapper.userToUserDto(ArgumentMatchers.any(User.class),
                ArgumentMatchers.any(CycleAvoidingMappingContext.class))).thenReturn(savedUserDto);

        UserDto returnedUserDto = this.userFacade.saveUser(userDto);

        Assertions.assertNotNull(returnedUserDto);
        Assertions.assertEquals(ID_1, returnedUserDto.getId());
        Assertions.assertEquals(userDto.getUserName(), returnedUserDto.getUserName());
        Mockito.verify(this.userService, Mockito.times(1)).saveUser(ArgumentMatchers.any(User.class));
        Mockito.verify(this.userMapper, Mockito.times(1)).userDtoToUser(ArgumentMatchers.any(UserDto.class),
                ArgumentMatchers.any(CycleAvoidingMappingContext.class));
        Mockito.verify(this.userMapper, Mockito.times(1)).userToUserDto(ArgumentMatchers.any(User.class),
                ArgumentMatchers.any(CycleAvoidingMappingContext.class));
        Mockito.verifyNoMoreInteractions(this.userService);
        Mockito.verifyNoMoreInteractions(this.userMapper);
    }

    @Test
    public void testRegisterUser() {
        String password = new BCryptPasswordEncoder().encode(PASSWORD);
        UserDto userDto = UserDto.builder().id(ID_1).userName(USERNAME).userPassword(PASSWORD).build();
        User convertedUser = User.builder().id(ID_1).userName(USERNAME).userPassword(password).isActive(Boolean.FALSE).build();
        User savedUser = User.builder().id(ID_1).userName(USERNAME).userPassword(password).isActive(Boolean.FALSE).build();
        UserDto sonvertedUserDto = UserDto.builder().id(ID_1).userName(USERNAME).userPassword(password).isActive(Boolean.FALSE).build();

        Mockito.when(this.userService.saveRegisteredUser(ArgumentMatchers.any(User.class))).thenReturn(savedUser);
        Mockito.when(this.userMapper.userDtoToUser(ArgumentMatchers.any(UserDto.class),
                ArgumentMatchers.any(CycleAvoidingMappingContext.class))).thenReturn(convertedUser);
        Mockito.when(this.userMapper.userToUserDto(ArgumentMatchers.any(User.class),
                ArgumentMatchers.any(CycleAvoidingMappingContext.class))).thenReturn(sonvertedUserDto);

        UserDto savedUserDto = this.userFacade.registerUser(userDto);

        Assertions.assertNotNull(savedUserDto);
        Assertions.assertEquals(ID_1, savedUserDto.getId());
        Assertions.assertEquals(userDto.getUserName(), savedUserDto.getUserName());
        Assertions.assertEquals(password, savedUserDto.getUserPassword());
        Assertions.assertEquals(Boolean.FALSE, savedUserDto.getIsActive());
        Mockito.verify(this.userService, Mockito.times(1)).saveRegisteredUser(ArgumentMatchers.any(User.class));
        Mockito.verify(this.userMapper, Mockito.times(1)).userDtoToUser(ArgumentMatchers.any(UserDto.class),
                ArgumentMatchers.any(CycleAvoidingMappingContext.class));
        Mockito.verify(this.userMapper, Mockito.times(1)).userToUserDto(ArgumentMatchers.any(User.class),
                ArgumentMatchers.any(CycleAvoidingMappingContext.class));
        Mockito.verifyNoMoreInteractions(this.userService);
        Mockito.verifyNoMoreInteractions(this.userMapper);
    }

    @Test
    public void testFindUserById() {
        User user = User.builder().id(ID_1).userName(USERNAME).build();
        UserDto sonvertedUserDto = UserDto.builder().id(ID_1).userName(USERNAME).build();

        Mockito.when(this.userService.findUserById(ArgumentMatchers.anyLong())).thenReturn(user);
        Mockito.when(this.userMapper.userToUserDto(ArgumentMatchers.any(User.class),
                ArgumentMatchers.any(CycleAvoidingMappingContext.class))).thenReturn(sonvertedUserDto);

        UserDto userDto = this.userFacade.findUserById(ID_1);

        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(ID_1, userDto.getId());
        Assertions.assertEquals(USERNAME, userDto.getUserName());
        Mockito.verify(this.userService, Mockito.times(1)).findUserById(ArgumentMatchers.anyLong());
        Mockito.verify(this.userMapper, Mockito.times(1)).userToUserDto(ArgumentMatchers.any(User.class),
                ArgumentMatchers.any(CycleAvoidingMappingContext.class));
        Mockito.verifyNoMoreInteractions(this.userService);
        Mockito.verifyNoMoreInteractions(this.userMapper);
    }

    @Test
    public void testGetEmptyUser() {
    }

    @Test
    public void testDeleteUser() {
        this.userFacade.deleteUser(ID_1);

        Mockito.verify(this.userService, Mockito.times(1)).deleteUser(ArgumentMatchers.anyLong());
        Mockito.verifyNoMoreInteractions(this.userService);
    }

    @Test
    public void testGetAvailableRoles() {
        Role adminRole = Role.builder().id(ID_1).role(Globals.Roles.ROLE_ADMIN).build();
        Role premiumRole = Role.builder().id(ID_2).role(Globals.Roles.ROLE_PREMIUM).build();
        RoleDto adminRoleDto = RoleDto.builder().id(ID_1).role(Globals.Roles.ROLE_ADMIN).build();
        RoleDto premiumRoleDto = RoleDto.builder().id(ID_2).role(Globals.Roles.ROLE_PREMIUM).build();
        List<Role> roles = List.of(adminRole, premiumRole);
        UserDto userDto = UserDto.builder().id(ID_1).userName(USERNAME).build();
        User convertedUser = User.builder().id(ID_1).userName(USERNAME).build();

        ArgumentMatcher<Role> adminRoleMatcher = role -> role != null && Globals.Roles.ROLE_ADMIN.equals(role.getRole());
        ArgumentMatcher<Role> premiumRoleMatcher = role -> role != null && Globals.Roles.ROLE_PREMIUM.equals(role.getRole());
        Mockito.when(this.userMapper.userDtoToUser(ArgumentMatchers.any(UserDto.class),
                ArgumentMatchers.any(CycleAvoidingMappingContext.class))).thenReturn(convertedUser);
        Mockito.when(this.userService.getAvailableRoles(ArgumentMatchers.any(User.class))).thenReturn(roles);
        Mockito.when(this.roleMapper.roleToRoleDto(ArgumentMatchers.argThat(adminRoleMatcher))).thenReturn(adminRoleDto);
        Mockito.when(this.roleMapper.roleToRoleDto(ArgumentMatchers.argThat(premiumRoleMatcher))).thenReturn(premiumRoleDto);

        List<RoleDto> foundRoleDtos = this.userFacade.getAvailableRoles(userDto);

        Assertions.assertNotNull(foundRoleDtos);
        Assertions.assertEquals(2, foundRoleDtos.size());
        Mockito.verify(this.userService, Mockito.times(1)).getAvailableRoles(ArgumentMatchers.any(User.class));
        Mockito.verify(this.userMapper, Mockito.times(1)).userDtoToUser(ArgumentMatchers.any(UserDto.class), ArgumentMatchers.any(CycleAvoidingMappingContext.class));
        Mockito.verify(this.roleMapper, Mockito.times(2)).roleToRoleDto(ArgumentMatchers.any(Role.class));
        Mockito.verifyNoMoreInteractions(this.userService);
        Mockito.verifyNoMoreInteractions(this.userMapper);
        Mockito.verifyNoMoreInteractions(this.roleMapper);
    }

    @Test
    public void testUpdateRoles() {
        Role adminRole = Role.builder().id(ID_1).role(Globals.Roles.ROLE_ADMIN).build();
        Role premiumRole = Role.builder().id(ID_2).role(Globals.Roles.ROLE_PREMIUM).build();
        RoleDto adminRoleDto = RoleDto.builder().id(ID_1).role(Globals.Roles.ROLE_ADMIN).build();
        RoleDto premiumRoleDto = RoleDto.builder().id(ID_2).role(Globals.Roles.ROLE_PREMIUM).build();
        Set<Role> roles = Set.of(adminRole, premiumRole);
        Set<RoleDto> roleDtos = Set.of(adminRoleDto, premiumRoleDto);
        User user = User.builder().id(ID_1).userName(USERNAME).roles(roles).build();
        UserDto convertedUser = UserDto.builder().id(ID_1).userName(USERNAME).roles(roleDtos).build();

        ArgumentMatcher<RoleDto> adminRoleDtoMatcher = role -> role != null && Globals.Roles.ROLE_ADMIN.equals(role.getRole());
        ArgumentMatcher<RoleDto> premiumRoleDtoMatcher = role -> role != null && Globals.Roles.ROLE_PREMIUM.equals(role.getRole());
        Mockito.when(this.roleMapper.roleDtoToRole(ArgumentMatchers.argThat(adminRoleDtoMatcher))).thenReturn(adminRole);
        Mockito.when(this.roleMapper.roleDtoToRole(ArgumentMatchers.argThat(premiumRoleDtoMatcher))).thenReturn(premiumRole);
        Mockito.when(this.userService.updateRoles(ArgumentMatchers.anyLong(), ArgumentMatchers.anySet())).thenReturn(user);
        Mockito.when(this.userMapper.userToUserDto(ArgumentMatchers.any(User.class), 
                ArgumentMatchers.any(CycleAvoidingMappingContext.class))).thenReturn(convertedUser);

        UserDto userDto = this.userFacade.updateRoles(ID_1, roleDtos);

        Assertions.assertNotNull(userDto);
        Assertions.assertNotNull(userDto.getRoles());
        Assertions.assertEquals(2, userDto.getRoles().size());
        Mockito.verify(this.userService, Mockito.times(1)).updateRoles(ArgumentMatchers.anyLong(), ArgumentMatchers.anySet());
        Mockito.verify(this.userMapper, Mockito.times(1)).userToUserDto(ArgumentMatchers.any(User.class), ArgumentMatchers.any(CycleAvoidingMappingContext.class));
        Mockito.verify(this.roleMapper, Mockito.times(2)).roleDtoToRole(ArgumentMatchers.any(RoleDto.class));
        Mockito.verifyNoMoreInteractions(this.userService);
        Mockito.verifyNoMoreInteractions(this.userMapper);
        Mockito.verifyNoMoreInteractions(this.roleMapper);
    }

    @Test
    public void testRemoveRole() {
        User user = User.builder().id(ID_1).userName(USERNAME).roles(Set.of()).build();
        UserDto convertedUser = UserDto.builder().id(ID_1).userName(USERNAME).roles(Set.of()).build();

        Mockito.when(this.userService.removeRole(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong())).thenReturn(user);
        Mockito.when(this.userMapper.userToUserDto(ArgumentMatchers.any(User.class), 
                ArgumentMatchers.any(CycleAvoidingMappingContext.class))).thenReturn(convertedUser);

        UserDto userDto = this.userFacade.removeRole(ID_1, ID_1);

        Assertions.assertNotNull(userDto);
        Assertions.assertNotNull(userDto.getRoles());
        Assertions.assertEquals(0, userDto.getRoles().size());
        Mockito.verify(this.userService, Mockito.times(1)).removeRole(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());
        Mockito.verify(this.userMapper, Mockito.times(1)).userToUserDto(ArgumentMatchers.any(User.class),
                ArgumentMatchers.any(CycleAvoidingMappingContext.class));
        Mockito.verifyNoMoreInteractions(this.userService);
        Mockito.verifyNoMoreInteractions(this.userMapper);
    }

    @Test
    public void testFindPaginated() {
        User user1 = User.builder().id(ID_1).userName(USERNAME).build();
        User user2 = User.builder().id(ID_2).userName("rob").build();
        User user3 = User.builder().id(ID_3).userName("kurt").build();
        UserDto userDto1 = UserDto.builder().id(ID_1).userName(USERNAME).build();
        UserDto userDto2 = UserDto.builder().id(ID_2).userName("rob").build();
        UserDto userDto3 = UserDto.builder().id(ID_3).userName("kurt").build();
        Pageable pageable = PageRequest.of(0, 3);
        Page<User> users = new PageImpl<>(List.of(user1 ,user2, user3), pageable, 3);

        ArgumentMatcher<User> user1Matcher = user -> user != null && user.getId().equals(ID_1);
        ArgumentMatcher<User> user2Matcher = user -> user != null && user.getId().equals(ID_2);
        ArgumentMatcher<User> user3Matcher = user -> user != null && user.getId().equals(ID_3);
        Mockito.when(this.userService.findPaginated(ArgumentMatchers.any())).thenReturn(users);
        Mockito.when(this.userMapper.userToUserDto(ArgumentMatchers.argThat(user1Matcher), 
                ArgumentMatchers.any(CycleAvoidingMappingContext.class))).thenReturn(userDto1);
        Mockito.when(this.userMapper.userToUserDto(ArgumentMatchers.argThat(user2Matcher),
                ArgumentMatchers.any(CycleAvoidingMappingContext.class))).thenReturn(userDto2);
        Mockito.when(this.userMapper.userToUserDto(ArgumentMatchers.argThat(user3Matcher),
                ArgumentMatchers.any(CycleAvoidingMappingContext.class))).thenReturn(userDto3);

        Page<UserDto> userDtos = this.userFacade.findPaginated(pageable);

        Assertions.assertNotNull(userDtos);
        Assertions.assertEquals(3, userDtos.getNumberOfElements());
        Assertions.assertEquals(3, userDtos.getSize());
        Assertions.assertEquals(1, userDtos.getTotalPages());
        Assertions.assertEquals(3, userDtos.getTotalElements());
        Mockito.verify(this.userService, Mockito.times(1)).findPaginated(ArgumentMatchers.any(Pageable.class));
        Mockito.verifyNoMoreInteractions(this.userService);
    }
    
}
