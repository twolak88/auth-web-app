package com.twolak.springframework.authwebapp.formatters;

import com.twolak.springframework.authwebapp.web.model.RoleDto;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

/**
 *
 * @author twolak
 */
@Component
public class AuthorityFormatter implements Formatter<RoleDto> {

	@Override
	public String print(RoleDto authority, Locale locale) {
		return authority.getId().toString();
	}

	@Override
	public RoleDto parse(String authorityId, Locale locale) throws ParseException {
		RoleDto authority = new RoleDto();
		authority.setId(Long.parseLong(authorityId));
		return authority;
	}

}
