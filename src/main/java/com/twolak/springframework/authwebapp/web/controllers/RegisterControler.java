package com.twolak.springframework.authwebapp.web.controllers;

import com.twolak.springframework.authwebapp.facade.UserFacade;
import com.twolak.springframework.authwebapp.web.model.UserDto;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author twolak
 */
@Controller
@RequestMapping(value = "/register")
public class RegisterControler {
	
	private final String USER_REG_FORM_VIEW = "/registration/form";
	private final String USER_SUCCESS_VIEW = "/registration/successful";
	private final String REDIRECT_SUCESS = "redirect:/register/successful/";
	
	@Autowired
	private UserFacade userFacade;
    
    @InitBinder
    public void setAllowedFields(WebDataBinder webDataBinder) {
        webDataBinder.setDisallowedFields("id");
    }
	
	@PostMapping
	public String processRegistration(Model model, @Valid @ModelAttribute("user") UserDto user,
			BindingResult result) {
		if (result.hasErrors()) {
			return USER_REG_FORM_VIEW;
		}
		user = this.userFacade.registerUser(user);
		return REDIRECT_SUCESS + user.getId();
	}
	
	@GetMapping(value = "/successful/{userId}")
	public String showSuccessfulMessage(@PathVariable Long userId, Model model) {
		model.addAttribute("user", this.userFacade.findUserById(userId));
		return USER_SUCCESS_VIEW;
	}
	
	@GetMapping
	public String showUserRegistrationForm(Model model) {
		model.addAttribute("user", this.userFacade.getEmptyUser());
		return USER_REG_FORM_VIEW;
	}
}
