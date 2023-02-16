package com.vithal.electronic.store.services.impl;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import com.vithal.electronic.store.dtos.ApiResponseMessage;
import com.vithal.electronic.store.dtos.PageableResponse;
import com.vithal.electronic.store.dtos.UserDto;
import com.vithal.electronic.store.entities.Role;
import com.vithal.electronic.store.entities.User;
import com.vithal.electronic.store.exceptions.ResourceNotFoundException;
import com.vithal.electronic.store.helper.Helper;
import com.vithal.electronic.store.repositories.RoleRepo;
import com.vithal.electronic.store.repositories.UserRepository;
import com.vithal.electronic.store.services.UserService;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ModelMapper mapper;

	@Value("${user.profile.image.path}")
	private String imagePath;

	@Autowired
	private PasswordEncoder encoder;
	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Value("${normal.role.id}")
	private String ROLE_NORMAL_ID;

	@Autowired
	private RoleRepo repo;

	@Override
	public UserDto createUser(UserDto userDto) {

		// generate unique id in string format
		String userId = UUID.randomUUID().toString();
		userDto.setUserId(userId);
		// encode password
		userDto.setPassword(encoder.encode(userDto.getPassword()));
		// dto->entity
		User user = dtoToEntity(userDto);
		// fetch role from db
		Role role = repo.findById(ROLE_NORMAL_ID).get();
		user.getRoles().add(role);
		User savedUser = userRepository.save(user);
		// entity -> dto
		UserDto newDto = entityToDto(savedUser);
		return newDto;
	}

	@Override
	public UserDto updateUser(UserDto userDto, String userId) {

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given id !!"));
		user.setName(userDto.getName());
		// email update
		user.setAbout(userDto.getAbout());
		user.setGender(userDto.getGender());
		user.setPassword(userDto.getPassword());
		user.setImageName(userDto.getImageName());

		// save data
		User updatedUser = userRepository.save(user);
		UserDto updatedDto = entityToDto(updatedUser);
		return updatedDto;
	}

	@Override
	public void deleteUser(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given id !!"));

		// delete user profile image
		// images/user/abc.png
		String fullPath = imagePath + user.getImageName();

		try {
			Path path = Paths.get(fullPath);
			Files.delete(path);
		} catch (NoSuchFileException ex) {
			logger.info("User image not found in folder");
			ex.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// delete user
		userRepository.delete(user);

	}

	@Override
	public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {

		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

//        pageNumber default starts from 0
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<User> page = userRepository.findAll(pageable);

		PageableResponse<UserDto> response = Helper.getPageableResponse(page, UserDto.class);

		return response;
	}

	@Override
	public UserDto getUserById(String userId) {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("user not found with given id !!"));
		return entityToDto(user);
	}

	@Override
	public UserDto getUserByEmail(String email) {
		User user = userRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with given email id !!"));
		return entityToDto(user);
	}

	@Override
	public List<UserDto> searchUser(String keyword) {
		List<User> users = userRepository.findByNameContaining(keyword);
		List<UserDto> dtoList = users.stream().map(user -> entityToDto(user)).collect(Collectors.toList());
		return dtoList;
	}

	private UserDto entityToDto(User savedUser) {

//        UserDto userDto = UserDto.builder()
//                .userId(savedUser.getUserId())
//                .name(savedUser.getName())
//                .email(savedUser.getEmail())
//                .password(savedUser.getPassword())
//                .about(savedUser.getAbout())
//                .gender(savedUser.getGender())
//                .imageName(savedUser.getImageName())
//                .build();

		return mapper.map(savedUser, UserDto.class);

	}

	private User dtoToEntity(UserDto userDto) {
//        User user = User.builder()
//                .userId(userDto.getUserId())
//                .name(userDto.getName())
//                .email(userDto.getEmail())
//                .password(userDto.getPassword())
//                .about(userDto.getAbout())
//                .gender(userDto.getGender())
//                .imageName(userDto.getImageName())
//                .build();

		return mapper.map(userDto, User.class);
	}

	@Override
	public Optional<User> findUserByEmailOptional(String email) {

		Optional<User> user = userRepository.findByEmail(email);
		return user;
	}

	@Override
	public UserDto updatePatchFields(String userId, Map<String, Object> fields) {

		User existUser = userRepository.findById(userId).orElseThrow(()-> new ResourceNotFoundException("User not found with this "+userId));
		
		
		
		fields.forEach((key,value)->{
			
			Field field = ReflectionUtils.findField(User.class, key);//we should give entity class here, not a DTO class.
			field.setAccessible(true);
			
			ReflectionUtils.setField(field, existUser, value);
			
		});
		
	User updateFields = userRepository.save(existUser);
		UserDto map = mapper.map(updateFields, UserDto.class);
		return map;
		

		
	}
}
