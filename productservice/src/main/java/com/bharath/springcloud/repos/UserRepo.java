package com.bharath.springcloud.repos;

import com.bharath.springcloud.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
	//Prefix with "findBy", followed by field name with first letter capitalized
	User findByEmail(String email);
}
