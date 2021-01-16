package com.twolak.springframework.authwebapp.formatters;

import com.twolak.springframework.authwebapp.domain.Role;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;

/**
 *
 * @author twolak
 */
public class AuthorityFormatter implements Formatter<Role> {

	@Override
	public String print(Role authority, Locale locale) {
		return authority.getId().toString();
	}

	@Override
	public Role parse(String authorityId, Locale locale) throws ParseException {
		Role authority = new Role();
		authority.setId(Long.parseLong(authorityId));
		return authority;
	}

}
