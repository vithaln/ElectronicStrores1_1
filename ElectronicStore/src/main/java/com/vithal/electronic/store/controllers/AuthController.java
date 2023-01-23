package com.vithal.electronic.store.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	private UserDetailsService detailsService;
	
	@GetMapping("/current")
	public ResponseEntity<UserDetails>getCurrentUser(Principal principal){
		String name = principal.getName();
		
		UserDetails user = detailsService.loadUserByUsername(name);
		return new ResponseEntity<UserDetails>(user,HttpStatus.OK);
		
	}

}
