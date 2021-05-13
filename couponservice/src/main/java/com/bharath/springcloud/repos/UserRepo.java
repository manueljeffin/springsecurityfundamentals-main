package com.bharath.springcloud.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bharath.springcloud.model.User;

public interface UserRepo extends JpaRepository<User, Long> {
	//Prefix with "findBy", followed by field name with first letter capitalized
	User findByEmail(String email);
}
