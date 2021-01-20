package com.twolak.springframework.authwebapp.domain;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

/**
 *
 * @author twolak
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@Entity
@Table(name="POSTS")
public class Post {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "post_id")
	private Long id;
	
	@Column(name = "post_topic")
	@Size(min = 5, max = 30, message = "{post.topic.validation}")
	private String topic;
	
	@Column(name = "post_message")
	@Size(min = 2, max = 100, message = "{post.message.validation}")
	private String message;
	
	@Column(name = "post_creationdt", updatable = false)
	@CreationTimestamp
	private Timestamp creationDatetime;
	
    @EqualsAndHashCode.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false/*, cascade = CascadeType.ALL*/)
    @JoinColumn(name = "post_user_id", nullable = false, updatable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;
	
    @EqualsAndHashCode.Exclude
	@OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Comment> comments = new HashSet<>();
}
