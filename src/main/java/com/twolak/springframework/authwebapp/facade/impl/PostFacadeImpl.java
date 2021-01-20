package com.twolak.springframework.authwebapp.facade.impl;

import com.twolak.springframework.authwebapp.config.Globals;
import com.twolak.springframework.authwebapp.domain.Comment;
import com.twolak.springframework.authwebapp.domain.Post;
import com.twolak.springframework.authwebapp.facade.PostFacade;
import com.twolak.springframework.authwebapp.services.PostService;
import com.twolak.springframework.authwebapp.web.mappers.CommentMapper;
import com.twolak.springframework.authwebapp.web.mappers.CycleAvoidingMappingContext;
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
    private final CommentMapper commentMapper;
	private final PostService postService;
    private final CycleAvoidingMappingContext cycleAvoidingMappingContext;

    public PostFacadeImpl(PostMapper postMapper, CommentMapper commentMapper, PostService postService, CycleAvoidingMappingContext cycleAvoidingMappingContext) {
        this.postMapper = postMapper;
        this.commentMapper = commentMapper;
        this.postService = postService;
        this.cycleAvoidingMappingContext = cycleAvoidingMappingContext;
    }
    
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
	@Override
	public Page<PostDto> findPaginated(Pageable pageable) {
		return this.postService.findPaginated(pageable).map(post -> {
            return this.postMapper.postToPostDto(post, this.cycleAvoidingMappingContext);
        });
	}
	
	@Transactional(propagation = Propagation.REQUIRED, readOnly = true, noRollbackFor = Exception.class)
	@Cacheable(key = "#id", cacheNames = {Globals.Caches.POSTS_CACHE}, sync = true)
	@Override
	public PostDto findPostById(Long id) {
        Post post = this.postService.findPostById(id);
		return this.postMapper.postToPostDto(post, this.cycleAvoidingMappingContext);
	}
	
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
    @CachePut(key = "#result.id", cacheNames = {Globals.Caches.POSTS_CACHE})
	@Override
	public PostDto savePost(PostDto postDto) {
		Post post = this.postMapper.postDtoToPost(postDto, this.cycleAvoidingMappingContext);
		return this.postMapper.postToPostDto(this.postService.savePost(post), this.cycleAvoidingMappingContext);
	}
    
	@Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
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
    
    @Transactional(propagation = Propagation.REQUIRED, noRollbackFor = Exception.class)
    @CachePut(key = "#result.id", cacheNames = {Globals.Caches.POSTS_CACHE})
    @Override
    public PostDto saveComment(Long postId, CommentDto comment) {
        Comment commentToSave = this.commentMapper.commentDtoToComment(comment);
        return this.postMapper.postToPostDto(
                this.postService.saveComment(postId, commentToSave), this.cycleAvoidingMappingContext);
    }
}
