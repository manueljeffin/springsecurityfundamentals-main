package com.bharath.springcloud.security.config;

import com.bharath.springcloud.security.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic();
        http.authorizeRequests()
            //.mvcMatchers(HttpMethod.GET, "/productapi/products")
            //.hasAnyRole("ADMIN", "USER")
            //            .mvcMatchers(HttpMethod.GET, "/showCreateCoupon", "/createCoupon", "/createResponse").hasRole("ADMIN")
            //            .mvcMatchers(HttpMethod.POST, "/getCoupon").hasAnyRole("USER", "ADMIN")
            //.mvcMatchers(HttpMethod.POST, "/productapi/products").hasRole("ADMIN")
            //				.mvcMatchers("/", "/login", "/logout", "/showReg", "/registerUser").permitAll()
            //				.and().logout().logoutSuccessUrl("/");

            .anyRequest().permitAll() //To not match strictly, but still authenticate by role
            .and().csrf().disable();


    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //    @Override
    //    @Bean
    //    public AuthenticationManager authenticationManagerBean() throws Exception {
    //        return super.authenticationManagerBean();
    //    }

}
