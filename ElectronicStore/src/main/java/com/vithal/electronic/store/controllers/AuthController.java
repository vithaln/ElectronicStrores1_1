package com.vithal.electronic.store.controllers;

import java.security.Principal;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vithal.electronic.store.dtos.JWTRequest;
import com.vithal.electronic.store.dtos.JWTResponse;
import com.vithal.electronic.store.dtos.UserDto;
import com.vithal.electronic.store.exceptions.BadApiRequestException;
import com.vithal.electronic.store.security.JwtHelper;
import com.vithal.electronic.store.services.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthController {
	
	@Autowired
	private UserDetailsService detailsService;
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private UserService service;
	
	@Autowired
	private JwtHelper helper;
	@Autowired
	private ModelMapper mapper;
	
	
	@GetMapping("/current")
	public ResponseEntity<UserDetails>getCurrentUser(Principal principal){
		String name = principal.getName();
		
		UserDetails user = detailsService.loadUserByUsername(name);
		return new ResponseEntity<UserDetails>(user,HttpStatus.OK);
		
	}
	@PostMapping("/login")
	public ResponseEntity<JWTResponse> login(@RequestBody JWTRequest jwtRequest){
		
		this.doAuthenticate(jwtRequest.getEmail(),jwtRequest.getPassword());
		
		UserDetails userDetails = detailsService.loadUserByUsername(jwtRequest.getEmail());
		String generateToken = helper.generateToken(userDetails);
		log.info("Generated JWT Token: {}",generateToken);
		
		UserDto userDto = mapper.map(userDetails, UserDto.class);
		JWTResponse jwtResponse = JWTResponse.builder().jwtToken(generateToken).user(userDto).build();
		return new ResponseEntity<JWTResponse>(jwtResponse,HttpStatus.CREATED);
		
		
		
	}
	private void doAuthenticate(String email, String password) {

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);

try {
	authenticationManager.authenticate(authentication);
	
}
catch (BadCredentialsException e) {
	
 throw new BadApiRequestException("Invalid Username and password !!");
}
	}

}
