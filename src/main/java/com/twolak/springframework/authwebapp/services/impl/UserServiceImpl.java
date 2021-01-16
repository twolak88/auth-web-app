package com.twolak.springframework.authwebapp.services.impl;

import com.twolak.springframework.authwebapp.config.Globals;
import com.twolak.springframework.authwebapp.config.Globals.Roles;
import com.twolak.springframework.authwebapp.domain.Role;
import com.twolak.springframework.authwebapp.domain.User;
import com.twolak.springframework.authwebapp.repository.UserRepository;
import com.twolak.springframework.authwebapp.services.RoleService;
import com.twolak.springframework.authwebapp.services.UserService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;

/**
 *
 * @author twolak
 */
@Service
public class UserServiceImpl implements UserService {
	
	private final UserRepository userRepository;
    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }
    
	/* UserService implementation */
    @Secured(Roles.ROLE_ADMIN)
    @Override
	public User saveUser(User user) {
		return userRepository.save(user);
	}

    @Override
    public User saveRegisteredUser(User user) {
        addRegistrationRole(user);
        return userRepository.save(user);
    }
	
    @Secured(Roles.ROLE_ADMIN)
    @Override
	public User findUserById(Long id) {
		return this.userRepository.findById(id).get();
	}
    
	@Override
	public User findUserByUserName(String userName) {
		return this.userRepository.getUserByUserName(userName);
	}
	
    @Secured(Roles.ROLE_ADMIN)
    @Override
	public Page<User> findPaginated(Pageable pageable) {
        return this.userRepository.findAll(pageable);
	}
	
//    @Override
//	public User getEmptyUser() {
//		return new User();
//	}
	
	@Secured(Roles.ROLE_ADMIN)
	@Override
	public void deleteUser(Long userId) {
		this.userRepository.deleteById(userId);
	}
	
	@Secured(Roles.ROLE_ADMIN)
	@Override
	public List<Role> getAvailableRoles(User user) {
		Globals.RolesList.forEach(role->{
            this.roleService.getOrCreateRole(role);
		});
		List<Role> availableAuthorities = this.roleService.findAll().stream()
				.filter(role -> user.getRoles().stream().map(Role::getRole)
						.noneMatch(r->r.equals(role.getRole())))
				.collect(Collectors.toList());
		return availableAuthorities;
	}
	
	@Secured(Roles.ROLE_ADMIN)
	@Override
	public User updateRoles(Long userId, Set<Role> authorities) {
		User user = this.userRepository.findById(userId).get();
		List<Role> authorityList = this.roleService.findByIdIn(authorities.stream().map(Role::getId).collect(Collectors.toList()));
		user.getRoles().addAll(authorityList);
		this.userRepository.save(user);
		return user;
	}
	
	@Secured(Roles.ROLE_ADMIN)
	@Override
	public User removeRole(Long userId, Long roleId) {
		User user = this.userRepository.findById(userId).get();
		boolean ret = user.getRoles().remove(this.roleService.findById(roleId));
		if (ret)
			user = this.userRepository.save(user);
		return user;
	}
    
    private User addRegistrationRole(User user) {
        Role role = this.roleService.getOrCreateRole(Globals.Roles.ROLE_USER);
        user.getRoles().add(role);
        return user;
    }
}
