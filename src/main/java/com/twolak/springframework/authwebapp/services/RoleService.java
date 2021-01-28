package com.twolak.springframework.authwebapp.services;

import com.twolak.springframework.authwebapp.domain.Role;
import java.util.List;
import java.util.Set;

/**
 *
 * @author twolak
 */
public interface RoleService {
    Role getOrCreateRole(String role);
    Role findById(Long roleId);
    Set<Role> findByIdIn(Set<Long> roles);
    List<Role> findAll();
}
