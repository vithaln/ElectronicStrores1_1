package com.vithal.electronic.store.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {
	
	/* this for InMemoryDB.
	 * 
	 * @Bean public UserDetailsService detailsService() {
	 * 
	 * UserDetails normal =
	 * User.builder().username("vithal").password(encoder().encode("vithal")).roles(
	 * "NORMAL").build();
	 * 
	 * UserDetails admin =
	 * User.builder().username("vikki").password(encoder().encode("vikki")).roles(
	 * "ADMIN").build();
	 * 
	 * return new InMemoryUserDetailsManager(normal,admin);
	 * 
	 * 
	 * }
	 */
	
	/*
	@Bean
	public SecurityFilterChain chain(HttpSecurity http) throws Exception {

		 BASIC LOGIN FORM
		 * 
		 * http.authorizeRequests().anyRequest().authenticated().and() .formLogin()
		 * .loginPage("/login.html") .loginProcessingUrl("process-page.html")
		 * .defaultSuccessUrl("success.html") .failureUrl("fail.html") .and() .logout()
		 * .logoutSuccessUrl("logout.html");
		
		
		
		
		
		
		return http.build();
		
	}   */
	
	//http BASIC Authentication
	
	@Bean
	public SecurityFilterChain chain(HttpSecurity http) throws Exception {
		
		http.csrf().disable().cors().disable() .authorizeRequests().anyRequest().authenticated()
		.and()
		.httpBasic();
		
		
		return http.build();
	}
	
	
	
	
	@Autowired
	public UserDetailsService detailsService;
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(detailsService);
		daoAuthenticationProvider.setPasswordEncoder(encoder());
		
		return daoAuthenticationProvider;
	
	}
	
	
	@Bean
	public PasswordEncoder encoder() {
		
		return new BCryptPasswordEncoder();
	}


}
