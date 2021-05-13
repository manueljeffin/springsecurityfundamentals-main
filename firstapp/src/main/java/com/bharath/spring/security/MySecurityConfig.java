package com.bharath.spring.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MyAuthenticationProvider authenticationProvider;

//    	@Override
//    	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//    		InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
//    		UserDetails user = User.withUsername("tom").password(passwordEncoder.encode("cruise")).authorities("read").build();
//    		userDetailsService.createUser(user);
//
//    		auth.userDetailsService(userDetailsService);
//    	}

	//Custom Authentication Provider
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider);
    }

    //Here is where we define the type of login(authentication method)
    // and also we can give regex for what type of urls to be
    // authenticated
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //Defining the authentication method
        http.httpBasic();

        //Have to go to browser for this
        //http.formLogin();

        //We are telling to authenticate any request which comes in
        http.authorizeRequests().anyRequest().authenticated();

        //Those who access /hello should be authenticated
        //http.authorizeRequests().antMatchers("/hello").authenticated();

        //Custom filter
        http.addFilterBefore(new MySecurityFilter(), BasicAuthenticationFilter.class);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
