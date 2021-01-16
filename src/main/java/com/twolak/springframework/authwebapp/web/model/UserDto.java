package com.twolak.springframework.authwebapp.web.model;

import com.twolak.springframework.authwebapp.domain.Role;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author twolak
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@Size(min = 5, max = 16, message = "{username.size}")
	private String userName;
	
	@Size(min = 2, max = 30, message = "{user_first_name.size}")
	private String userFirstName;
	
	@Size(min = 2, max = 30, message = "{user_last_name.size}")
	private String userLastName;
	
	@Size(min = 5, max = 20, message = "{user_password.size}")
	private String userPassword;
	
	@Size(min = 3, max = 30, message = "{user_email.size}")
	@Email(message = "{user_email.format}")
	private String userEmail;
	
	private OffsetDateTime userRegistrationDate;
	
	private Boolean isActive;
	
	private Set<Role> roles = new HashSet<>();
	
	private Set<PostDto> posts = new HashSet<>();
	
	private Set<CommentDto> comments = new HashSet<>();
}
