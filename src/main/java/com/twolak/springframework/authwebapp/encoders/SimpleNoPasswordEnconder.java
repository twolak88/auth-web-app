package com.twolak.springframework.authwebapp.encoders;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author twolak
 */
public class SimpleNoPasswordEnconder implements PasswordEncoder{

    @Override
	public String encode(CharSequence rawPassword) {
		return rawPassword.toString();
	}

    @Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return rawPassword.toString().equals(encodedPassword);
	}
}
