package com.twolak.springframework.authwebapp.web.controllers;

import com.twolak.springframework.authwebapp.config.Globals;
import com.twolak.springframework.authwebapp.facade.UserFacade;
import com.twolak.springframework.authwebapp.web.model.RoleDto;
import com.twolak.springframework.authwebapp.web.model.UserDto;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author twolak
 */
@Controller
@RequestMapping("/users")
public class UserController {
	
	private final String ADD_ROLE_VIEW = "/users/role/add";
	private final String USER_PROF_VIEW = "/users/profile";
	private final String USERS_VIEW = "/users/users";
	
	private final String REDIRECT_PROFILE = "redirect:/users/profile/";
	private final String REDIRECT_USERS = "redirect:/users";
	
	@Autowired
	private UserFacade userFacade;
	
	@GetMapping
	public String users(Model model,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size,
			@RequestParam("sort") Optional<String> sort){
		int currentPage = page.orElse(Globals.Pagination.INITIAL_PAGE);
		int pageSize = size.orElse(Globals.Pagination.INITIAL_PAGE_SIZE);
		String sortOrder = sort.orElse("id");
		
		Page<UserDto> users = this.userFacade.findPaginated(PageRequest.of(currentPage - 1, pageSize, Sort.by(sortOrder)));
		
		model.addAttribute("pageContent", users);
		model.addAttribute("pageSizes", Globals.Pagination.PAGE_SIZES);
		model.addAttribute("link", "/users");
		model.addAttribute("userId", new Long(-1l));
		return USERS_VIEW;
	}
	
	@PostMapping(value = "/delete")
	public String deleteUser(Model model,
			@ModelAttribute("user") UserDto user) {
		this.userFacade.deleteUser(user.getId());
		return REDIRECT_USERS;
	}
	
	@GetMapping(value = "/profile/{userId}")
	public String showUserProfile(@PathVariable Long userId, Model model) {
        UserDto user = this.userFacade.findUserById(userId);
		model.addAttribute("user", user);
		model.addAttribute("remRole", new RoleDto());
		return USER_PROF_VIEW;
	}
	
	@GetMapping(value = "/role/add/{userId}")
	public String showUserProfileAddRole(@PathVariable Long userId, Model model) {
		UserDto user = this.userFacade.findUserById(userId);
		model.addAttribute("user", user);
		model.addAttribute("availableRoles", this.userFacade.getAvailableRoles(user));
		return ADD_ROLE_VIEW;
	}
    
    @PostMapping(value = "/role/remove/{userId}")
    public String removeRole(Model model,
            @ModelAttribute("remRole") RoleDto authority,
            @PathVariable Long userId,
            BindingResult result) {
        userId = this.userFacade.removeRole(userId, authority.getId()).getId();
        return REDIRECT_PROFILE + userId;
    }
	
	@PostMapping(value = "/role/add/{userId}")
	public String updateUserRoles(Model model, 
			@ModelAttribute("user") UserDto user,
			@PathVariable Long userId,
			BindingResult result) {
		if (result.hasErrors()) {
			return ADD_ROLE_VIEW;
		}
		userId = this.userFacade.updateRoles(user.getId(), user.getRoles()).getId();
		return REDIRECT_PROFILE + userId;
	}
}
