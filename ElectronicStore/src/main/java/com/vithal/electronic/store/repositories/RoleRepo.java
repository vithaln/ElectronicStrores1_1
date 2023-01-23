package com.vithal.electronic.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vithal.electronic.store.entities.Role;

public interface RoleRepo extends JpaRepository<Role, String> {

}
