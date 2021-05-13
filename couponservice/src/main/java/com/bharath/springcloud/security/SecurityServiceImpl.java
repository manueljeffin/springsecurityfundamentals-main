package com.bharath.springcloud.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SecurityServiceImpl implements SecurityService {

	//We have defined our own (UserDetailsServiceImpl)
	@Autowired
	UserDetailsService userDetailsService;

	//In WebSecurityConfig we are exposing this
	@Autowired
	AuthenticationManager authenticationManger;

	@Override
	public boolean login(String userName, String password) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
		//userDetails.getAuthorities gives the roles for the user
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password,
				userDetails.getAuthorities());
		//Authenticates and sets token
		authenticationManger.authenticate(token);
		boolean result = token.isAuthenticated();

		if (result) {
			SecurityContextHolder.getContext().setAuthentication(token);
		}
		return result;
	}

}
