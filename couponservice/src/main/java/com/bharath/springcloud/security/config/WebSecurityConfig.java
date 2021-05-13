package com.bharath.springcloud.security.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.bharath.springcloud.security.UserDetailsServiceImpl;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;

@Configuration
//To enable method level security
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http.httpBasic(); => If postman based
        //http.formLogin(); //=> If UI Based and we don't have custom form
//        http.authorizeRequests()
//            .mvcMatchers(HttpMethod.GET, "/couponapi/coupons/{code:^[A-Z]*$}", "/showGetCoupon",
//                         "/getCoupon", "/couponDetails")
//            .hasAnyRole("USER", "ADMIN")
//            .mvcMatchers(HttpMethod.GET, "/showCreateCoupon", "/createCoupon", "/createResponse").hasRole("ADMIN")
//            .mvcMatchers(HttpMethod.POST, "/getCoupon").hasAnyRole("USER", "ADMIN")
//            .mvcMatchers(HttpMethod.POST, "/couponapi/coupons", "/saveCoupon", "/getCoupon").hasRole("ADMIN")
//            .mvcMatchers("/", "/login", "/logout", "/showReg", "/registerUser")
//            .permitAll()
//
//            .anyRequest().denyAll()             //Telling to strictly pattern match the url.
//            //.and().csrf().disable()
//            .and()
//            //Cute way of handling logout. No need endpoints mapping it(ofcourse can configure if needed).
//            .logout().logoutSuccessUrl("/");
//
//        //Custom csrf configuring. Here we are just ignoring all paths
//        http.csrf(csrfCustomizer -> {
//            RequestMatcher requestMatcherPost = new RegexRequestMatcher("/*", "POST");
//            RequestMatcher requestMatcherGet = new RegexRequestMatcher("/*", "GET");
//            csrfCustomizer.ignoringRequestMatchers(requestMatcherPost, requestMatcherGet);
//        });

        //Easiest way to switch off all authentication
        http.authorizeRequests().antMatchers("/**").permitAll();


    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //We are exposing AuthenticationManager so that it can be used in SecurityServiceImpl
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
