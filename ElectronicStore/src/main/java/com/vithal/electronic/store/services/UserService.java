package com.vithal.electronic.store.services;

import java.util.List;

import com.vithal.electronic.store.dtos.PageableResponse;
import com.vithal.electronic.store.dtos.UserDto;
import com.vithal.electronic.store.entities.User;

public interface UserService {

    //create
    UserDto createUser(UserDto userDto);


    //update
    UserDto updateUser(UserDto userDto, String userId);

    //delete

    void deleteUser(String userId);


    //get all users
    PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

    //get single user by id
    UserDto getUserById(String userId);

    //get  single user by email
    UserDto getUserByEmail(String email);

    //search user
    List<UserDto> searchUser(String keyword);

    //other user specific features

}
