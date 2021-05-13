package com.bharath.springcloud.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;

//Need spring-cloud-starter-oauth2 library for this
//Responsible for receiving the token generated through AuthorizationServerConfig, validating it and then giving access to appropriate urls
//@Configuration
//@EnableResourceServer
public class ResouceServerConfig extends ResourceServerConfigurerAdapter {
    private static final String RESOURCE_ID = "couponservice"; //Link between authorizationserver and resourceserver

    //Will be used by resource server to validate the token
//    @Value("${publicKey}")
//    private              String    publicKey;

    //Have to define which resource it's protecting
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID)
        .tokenStore(tokenStore()); //relevant only for symmetric key
    }

    //Not Web app, this is restful API protection
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
            .mvcMatchers(HttpMethod.GET, "/couponapi/coupons/{code:^[A-Z]*$}")
            .hasAnyRole("USER", "ADMIN")
            .mvcMatchers(HttpMethod.POST, "/couponapi/coupons")
            .hasRole("ADMIN")
            .anyRequest()
            .denyAll()
            .and()
            .csrf()
            .disable();

    }

    //Since in config we have told how to get public key, spring will take care of pulling key and validating the token. So we can comment out below two methods in case of asymmetric key.
    @Bean
    public TokenStore tokenStore() {

        //In Memory Token store
        //return new InMemoryTokenStore();

        //In db
        //return new JdbcTokenStore(dataSource);

        //JWT token store
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        //public key
        //jwtAccessTokenConverter.setVerifierKey(publicKey);

        //For symmetric key
        jwtAccessTokenConverter.setSigningKey("testkey");
        return jwtAccessTokenConverter;

    }

}
