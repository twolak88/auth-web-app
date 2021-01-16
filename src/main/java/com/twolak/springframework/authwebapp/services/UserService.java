package com.twolak.springframework.authwebapp.services;

import com.twolak.springframework.authwebapp.domain.Role;
import com.twolak.springframework.authwebapp.domain.User;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author twolak
 */
public interface UserService {
	
	User saveUser(User user);
    User saveRegisteredUser(User user);
	User findUserById(Long id);
	User findUserByUserName(String userName);
//	List<User> getAllUsers();
//	User getEmptyUser();
	void deleteUser(Long userId);
	
	List<Role> getAvailableRoles(User user);
	User updateRoles(Long userId, Set<Role> authorities);
	User removeRole(Long userId, Long role);
	Page<User> findPaginated(Pageable pageable);
}
