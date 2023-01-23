package com.vithal.electronic.store.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.vithal.electronic.store.security.JwtAuthenticationEntryPoint;
import com.vithal.electronic.store.security.JwtAuthenticationFilter;

import lombok.Builder;


@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	@Autowired
	private JwtAuthenticationEntryPoint authenticationEntryPoint;
	
	@Autowired
	private JwtAuthenticationFilter authenticationFilter;
	
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
		
		http.csrf().disable().cors().disable() .authorizeRequests()
		.antMatchers("/auth/login").permitAll()
		.antMatchers("/auth/current").permitAll()
		.antMatchers(HttpMethod.POST,"/users")
		.permitAll()
		.antMatchers(HttpMethod.DELETE,"/users/**").hasRole("ADMIN")
		.anyRequest().authenticated()
		.and()
		.exceptionHandling()
		.authenticationEntryPoint(authenticationEntryPoint)
		.and()
		.sessionManagement()
		.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		 http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);		
		
		
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
@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		
		return config.getAuthenticationManager();
	}

}
