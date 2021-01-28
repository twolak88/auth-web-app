package com.twolak.springframework.authwebapp.repository;

import com.twolak.springframework.authwebapp.domain.Role;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author twolak
 */
public interface RolesRepository extends JpaRepository<Role, Long> {
	Role getRoleByRole(String role);
    Set<Role> findByIdIn(Set<Long> roleIdList);
}
