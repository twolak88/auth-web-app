package com.twolak.springframework.authwebapp.services.security;

import com.twolak.springframework.authwebapp.domain.User;
import com.twolak.springframework.authwebapp.services.CustomUserDetails;
import com.twolak.springframework.authwebapp.services.SecurityService;
import com.twolak.springframework.authwebapp.services.UserService;
import org.hibernate.Hibernate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author twolak
 */
@Service
public class SecurityServiceImpl implements UserDetailsService, SecurityService{
    
	private final UserService userService;

    public SecurityServiceImpl(UserService userService) {
        this.userService = userService;
    }
	
	@Override
	public User getAuthenticatedUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof CustomUserDetails)
			return ((CustomUserDetails) principal).getAuthenticatedUser();
		if (principal instanceof UserDetails)
			return this.userService.findUserByUserName(((UserDetails) principal).getUsername());
		return this.userService.findUserByUserName(principal.toString());
	}
	
    @Transactional
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = this.userService.findUserByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found.");
        }
        Hibernate.initialize(user.getRoles());
//        user.getRoles().size(); //not elegant - lazy loading no session
        return new UserDetailsImpl(user);
	}

}
