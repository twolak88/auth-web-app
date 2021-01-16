package com.twolak.springframework.authwebapp.web.model;

import java.io.Serializable;
import java.time.OffsetDateTime;
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
public class CommentDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@Size(min = 5, max = 100, message = "{comment.message}")
	private String message;
	
	private OffsetDateTime creationDatetime;
	
	private UserDto owner;
	
	private PostDto post;
}
