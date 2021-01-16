package com.twolak.springframework.authwebapp.services;

import com.twolak.springframework.authwebapp.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 *
 * @author twolak
 */
public interface PostService {
	Page<Post> findPaginated(Pageable pageable);
	
	Post findPostById(Long id);
	Post savePost(Post post);
	void deletePost(Long postId);
//	Post getEmptyPost();
}