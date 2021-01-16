package com.twolak.springframework.authwebapp.services.impl;

import com.twolak.springframework.authwebapp.config.Globals.Roles;
import com.twolak.springframework.authwebapp.domain.Post;
import com.twolak.springframework.authwebapp.repository.PostRepository;
import com.twolak.springframework.authwebapp.services.PostService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

/**
 *
 * @author twolak
 */
@Service
public class PostServiceImpl implements PostService {
	
	private final PostRepository postRepository;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }
    
	@PreAuthorize("isAuthenticated()")
	@Override
	public Page<Post> findPaginated(Pageable pageable) {
		return this.postRepository.findAll(pageable);
	}
	
    @PreAuthorize("isAuthenticated()")
	@Override
	public Post findPostById(Long id) {
		return this.postRepository.findById(id).get();
	}
	
    @PreAuthorize("hasRole(T(com.twolak.spring.posts.config.Globals.Roles).ROLE_USER) and #post.message.length() <= 20"
    		+ " or hasRole(T(com.twolak.spring.posts.config.Globals.Roles).ROLE_PREMIUM) "
    		+ " or hasRole(T(com.twolak.spring.posts.config.Globals.Roles).ROLE_ADMIN)")
	@Override
	public Post savePost(Post post) {
		return this.postRepository.save(post);
	}
    
	@Secured(Roles.ROLE_ADMIN)
	@Override
	public void deletePost(Long postId) {
		this.postRepository.deleteById(postId);		
	}

//	@Override
//	public Post getEmptyPost() {
//		return new Post();
//	}
}
