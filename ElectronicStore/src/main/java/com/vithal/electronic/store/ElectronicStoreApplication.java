package com.vithal.electronic.store;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vithal.electronic.store.entities.Role;
import com.vithal.electronic.store.repositories.RoleRepo;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private RoleRepo repo;

	@Value("${admin.role.id}")
	private String ROLE_ADMIN_ID;

	@Value("${normal.role.id}")
	private String ROLE_NORMAL_ID;

	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		String encodePassword = encoder.encode("viKLki@123");
		System.out.println(encodePassword);

		try {

			Role admin = Role.builder().roleId(ROLE_ADMIN_ID).roleName("ROLE_ADMIN").build();

			Role normal = Role.builder().roleId(ROLE_NORMAL_ID).roleName("ROLE_NORMAL").build();

			repo.save(admin);
			repo.save(normal);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
