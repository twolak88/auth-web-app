package com.twolak.springframework.authwebapp.facade.impl;

import com.twolak.springframework.authwebapp.config.Globals;
import com.twolak.springframework.authwebapp.domain.Post;
import com.twolak.springframework.authwebapp.facade.PostFacade;
import com.twolak.springframework.authwebapp.services.PostService;
import com.twolak.springframework.authwebapp.services.SecurityService;
import com.twolak.springframework.authwebapp.web.mappers.PostMapper;
import com.twolak.springframework.authwebapp.web.model.CommentDto;
import com.twolak.springframework.authwebapp.web.model.PostDto;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author twolak
 */
@Component
public class PostFacadeImpl implements PostFacade{
    
	private final PostMapper postMapper;
	private final PostService postService;
	private final SecurityService securityService;
    
    public PostFacadeImpl(PostMapper postMapper, PostService postService, SecurityService securityService) {
        this.postMapper = postMapper;
        this.postService = postService;
        this.securityService = securityService;
    }
	
	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	public Page<PostDto> findPaginated(Pageable pageable) {
		return this.postService.findPaginated(pageable).map(this.postMapper::postToPostDto);
	}
	
	@Transactional
	@Cacheable(key = "#id", cacheNames = {Globals.Caches.POSTS_CACHE}, sync = true)
	@Override
	public PostDto findPostById(Long id) {
		return this.postMapper.postToPostDto(this.postService.findPostById(id));
	}
	
	@Transactional
    @CachePut(key = "#result.id", cacheNames = {Globals.Caches.POSTS_CACHE})
	@Override
	public PostDto savePost(PostDto postDto) {
		Post post = this.postMapper.postDtoToPost(postDto);
		post.setOwner(this.securityService.getAuthenticatedUser());
		return this.postMapper.postToPostDto(this.postService.savePost(post));
	}
    
	@Transactional
	@CacheEvict(key = "#postId", cacheNames = {Globals.Caches.POSTS_CACHE})
	@Override
	public void deletePost(Long postId) {
		this.postService.deletePost(postId);		
	}

	@Override
	public PostDto getEmptyPost() {
		return new PostDto();
	}

	@Override
	public CommentDto getEmptyComment() {
		return new CommentDto();
	}
}
