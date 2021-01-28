/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twolak.springframework.authwebapp.web.controllers;

import com.twolak.springframework.authwebapp.config.Globals;
import com.twolak.springframework.authwebapp.facade.UserFacade;
import com.twolak.springframework.authwebapp.web.model.RoleDto;
import com.twolak.springframework.authwebapp.web.model.UserDto;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 *
 * @author twolak
 */
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    
    private final String ADD_ROLE_VIEW = "/users/role/add";
    private final String USER_PROF_VIEW = "/users/profile";
    private final String USERS_VIEW = "/users/users";

    private final String REDIRECT_PROFILE = "redirect:/users/profile/";
    private final String REDIRECT_USERS = "redirect:/users";
    
    @Mock
    private UserFacade userFacade;
    
    @InjectMocks
    private UserController userController;
    
    private MockMvc mockMvc;
    
    @BeforeEach
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(this.userController).build();
    }

    @Test
    public void testUsers() throws Exception {
        Page<UserDto> users = new PageImpl<>(List.of(UserDto.builder().id(1L).userName("thomas").build(), 
                UserDto.builder().id(2L).userName("rob").build(), 
                UserDto.builder().id(3L).userName("kurt").build()));
        
        Mockito.when(this.userFacade.findPaginated(ArgumentMatchers.any(Pageable.class))).thenReturn(users);
        
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name(USERS_VIEW))
                .andExpect(MockMvcResultMatchers.model().attribute("pageContent", Matchers.notNullValue(Page.class)))
//                .andExpect(MockMvcResultMatchers.model().attribute("pageSizes", Matchers.arrayWithSize(Globals.Pagination.PAGE_SIZES.length)))
                .andExpect(MockMvcResultMatchers.model().attribute("link", Matchers.is(Matchers.equalTo("/users"))))
                .andExpect(MockMvcResultMatchers.model().attribute("userId", Matchers.both(Matchers.notNullValue(Long.class))
                        .and(Matchers.equalTo(-1L))));
        Mockito.verify(this.userFacade, Mockito.times(1)).findPaginated(ArgumentMatchers.any(Pageable.class));
        Mockito.verifyNoMoreInteractions(this.userFacade);
    }

    @Test
    public void testDeleteUser() throws Exception {
        
        this.mockMvc.perform(MockMvcRequestBuilders.post("/users/delete")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name(REDIRECT_USERS));
        Mockito.verify(this.userFacade, Mockito.times(1)).deleteUser(ArgumentMatchers.anyLong());
        Mockito.verifyNoMoreInteractions(this.userFacade);
    }

    @Test
    public void testShowUserProfile() throws Exception {
        UserDto userDto = UserDto.builder().id(1L).userName("thomas").build();
        Mockito.when(this.userFacade.findUserById(ArgumentMatchers.anyLong())).thenReturn(userDto);
        
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/profile/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name(USER_PROF_VIEW))
                .andExpect(MockMvcResultMatchers.model().attribute("user", Matchers.notNullValue(UserDto.class)))
                .andExpect(MockMvcResultMatchers.model().attribute("remRole", Matchers.notNullValue(RoleDto.class)));
        Mockito.verify(this.userFacade, Mockito.times(1)).findUserById(ArgumentMatchers.anyLong());
        Mockito.verifyNoMoreInteractions(this.userFacade);
    }

    @Test
    public void testShowUserProfileAddRole() throws Exception {
        UserDto userDto = UserDto.builder().id(1L).userName("thomas").build();
        List<RoleDto> roles = List.of(RoleDto.builder().id(1L).role(Globals.Roles.ROLE_USER).build(),
                RoleDto.builder().id(1L).role(Globals.Roles.ROLE_PREMIUM).build());
        
        Mockito.when(this.userFacade.findUserById(ArgumentMatchers.anyLong())).thenReturn(userDto);
        Mockito.when(this.userFacade.getAvailableRoles(ArgumentMatchers.any(UserDto.class))).thenReturn(roles);
        
        this.mockMvc.perform(MockMvcRequestBuilders.get("/users/role/add/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name(ADD_ROLE_VIEW))
                .andExpect(MockMvcResultMatchers.model().attribute("user", Matchers.notNullValue(UserDto.class)))
                .andExpect(MockMvcResultMatchers.model().attribute("availableRoles", Matchers.hasSize(2)));
        Mockito.verify(this.userFacade, Mockito.times(1)).findUserById(ArgumentMatchers.anyLong());
        Mockito.verify(this.userFacade, Mockito.times(1)).getAvailableRoles(ArgumentMatchers.any(UserDto.class));
        Mockito.verifyNoMoreInteractions(this.userFacade);
                
    }
    
    @Test
    public void testRemoveRole() throws Exception {
        UserDto userDto = UserDto.builder().id(1L).userName("thomas").build();
        
        Mockito.when(this.userFacade.removeRole(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong())).thenReturn(userDto);
        
        this.mockMvc.perform(MockMvcRequestBuilders.post("/users/role/remove/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name(REDIRECT_PROFILE + 1));
        Mockito.verify(this.userFacade, Mockito.times(1)).removeRole(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong());
        Mockito.verifyNoMoreInteractions(this.userFacade);
    }

    @Test
    public void testUpdateUserRoles() throws Exception {
        UserDto userDto = UserDto.builder().id(1L).userName("thomas").build();
        
        Mockito.when(this.userFacade.updateRoles(ArgumentMatchers.anyLong(), ArgumentMatchers.anySet())).thenReturn(userDto);
        
        this.mockMvc.perform(MockMvcRequestBuilders.post("/users/role/add/1")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "1"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.view().name(REDIRECT_PROFILE + 1));
        Mockito.verify(this.userFacade, Mockito.times(1)).updateRoles(ArgumentMatchers.anyLong(), ArgumentMatchers.anySet());
        Mockito.verifyNoMoreInteractions(this.userFacade);
    }
    
}
