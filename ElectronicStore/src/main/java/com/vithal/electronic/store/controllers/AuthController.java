package com.vithal.electronic.store.controllers;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier.Builder;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.vithal.electronic.store.dtos.JWTRequest;
import com.vithal.electronic.store.dtos.JWTResponse;
import com.vithal.electronic.store.dtos.UserDto;
import com.vithal.electronic.store.entities.User;
import com.vithal.electronic.store.exceptions.BadApiRequestException;
import com.vithal.electronic.store.security.JwtHelper;
import com.vithal.electronic.store.services.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/auth")
@Slf4j
//@CrossOrigin("*")
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
	
	@Value("${googleClientid}")
	private String googleClientId;
	@Value("${newPassword}")
	private String newPassword;
	
	
	@GetMapping("/current")
	public ResponseEntity<UserDto>getCurrentUser(Principal principal){
		String name = principal.getName();
		
		UserDetails user = detailsService.loadUserByUsername(name);
		UserDto userDto = mapper.map(user, UserDto.class);
		return new ResponseEntity<UserDto>(userDto,HttpStatus.OK);
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
	
	//login with google
	@PostMapping("/google")
	public ResponseEntity<JWTResponse> loginWithGoogle(@RequestBody Map<String, Object> data) throws IOException{
		
		String idToken = data.get("idToken").toString();
		
		NetHttpTransport netHttpTransport = new NetHttpTransport();
	//for google verify
		JacksonFactory jacksonFactory = JacksonFactory.getDefaultInstance();
		GoogleIdTokenVerifier.Builder verifyeir = new GoogleIdTokenVerifier.Builder(netHttpTransport, jacksonFactory).setAudience(Collections.singleton(googleClientId));
	
	
	GoogleIdToken googleIdToken = GoogleIdToken.parse(verifyeir.getJsonFactory(), idToken);
	
	Payload payload = googleIdToken.getPayload();
	
	log.info("PAYLOAD:  {} ",payload);
	String email = payload.getEmail();
	log.info("email is : {} ",email);
	
	User user=null;
	
	
 user = service.findUserByEmailOptional(email).get();
	
 if(user==null) {
	 
	 //create new user
	user= this.saveUser(email,data.get("name").toString(),data.get("photoURL").toString());
 }
 ;
ResponseEntity<JWTResponse> jwtResponse = this.login(JWTRequest.builder().email(user.getEmail()).password(newPassword).build());
	
return jwtResponse;
	}
	private User saveUser(String email, String name, String photoURL) {


		UserDto newUser = UserDto.builder().name(name)
		.email(email)
		.password(newPassword)
.imageName(photoURL)
.roles(new HashSet<>()).build();
		
		UserDto createUser = service.createUser(newUser);
		
		return mapper.map(createUser, User.class);
	}

}
