package com.twolak.springframework.authwebapp.services.impl;

import com.twolak.springframework.authwebapp.domain.Role;
import com.twolak.springframework.authwebapp.repository.RolesRepository;
import com.twolak.springframework.authwebapp.services.RoleService;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;

/**
 *
 * @author twolak
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final RolesRepository rolesRepository;

    public RoleServiceImpl(RolesRepository rolesRepository) {
        this.rolesRepository = rolesRepository;
    }
    
    @Override
    public Role getOrCreateRole(String role) {
        Role authority = this.rolesRepository.getRoleByRole(role);
        if (authority == null) {
            Role newAuthority = new Role();
            newAuthority.setRole(role);
            this.rolesRepository.save(newAuthority);
        }
        return authority;
    }

    @Override
    public Role findById(Long roleId) {
        return this.rolesRepository.findById(roleId).get();
    }

    @Override
    public Set<Role> findByIdIn(Set<Long> roles) {
        return this.rolesRepository.findByIdIn(roles);
    }

    @Override
    public List<Role> findAll() {
        return this.rolesRepository.findAll();
    }
    
    
    
}
