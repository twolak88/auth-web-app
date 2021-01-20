package com.twolak.springframework.authwebapp.facade;

import com.twolak.springframework.authwebapp.web.model.CommentDto;
import com.twolak.springframework.authwebapp.web.model.PostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author twolak
 */
public interface PostFacade {
	
	Page<PostDto> findPaginated(Pageable pageable);
	PostDto findPostById(Long id);
	PostDto savePost(PostDto post);
	void deletePost(Long postId);
	PostDto getEmptyPost();
	CommentDto getEmptyComment();
    PostDto saveComment(Long postId, CommentDto comment);
}
