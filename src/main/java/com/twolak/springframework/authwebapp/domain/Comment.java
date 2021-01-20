package com.twolak.springframework.authwebapp.domain;

import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name="COMMENTS")
public class Comment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "comm_id")
	private Long id;
	
	@Column(name = "comm_message")
	@Size(min = 5, max = 100, message = "{comment.message}")
	private String message;
	
	@Column(name = "comm_creationdt", updatable = false)
	@CreationTimestamp
	private Timestamp creationDatetime;
	
    @EqualsAndHashCode.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false/*, cascade = CascadeType.ALL*/)
    @JoinColumn(name = "comm_user_id", nullable = false, updatable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private User owner;
	
    @EqualsAndHashCode.Exclude
	@ManyToOne(fetch = FetchType.LAZY, optional = false/*, cascade = CascadeType.ALL*/)
    @JoinColumn(name = "comm_post_id", nullable = false, updatable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    private Post post;
}
