package com.twolak.springframework.authwebapp.repository;

import com.twolak.springframework.authwebapp.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author twolak
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User getUserByUserName(String username);
}
