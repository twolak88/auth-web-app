package com.twolak.springframework.authwebapp.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author twolak
 */
public class Globals {
	
	//Security
	public static final List<String> RolesList = Collections.unmodifiableList(new ArrayList<String>() {{
		add(Roles.ROLE_ADMIN); 
		add(Roles.ROLE_USER);
		add(Roles.ROLE_PREMIUM);
	}});
    
    public static final String AUTH_ERROR_ATTR = "AUTH_ERROR";
	
	public static class Roles {
		public static final String ROLE_ADMIN = "ROLE_ADMIN";
		public static final String ROLE_USER = "ROLE_USER";
		public static final String ROLE_PREMIUM = "ROLE_PREMIUM";
	}
	
	public static class Pagination {
	    public static final int BUTTONS_TO_SHOW = 3;
	    public static final int INITIAL_PAGE = 1;
	    public static final int INITIAL_PAGE_SIZE = 3;
	    public static final int[] PAGE_SIZES = { 1, 2, 3, 5, 10, 20};
	}
	
	public static class Caches {
		public static final String USERS_CACHE = "USERS_CACHE";
		public static final String POSTS_CACHE = "POSTS_CACHE";
	}
}
