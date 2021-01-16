package com.twolak.springframework.authwebapp.web.controllers;

import com.twolak.springframework.authwebapp.config.Globals;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author twolak
 */
@Controller
public class LoginController {
	
	private final String REDIRECT_LOGOUT = "redirect:/login?logout=true";
	private final String LOGIN_VIEW = "login";
	
	@GetMapping("/login")
	public String loginPage(@RequestParam(value = "error", required = false) String error, 
            				@RequestParam(value = "logout", required = false) String logout,
            				Model model, HttpServletRequest request) {
        String errorMessge = null;
        if(error != null) {
            errorMessge = (String) request.getSession().getAttribute(Globals.AUTH_ERROR_ATTR);
        }
        if(logout != null) {
            errorMessge = "You have been successfully logged out !!";
        }
        model.addAttribute("errorMessge", errorMessge);
        return LOGIN_VIEW;
    }
	
	@GetMapping("/logout")
	public String logoutPage(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
		return REDIRECT_LOGOUT;
	}
}