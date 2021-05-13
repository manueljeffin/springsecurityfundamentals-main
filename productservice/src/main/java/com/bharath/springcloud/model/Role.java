package com.bharath.springcloud.model;


import org.springframework.security.core.GrantedAuthority;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Role implements GrantedAuthority {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	//We are telling mapping already happened in User.java
	@ManyToMany(mappedBy = "roles") //field name in User.java
	private Set<User> users;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	//Will be invoked internally internally by string security and it should return a role
	@Override
	public String getAuthority() {
		return name;
	}

}
