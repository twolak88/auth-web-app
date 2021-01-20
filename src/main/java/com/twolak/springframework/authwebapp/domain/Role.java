package com.twolak.springframework.authwebapp.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
@Table(name="ROLES")
public class Role implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_id")
	private Long id;
	
	
	@Column(name = "role_name")
	@Size(min = 3, max = 10)
	private String role;

//	@ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "auth_user_id", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//	private User user;
}
