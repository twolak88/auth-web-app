package com.twolak.springframework.authwebapp.facade;

import com.twolak.springframework.authwebapp.web.model.RoleDto;
import com.twolak.springframework.authwebapp.web.model.UserDto;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author twolak
 */
public interface UserFacade {
	
	UserDto saveUser(UserDto user);
    UserDto registerUser(UserDto user);
	UserDto findUserById(Long id);
//	List<UserDto> getAllUsers();
	UserDto getEmptyUser();
	void deleteUser(Long userId);
	
	List<RoleDto> getAvailableRoles(UserDto user);
	UserDto updateRoles(Long userId, Set<RoleDto> roles);
	UserDto removeRole(Long userId, Long role);
	Page<UserDto> findPaginated(Pageable pageable);
}
