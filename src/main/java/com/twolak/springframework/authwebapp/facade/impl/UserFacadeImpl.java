package com.twolak.springframework.authwebapp.facade.impl;

import com.twolak.springframework.authwebapp.config.Globals;
import com.twolak.springframework.authwebapp.domain.Role;
import com.twolak.springframework.authwebapp.domain.User;
import com.twolak.springframework.authwebapp.facade.UserFacade;
import com.twolak.springframework.authwebapp.services.UserService;
import com.twolak.springframework.authwebapp.web.mappers.CycleAvoidingMappingContext;
import com.twolak.springframework.authwebapp.web.mappers.RoleMapper;
import com.twolak.springframework.authwebapp.web.mappers.UserMapper;
import com.twolak.springframework.authwebapp.web.model.RoleDto;
import com.twolak.springframework.authwebapp.web.model.UserDto;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author twolak
 */
@Component
public class UserFacadeImpl implements UserFacade{
    
    private final UserMapper userMapper;
	private final UserService userService;
    private final RoleMapper roleMapper;
    private final CycleAvoidingMappingContext cycleAvoidingMappingContext;

    public UserFacadeImpl(UserMapper userMapper, UserService userService, RoleMapper roleMapper, CycleAvoidingMappingContext cycleAvoidingMappingContext) {
        this.userMapper = userMapper;
        this.userService = userService;
        this.roleMapper = roleMapper;
        this.cycleAvoidingMappingContext = cycleAvoidingMappingContext;
    }
    
	@Transactional
    @CachePut(key = "#result.id", cacheNames = {Globals.Caches.USERS_CACHE})
	@Override
	public UserDto saveUser(UserDto user) {
        User convertedUser = this.userMapper.userDtoToUser(user, this.cycleAvoidingMappingContext);
        User savedUser = this.userService.saveUser(convertedUser);
		return this.userMapper.userToUserDto(savedUser, this.cycleAvoidingMappingContext);
	}
    
    @Transactional
    @CachePut(key = "#result.id", cacheNames = {Globals.Caches.USERS_CACHE})
    @Override
    public UserDto registerUser(UserDto user) {
        user.setIsActive(Boolean.FALSE);
        user.setUserPassword(new BCryptPasswordEncoder().encode(user.getUserPassword()));
        User convertedUser = this.userMapper.userDtoToUser(user, this.cycleAvoidingMappingContext);
        User savedUser = this.userService.saveRegisteredUser(convertedUser);
        return this.userMapper.userToUserDto(savedUser, this.cycleAvoidingMappingContext);
    }
	
    @Transactional
	@Cacheable(key = "#id", cacheNames = {Globals.Caches.USERS_CACHE}, sync = true)
	@Override
	public UserDto findUserById(Long id) {
        User user = this.userService.findUserById(id);
		return this.userMapper.userToUserDto(user, this.cycleAvoidingMappingContext);
	}

	@Override
	public UserDto getEmptyUser() {
		return new UserDto();
	}
	
	@Transactional
	@CacheEvict(key = "#userId", cacheNames = {Globals.Caches.USERS_CACHE})
	@Override
	public void deleteUser(Long userId) {
		this.userService.deleteUser(userId);
		
	}

	@Override
	public List<RoleDto> getAvailableRoles(UserDto user) {
        User convertedUser = this.userMapper.userDtoToUser(user, this.cycleAvoidingMappingContext);
		return this.userService.getAvailableRoles(convertedUser).stream().map(this.roleMapper::roleToRoleDto).collect(Collectors.toList());
	}
	
	@Transactional
	@CachePut(key = "#userId", cacheNames = {Globals.Caches.USERS_CACHE})
	@Override
	public UserDto updateRoles(Long userId, Set<RoleDto> roles) {
        Set<Role> convertedRoles = roles.stream().map(this.roleMapper::roleDtoToRole).collect(Collectors.toSet());
        User user = this.userService.updateRoles(userId, convertedRoles);
		return this.userMapper.userToUserDto(user, this.cycleAvoidingMappingContext);
	}
	
	@Transactional
	@CachePut(key = "#userId", cacheNames = {Globals.Caches.USERS_CACHE})
	@Override
	public UserDto removeRole(Long userId, Long role) {
        User user = this.userService.removeRole(userId, role);
		return this.userMapper.userToUserDto(user, this.cycleAvoidingMappingContext);
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	@Override
	public Page<UserDto> findPaginated(Pageable pageable) {
        Page<User> users = this.userService.findPaginated(pageable);
		return users.map(user -> {
            return this.userMapper.userToUserDto(user, cycleAvoidingMappingContext);
        });
	}
}
