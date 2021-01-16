package com.twolak.springframework.authwebapp.web.model;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
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
public class PostDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	@Size(min = 5, max = 30, message = "{post.topic.validation}")
	private String topic;
	
	@Size(min = 5, max = 100, message = "{post.message.validation}")
	private String message;
	
	private OffsetDateTime creationDatetime;
	
	private UserDto owner;
	
	private Set<CommentDto> comments = new HashSet<>();
}
