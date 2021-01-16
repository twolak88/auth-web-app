package com.twolak.springframework.authwebapp.repository;

import com.twolak.springframework.authwebapp.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author twolak
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long>{
//	List<Post> findMessages(int count, int max);
}