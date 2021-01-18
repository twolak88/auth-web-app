package com.twolak.springframework.authwebapp.config.security;

import com.twolak.springframework.authwebapp.config.Globals;
import com.twolak.springframework.authwebapp.handlers.LoginFailureHandler;
import com.twolak.springframework.authwebapp.services.UserService;
import com.twolak.springframework.authwebapp.services.security.SecurityServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author twolak
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    
    @Autowired
    private UserService userService;
    
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        return new SecurityServiceImpl(userService);
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	//permit all
//    	http.authorizeRequests().anyRequest().permitAll().and().csrf().disable();
    	
    	http.authorizeRequests()
    		//access
//    			.antMatchers("/**").hasAnyAuthority(RoleType.ROLE_USER.toString() + "," + RoleType.ROLE_ADMIN.toString())
    			.antMatchers("/users/**").hasAuthority(Globals.Roles.ROLE_ADMIN)
    			.antMatchers("/login/**", "/register/**", "/", "/home", "/css/**", "/h2-console/**").permitAll()
//    			.anyRequest().permitAll()
    		
    		//https
//        	.and().requiresChannel().antMatchers("/posts", "/posts/users/**").requiresInsecure()
//    		.and().requiresChannel().antMatchers("/posts/login", "/posts/register").requiresSecure()
    		
    		//login
    		.and().formLogin().loginPage("/login")
    			.defaultSuccessUrl("/home").failureUrl("/login?error=true").failureHandler(this.loginFailureHandler).permitAll()
    		
    		//logout
    		.and().logout().logoutSuccessUrl("/login?logout=true").deleteCookies("JSESSIONID")
            	.invalidateHttpSession(true).clearAuthentication(true).permitAll()
    	
    		.and().rememberMe().rememberMeParameter("custom-remember-me")
    			.rememberMeCookieName("REMEMBERME").tokenValiditySeconds(24*60*60).key("authKey");
            
//            .and().sessionManagement().sessionFixation().none() 

        //for h2-console
//        http.csrf().disable();
//        http.headers().frameOptions().disable();
            
//    		.and().httpBasic().realmName("Posts");
    }
	
//	@Override
//	public void configure(AuthenticationManagerBuilder auth)
//	@Autowired
//	public void customConfigureAuthBuilder(AuthenticationManagerBuilder auth)
//	  throws Exception {
//	    auth.inMemoryAuthentication()
//	      .withUser("user").password("password").roles("USER")
//	      .and()
//	      .withUser("admin").password("password").roles("USER", "ADMIN");
//	}
}