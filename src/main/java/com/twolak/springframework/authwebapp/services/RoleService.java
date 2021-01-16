package com.twolak.springframework.authwebapp.services;

import com.twolak.springframework.authwebapp.domain.Role;
import java.util.List;

/**
 *
 * @author twolak
 */
public interface RoleService {
    Role getOrCreateRole(String role);
    Role findById(Long roleId);
    List<Role> findByIdIn(List<Long> roles);
    List<Role> findAll();
}
