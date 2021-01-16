package com.twolak.springframework.authwebapp.services;

import com.twolak.springframework.authwebapp.domain.User;

/**
 *
 * @author twolak
 */
public interface SecurityService {
	User getAuthenticatedUser();
}
