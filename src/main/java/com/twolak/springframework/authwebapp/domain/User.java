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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
@Entity
@Table(name="USERS")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long id;

	@Column(name = "user_username", unique = true)
	@Size(min = 5, max = 16, message = "{username.size}")
	private String userName;
	
	@Column(name = "user_firstname")
	@Size(min = 2, max = 30, message = "{user_first_name.size}")
	private String userFirstName;
	
	@Column(name = "user_lastname")
	@Size(min = 2, max = 30, message = "{user_last_name.size}")
	private String userLastName;
	
	@Column(name = "user_password", length = 100)
	@Size(min = 5, max = 100, message = "{user_password.size}")
	private String userPassword;
	
	@Column(name = "user_email")
	@Size(min = 3, max = 30, message = "{user_email.size}")
	@Email(message = "{user_email.format}")
	private String userEmail;
	
	@Column(name = "user_reg_date", updatable = false)
	@CreationTimestamp
	private Timestamp userRegistrationDate;
	
	@Column(name = "user_isactive")
	private Boolean isActive;
	
	@ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "role_id") })
    private Set<Role> roles = new HashSet<>();
	
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<Post> posts = new HashSet<>();
	
	@OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private Set<Comment> comments = new HashSet<>();
    
}
