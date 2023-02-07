package com.vithal.electronic.store.services;

import java.util.List;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.vithal.electronic.store.dtos.PageableResponse;
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
		user = User.builder().name("Vithal").email("vnnivargi12@gmail.com")
				.about("This is Testing method for create Users").gender("Male").imageName("abc.png").password("abcd")
				.roles(Set.of(role)).build();

		roleId = "abc";
	}

	// create User
	@Test
	public void createUserTest() {

		Mockito.when(repository.save(Mockito.any())).thenReturn(user);

		Mockito.when(repo.findById(Mockito.anyString())).thenReturn(Optional.of(role));

		UserDto createUser = service.createUser(mapper.map(user, UserDto.class));

		Assertions.assertNotNull(createUser);
		System.out.println(createUser.getName() + "  " + createUser.getEmail());
		Assertions.assertEquals("Vithal", createUser.getName());

	}

	// update User

	@Test
	public void updateUser() {

		String userId = "hjg5434";

		UserDto userDto = UserDto.builder().name("Vithal Nivargi").about("This is Updated method for test Users")
				.gender("Male").imageName("cbd.png").build();

		Mockito.when(repository.findById(Mockito.anyString())).thenReturn(Optional.of(user));
		Mockito.when(repository.save(Mockito.any())).thenReturn(user);

		UserDto updateUser = service.updateUser(userDto, userId);
		System.out.println(updateUser.getName());

		Assertions.assertNotNull(updateUser);

		Assertions.assertEquals(userDto.getName(), updateUser.getName());
	}

	@Test
	public void deleteUserById() {
		String userId = "userid123";

		Mockito.when(repository.findById("userid123")).thenReturn(Optional.of(user));
		service.deleteUser(userId);

		Mockito.verify(repository, Mockito.times(1)).delete(user);

	}

	@Test
	public void getAllusersTest() {

		User user1 = User.builder().name("Mahesh").email("mahesh@gmail.com")
				.about("This is Testing method for create Users").gender("Male").imageName("abc.png").password("abcd")
				.roles(Set.of(role)).build();

		User user2 = User.builder().name("Subahsh").email("subahsh@gmail.com")
				.about("This is Testing method for create Users").gender("Male").imageName("abc.png").password("abcd")
				.roles(Set.of(role)).build();

		List<User> userLists = List.of(user, user1, user2);

		Page<User> page = new PageImpl<>(userLists);

		Mockito.when(repository.findAll((Pageable) Mockito.any())).thenReturn(page);

		PageableResponse<UserDto> allUser = service.getAllUser(1, 2, "name", "asc");

		Assertions.assertEquals(3, allUser.getContent().size());

	}

	@Test
	public void getSingleUser() {
		String userId = "userid1232";

		Mockito.when(repository.findById("userid1232")).thenReturn(Optional.of(user));

		UserDto userById = service.getUserById(userId);

		Assertions.assertNotNull(userById);
		Assertions.assertEquals(user.getName(), userById.getName());
	}

	@Test
	public void getUserByEmail() {
		String userEmail = "vnnivargi12@gmail.com";

		Mockito.when(repository.findByEmail("vnnivargi12@gmail.com")).thenReturn(Optional.of(user));

		UserDto userByEmail = service.getUserByEmail(userEmail);

		Assertions.assertNotNull(userByEmail);
		Assertions.assertEquals(user.getName(), userByEmail.getName());
	}

	@Test
	public void searchUserByKeywordTest() {

		User user1 = User.builder().name("Mahesh").email("mahesh@gmail.com")
				.about("This is Testing method for create Users").gender("Male").imageName("abc.png").password("abcd")
				.roles(Set.of(role)).build();

		User user2 = User.builder().name("Subahsh").email("subahsh@gmail.com")
				.about("This is Testing method for create Users").gender("Male").imageName("abc.png").password("abcd")
				.roles(Set.of(role)).build();

		User user = User.builder().name("Mahesh").email("mahesh@gmail.com")
				.about("This is Testing method for create Users").gender("Male").imageName("abc.png").password("abcd")
				.roles(Set.of(role)).build();

		User user3 = User.builder().name("Sanvi").email("sanvi@gmail.com")
				.about("This is Testing method for create Users").gender("Male").imageName("abc.png").password("abcd")
				.roles(Set.of(role)).build();

		String keyword = "vithal";

		Mockito.when(repository.findByNameContaining(keyword))
				.thenReturn(java.util.Arrays.asList(user, user1, user2, user3));
		List<UserDto> searchUser = service.searchUser(keyword);

		Assertions.assertEquals(4, searchUser.size());

	}
}
