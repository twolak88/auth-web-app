package com.twolak.springframework.authwebapp.web.controllers;

import com.twolak.springframework.authwebapp.config.Globals;
import com.twolak.springframework.authwebapp.facade.PostFacade;
import com.twolak.springframework.authwebapp.web.model.PostDto;
import java.util.Optional;
import javax.validation.Valid;
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
@RequestMapping("/posts")
public class PostController {
	
	private final String POSTS_VIEW = "/posts/posts";
	private final String POST_VIEW = "/posts/post";
	private final String NEW_POST_FORM_VIEW = "/posts/form";
	private final String REDIRECT_SUCESS = "redirect:/posts/post/";
	
	private final String REDIRECT_POSTS = "redirect:/posts/list";
	
	@Autowired
	private PostFacade postFacade;
	
	@GetMapping(value = "/list")
	public String posts(Model model,
			@RequestParam("page") Optional<Integer> page,
			@RequestParam("size") Optional<Integer> size,
			@RequestParam("sort") Optional<String> sort){
		int currentPage = page.orElse(Globals.Pagination.INITIAL_PAGE);
		int pageSize = size.orElse(Globals.Pagination.INITIAL_PAGE_SIZE);
		String sortOrder = sort.orElse("id");
		
		Page<PostDto> posts = this.postFacade.findPaginated(PageRequest.of(currentPage - 1, pageSize, Sort.by(sortOrder)));
		
		model.addAttribute("pageContent", posts);
		model.addAttribute("pageSizes", Globals.Pagination.PAGE_SIZES);
		model.addAttribute("link", "/posts/list");
		model.addAttribute("postId", new Long(-1l));
		return POSTS_VIEW;
	}
	
	@PostMapping(value = "/delete")
	public String deletePost(Model model,
			@ModelAttribute("userId") Long postId) {
		this.postFacade.deletePost(postId);
		return REDIRECT_POSTS;
	}
	
	@GetMapping(value = "/post/{postId}")
	public String showUserProfile(@PathVariable Long postId, Model model) {
		model.addAttribute("post", this.postFacade.findPostById(postId));
		model.addAttribute("comment", this.postFacade.getEmptyComment());
		return POST_VIEW;
	}
	
	@GetMapping(value = "/new")
	public String showNewPostForm(Model model) {
		model.addAttribute("post", this.postFacade.getEmptyPost());
		return NEW_POST_FORM_VIEW;
	}
	
	@PostMapping("/new")
	public String processNewPost(Model model, @Valid @ModelAttribute("post") PostDto post,
			BindingResult result) {
		if (result.hasErrors()) {
			return NEW_POST_FORM_VIEW;
		}
		post = this.postFacade.savePost(post);
		return REDIRECT_SUCESS + post.getId();
	}
}
