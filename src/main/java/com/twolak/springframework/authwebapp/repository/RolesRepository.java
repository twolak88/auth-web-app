package com.twolak.springframework.authwebapp.repository;

import com.twolak.springframework.authwebapp.domain.Role;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author twolak
 */
public interface RolesRepository extends JpaRepository<Role, Long> {
	Role getRoleByRole(String role);
	List<Role> findByIdIn(List<Long> roleIdList);
}
