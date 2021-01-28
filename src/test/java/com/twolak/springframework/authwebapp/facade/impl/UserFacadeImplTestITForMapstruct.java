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
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
@SpringBootTest
public class UserFacadeImplTestITForMapstruct {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
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
        UserDto userDto = UserDto.builder().id(1L).userName("thomas").build();
        User savedUser = User.builder().id(1L).userName("thomas").build();
        
        Mockito.when(this.userService.saveUser(ArgumentMatchers.any(User.class))).thenReturn(savedUser);
        
        UserDto savedUserDto = this.userFacade.saveUser(userDto);
        
        Assertions.assertNotNull(savedUserDto);
        Assertions.assertEquals(1L, savedUserDto.getId());
        Assertions.assertEquals(userDto.getUserName(), savedUserDto.getUserName());
        Mockito.verify(this.userService, Mockito.times(1)).saveUser(ArgumentMatchers.any(User.class));
        Mockito.verifyNoMoreInteractions(this.userService);
    }

    @Test
    public void testRegisterUser() {
        String password = new BCryptPasswordEncoder().encode("1234");
        UserDto userDto = UserDto.builder().id(1L).userName("thomas").userPassword("1234").build();
        User savedUser = User.builder().id(1L).userName("thomas").userPassword(password).isActive(Boolean.FALSE).build();

        Mockito.when(this.userService.saveUser(ArgumentMatchers.any(User.class))).thenReturn(savedUser);

        UserDto savedUserDto = this.userFacade.saveUser(userDto);

        Assertions.assertNotNull(savedUserDto);
        Assertions.assertEquals(1L, savedUserDto.getId());
        Assertions.assertEquals(userDto.getUserName(), savedUserDto.getUserName());
        Assertions.assertEquals(password, savedUserDto.getUserPassword());
        Assertions.assertEquals(Boolean.FALSE, savedUserDto.getIsActive());
        Mockito.verify(this.userService, Mockito.times(1)).saveUser(ArgumentMatchers.any(User.class));
        Mockito.verifyNoMoreInteractions(this.userService);
    }

    @Test
    public void testFindUserById() {
        User user = User.builder().id(1L).userName("thomas").build();
        
        Mockito.when(this.userService.findUserById(ArgumentMatchers.anyLong())).thenReturn(user);
        
        UserDto userDto = this.userFacade.findUserById(1L);
        
        Assertions.assertNotNull(userDto);
        Assertions.assertEquals(1L, userDto.getId());
        Assertions.assertEquals("thomas", userDto.getUserName());
        Mockito.verify(this.userService, Mockito.times(1)).findUserById(ArgumentMatchers.anyLong());
        Mockito.verifyNoMoreInteractions(this.userService);
    }

    @Test
    public void testGetEmptyUser() {
    }

    @Test
    public void testDeleteUser() {
        this.userFacade.deleteUser(1L);
        
        Mockito.verify(this.userService, Mockito.times(1)).deleteUser(ArgumentMatchers.anyLong());
        Mockito.verifyNoMoreInteractions(this.userService);
    }

    @Test
    public void testGetAvailableRoles() {
        List<Role> roles  = List.of(Role.builder().id(1L).role(Globals.Roles.ROLE_ADMIN).build(),
                Role.builder().id(2L).role(Globals.Roles.ROLE_PREMIUM).build());
        UserDto userDto = UserDto.builder().id(1L).userName("thomas").build();
        
        Mockito.when(this.userService.getAvailableRoles(ArgumentMatchers.any(User.class))).thenReturn(roles);
        
        List<RoleDto> foundRoleDtos = this.userFacade.getAvailableRoles(userDto);
        
        Assertions.assertNotNull(foundRoleDtos);
        Assertions.assertEquals(2, foundRoleDtos.size());
        Mockito.verify(this.userService, Mockito.times(1)).getAvailableRoles(ArgumentMatchers.any(User.class));
        Mockito.verifyNoMoreInteractions(this.userService);
    }

    @Test
    public void testUpdateRoles() {
        Set<Role> roles = Set.of(Role.builder().id(1L).role(Globals.Roles.ROLE_ADMIN).build(),
                Role.builder().id(2L).role(Globals.Roles.ROLE_PREMIUM).build());
        Set<RoleDto> roleDtos = Set.of(RoleDto.builder().id(1L).role(Globals.Roles.ROLE_ADMIN).build(),
                RoleDto.builder().id(2L).role(Globals.Roles.ROLE_PREMIUM).build());
        User user = User.builder().id(1L).userName("thomas").roles(roles).build();
        
        Mockito.when(this.userService.updateRoles(ArgumentMatchers.anyLong(), ArgumentMatchers.anySet())).thenReturn(user);
        
        UserDto userDto = this.userFacade.updateRoles(1L, roleDtos);
        
        Assertions.assertNotNull(userDto);
        Assertions.assertNotNull(userDto.getRoles());
        Assertions.assertEquals(2, userDto.getRoles().size());
        Mockito.verify(this.userService, Mockito.times(1)).updateRoles(ArgumentMatchers.anyLong(), ArgumentMatchers.anySet());
        Mockito.verifyNoMoreInteractions(this.userService);
    }

    @Test
    public void testRemoveRole() {
        User user = User.builder().id(1L).userName("thomas").roles(Set.of()).build();
        
        Mockito.when(this.userService.removeRole(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong())).thenReturn(user);
        
        UserDto userDto = this.userFacade.removeRole(1L, 1L);
        
        Assertions.assertNotNull(userDto);
        Assertions.assertNotNull(userDto.getRoles());
        Assertions.assertEquals(0, userDto.getRoles().size());
        Mockito.verify(this.userService, Mockito.times(1)).removeRole(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());
        Mockito.verifyNoMoreInteractions(this.userService);
    }

    @Test
    public void testFindPaginated() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<User> users = new PageImpl<>(List.of(User.builder().id(1L).userName("thomas").build(),
                User.builder().id(2L).userName("rob").build(),
                User.builder().id(3L).userName("kurt").build()), pageable, 3);
        
        Mockito.when(this.userService.findPaginated(ArgumentMatchers.any())).thenReturn(users);
        
        
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
