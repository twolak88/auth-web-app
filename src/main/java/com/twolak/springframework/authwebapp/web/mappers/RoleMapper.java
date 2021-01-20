/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twolak.springframework.authwebapp.web.mappers;

import com.twolak.springframework.authwebapp.domain.Role;
import com.twolak.springframework.authwebapp.web.model.RoleDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;

/**
 *
 * @author twolak
 */
@Mapper(builder = @Builder(disableBuilder = true))
public interface RoleMapper {
    RoleDto roleToRoleDto(Role role);
    Role roleDtoToRole(RoleDto roleDto);
}
