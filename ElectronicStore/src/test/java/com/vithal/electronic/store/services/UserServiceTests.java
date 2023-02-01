package com.vithal.electronic.store.services;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.vithal.electronic.store.dtos.UserDto;
import com.vithal.electronic.store.entities.Role;
import com.vithal.electronic.store.entities.User;
import com.vithal.electronic.store.repositories.RoleRepo;
import com.vithal.electronic.store.repositories.UserRepository;

@SpringBootTest
public class UserServiceTests {
	
	
	@MockBean
	private UserRepository repository;
	@MockBean
	private RoleRepo repo;
	
	@Autowired
	private UserService service;
	
	@Autowired
	private ModelMapper mapper;
	
	User user;
	Role role;
	
	String roleId;
	
	@BeforeEach
	public void init() {
		
		 role = Role.builder().roleId("abc").roleName("NORMAL").build();
		 user = User.builder()
		.name("Vithal")
		.email("vnnivargi12@gmail.com")
		.about("This is Testing method for create Users")
		.gender("Male")
		.imageName("abc.png")
		.password("abcd")
		.roles(Set.of(role)).build();
		
		
		roleId="abc";
	}
	
	//create User
	@Test
	public void createUserTest() {
		
		Mockito.when(repository.save(Mockito.any())).thenReturn(user);
		
		Mockito.when(repo.findById(Mockito.anyString())).thenReturn(Optional.of(role));
		
		UserDto createUser = service.createUser(mapper.map(user, UserDto.class));
		
		
		
		Assertions.assertNotNull(createUser);
	System.out.println(createUser.getName()+"  "+createUser.getEmail());
		Assertions.assertEquals("Vithal", createUser.getName());
		
	}
	
	
	//update User
	
	@Test
	public void updateUser() {
		
		String userId="hjg5434";
		
		UserDto userDto = UserDto.builder()
		.name("Vithal Nivargi")
		.about("This is Updated method for test Users")
		.gender("Male")
		.imageName("cbd.png")
		.build();
		
		
		Mockito.when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		Mockito.when(repository.save(Mockito.any())).thenReturn(user);
		
		UserDto updateUser = service.updateUser(userDto, userId);
		System.out.println(updateUser.getName());
		
		Assertions.assertNotNull(updateUser);
		
		Assertions.assertEquals(userDto.getName(), updateUser.getName());
	}
	

}
