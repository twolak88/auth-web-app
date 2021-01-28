package com.twolak.springframework.authwebapp.services.impl;

import com.twolak.springframework.authwebapp.config.Globals;
import com.twolak.springframework.authwebapp.domain.Role;
import com.twolak.springframework.authwebapp.domain.User;
import com.twolak.springframework.authwebapp.repository.UserRepository;
import com.twolak.springframework.authwebapp.services.RoleService;
import com.twolak.springframework.authwebapp.services.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author twolak
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private static final String USERNAME = "thomas";
    private static final String USENAME_2 = "rob";
    private static final String USERNAME_3 = "kurt";
    private static final long ID_1 = 1L;
    private static final long ID_2 = 2L;
    private static final long ID_3 = 3L;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    private UserService userService;

    @BeforeEach
    public void setUp() {
        this.userService = new UserServiceImpl(this.userRepository, this.roleService);
    }

    @Test
    public void testSaveUser() {
        User user = User.builder().id(ID_1).userName(USERNAME).build();
        User userToSave = User.builder().userName(USERNAME).build();

        Mockito.when(this.userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        User savedUser = this.userService.saveUser(userToSave);

        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(ID_1, savedUser.getId());
        Assertions.assertEquals(USERNAME, savedUser.getUserName());
        Mockito.verify(this.userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
        Mockito.verifyNoMoreInteractions(this.userRepository);
    }

    @Test
    public void testSaveRegisteredUser() {
        Role role = Role.builder().id(ID_1).role(Globals.Roles.ROLE_USER).build();
        User user = User.builder().id(ID_1).userName(USERNAME).roles(Stream.of(role).collect(Collectors.toCollection(HashSet::new))).build();
        User userToSave = User.builder().userName(USERNAME).roles(new HashSet()).build();

        Mockito.when(this.roleService.getOrCreateRole(ArgumentMatchers.anyString())).thenReturn(role);
        Mockito.when(this.userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        User savedUser = this.userService.saveRegisteredUser(userToSave);

        Assertions.assertNotNull(savedUser);
        Assertions.assertEquals(ID_1, savedUser.getId());
        Assertions.assertEquals(USERNAME, savedUser.getUserName());
        Assertions.assertNotNull(savedUser.getRoles());
        Assertions.assertEquals(1, savedUser.getRoles().size());
        Mockito.verify(this.userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
        Mockito.verify(this.roleService, Mockito.times(1)).getOrCreateRole(ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.userRepository);
    }

    @Test
    public void testFindUserById() {
        Long id = ID_1;
        User user = User.builder().id(ID_1).userName(USERNAME).build();

        Mockito.when(this.userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));

        User foundUser = this.userService.findUserById(id);

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(id, foundUser.getId());
        Assertions.assertEquals(USERNAME, foundUser.getUserName());
        Mockito.verify(this.userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyLong());
        Mockito.verifyNoMoreInteractions(this.userRepository);
    }

    @Test
    public void testFindUserByUserName() {
        String username = USERNAME;
        User user = User.builder().id(ID_1).userName(USERNAME).build();

        Mockito.when(this.userRepository.getUserByUserName(ArgumentMatchers.anyString())).thenReturn(user);

        User foundUser = this.userService.findUserByUserName(username);

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(ID_1, foundUser.getId());
        Assertions.assertEquals(USERNAME, foundUser.getUserName());
        Mockito.verify(this.userRepository, Mockito.times(1)).getUserByUserName(ArgumentMatchers.anyString());
        Mockito.verifyNoMoreInteractions(this.userRepository);
    }

    @Test
    public void testFindPaginated() {
        Pageable pageable = PageRequest.of(0, 3);
        Page<User> users = new PageImpl<>(List.of(User.builder().id(ID_1).userName(USERNAME).build(),
                User.builder().id(ID_2).userName(USENAME_2).build(),
                User.builder().id(ID_3).userName(USERNAME_3).build()), pageable, 3);

        Mockito.when(this.userRepository.findAll(ArgumentMatchers.any(Pageable.class))).thenReturn(users);

        Page<User> foundUsers = this.userService.findPaginated(pageable);

        Assertions.assertNotNull(foundUsers);
        Assertions.assertEquals(3, foundUsers.getNumberOfElements());
        Assertions.assertEquals(3, foundUsers.getSize());
        Assertions.assertEquals(1, foundUsers.getTotalPages());
        Assertions.assertEquals(3, foundUsers.getTotalElements());
        Mockito.verify(this.userRepository, Mockito.times(1)).findAll(ArgumentMatchers.any(Pageable.class));
        Mockito.verifyNoMoreInteractions(this.userRepository);
    }

    @Test
    public void testDeleteUser() {
        Long idToDelete = ID_2;

        this.userService.deleteUser(idToDelete);

        Mockito.verify(this.userRepository, Mockito.times(1)).deleteById(ArgumentMatchers.anyLong());
        Mockito.verifyNoMoreInteractions(this.userRepository);
    }

    @Test
    public void testGetAvailableRoles() {
        List<Role> roles = List.of(Role.builder().id(ID_1).role(Globals.Roles.ROLE_ADMIN).build(),
                Role.builder().id(ID_2).role(Globals.Roles.ROLE_PREMIUM).build());
        Set<Role> userRoles = Set.of(Role.builder().id(ID_1).role(Globals.Roles.ROLE_ADMIN).build());
        User user = User.builder().id(ID_1).userName(USERNAME).roles(userRoles).build();

        Mockito.when(this.roleService.findAll()).thenReturn(roles);

        List<Role> availableRoles = this.userService.getAvailableRoles(user);

        Assertions.assertNotNull(availableRoles);
        Assertions.assertEquals(1, availableRoles.size());
        Mockito.verify(this.roleService, Mockito.times(1)).findAll();
        Mockito.verifyNoMoreInteractions(this.roleService);
    }

    @Test
    public void testUpdateRoles() {
        Role premium = Role.builder().id(ID_2).role(Globals.Roles.ROLE_PREMIUM).build();
        Role admin = Role.builder().id(ID_1).role(Globals.Roles.ROLE_ADMIN).build();
        Role userRole = Role.builder().id(ID_3).role(Globals.Roles.ROLE_USER).build();
        Set<Role> roles = Set.of(admin, userRole);
        User user = User.builder().id(ID_1).userName(USERNAME).roles(Stream.of(premium).collect(Collectors.toCollection(HashSet::new))).build();
        User savedUser = User.builder().id(ID_1).userName(USERNAME).roles(Set.of(premium, admin, userRole)).build();

        Mockito.when(this.userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(this.roleService.findByIdIn(ArgumentMatchers.anySet())).thenReturn(roles);
        Mockito.when(this.userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(savedUser);

        User returnedUser = this.userService.updateRoles(ID_1, roles);

        Assertions.assertNotNull(returnedUser);
        Assertions.assertEquals(3, returnedUser.getRoles().size());
        Mockito.verify(this.userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyLong());
        Mockito.verify(this.roleService, Mockito.times(1)).findByIdIn(ArgumentMatchers.anySet());
        Mockito.verify(this.userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
        Mockito.verifyNoMoreInteractions(this.userRepository);
        Mockito.verifyNoMoreInteractions(this.roleService);
    }

    @Test
    public void testRemoveRole() {
        Role userRole = Role.builder().id(ID_2).role(Globals.Roles.ROLE_USER).build();
        Role promiumRole = Role.builder().id(ID_1).role(Globals.Roles.ROLE_PREMIUM).build();
        User user = User.builder().id(ID_1).userName(USERNAME).roles(Stream.of(userRole, promiumRole).collect(Collectors.toCollection(HashSet::new))).build();
        User savedUser = User.builder().id(ID_1).userName(USERNAME).roles(Stream.of(userRole).collect(Collectors.toCollection(HashSet::new))).build();

        Mockito.when(this.userRepository.findById(ArgumentMatchers.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(this.roleService.findById(ArgumentMatchers.anyLong())).thenReturn(promiumRole);
        Mockito.when(this.userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(savedUser);

        User returnedUser = this.userService.removeRole(ID_1, ID_1);

        Assertions.assertNotNull(returnedUser);
        Assertions.assertEquals(1, returnedUser.getRoles().size());

        Mockito.verify(this.userRepository, Mockito.times(1)).findById(ArgumentMatchers.anyLong());
        Mockito.verify(this.roleService, Mockito.times(1)).findById(ArgumentMatchers.anyLong());
        Mockito.verify(this.userRepository, Mockito.times(1)).save(ArgumentMatchers.any(User.class));
        Mockito.verifyNoMoreInteractions(this.userRepository);
        Mockito.verifyNoMoreInteractions(this.roleService);
    }

}
