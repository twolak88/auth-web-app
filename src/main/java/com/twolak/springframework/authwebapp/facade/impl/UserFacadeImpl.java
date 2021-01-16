package com.twolak.springframework.authwebapp.facade.impl;

import com.twolak.springframework.authwebapp.config.Globals;
import com.twolak.springframework.authwebapp.domain.Role;
import com.twolak.springframework.authwebapp.facade.UserFacade;
import com.twolak.springframework.authwebapp.services.UserService;
import com.twolak.springframework.authwebapp.web.mappers.UserMapper;
import com.twolak.springframework.authwebapp.web.model.UserDto;
import java.util.List;
import java.util.Set;
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
	
    public UserFacadeImpl(UserMapper userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }
    
	@Transactional
    @CachePut(key = "#result.id", cacheNames = {Globals.Caches.USERS_CACHE})
	@Override
	public UserDto saveUser(UserDto user) {
		return this.userMapper.userToUserDto(this.userService.saveUser(
				this.userMapper.userDtoToUser(user)));
	}
    
    @Transactional
    @CachePut(key = "#result.id", cacheNames = {Globals.Caches.USERS_CACHE})
    @Override
    public UserDto registerUser(UserDto user) {
        user.setIsActive(Boolean.FALSE);
        user.setUserPassword(new BCryptPasswordEncoder().encode(user.getUserPassword()));
        return this.userMapper.userToUserDto(this.userService.saveRegisteredUser(
                this.userMapper.userDtoToUser(user)));
    }
	
    @Transactional
	@Cacheable(key = "#id", cacheNames = {Globals.Caches.USERS_CACHE}, sync = true)
	@Override
	public UserDto findUserById(Long id) {
		return this.userMapper.userToUserDto(this.userService.findUserById(id));
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
	public List<Role> getAvailableRoles(UserDto user) {
		return this.userService.getAvailableRoles(this.userMapper.userDtoToUser(user));
	}
	
	@Transactional
	@CachePut(key = "#userId", cacheNames = {Globals.Caches.USERS_CACHE})
	@Override
	public UserDto updateRoles(Long userId, Set<Role> roles) {
		return this.userMapper.userToUserDto(this.userService.updateRoles(userId, roles));
	}
	
	@Transactional
	@CachePut(key = "#userId", cacheNames = {Globals.Caches.USERS_CACHE})
	@Override
	public UserDto removeRole(Long userId, Long role) {
		return this.userMapper.userToUserDto(this.userService.removeRole(userId, role));
	}

	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	@Override
	public Page<UserDto> findPaginated(Pageable pageable) {
		return this.userService.findPaginated(pageable).map(this.userMapper::userToUserDto);
	}
}
