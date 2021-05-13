package com.bharath.springcloud.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;

import java.security.KeyPair;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
//Need spring-cloud-starter-oauth2 library for this
//Responsible for authenticating and generating the token.
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    private static final String RESOURCE_ID = "couponservice";

    //Exposed by OAuth2SecurityConfig.java
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    //Exposed by OAuth2SecurityConfig.java
    @Autowired
    private PasswordEncoder passwordEncoder;

    //mysql defined in application.properties (spring.datasources section)
    @Autowired
    private DataSource dataSource;

    @Value("${keyFile}")
    private String keyFile;
    @Value("${password}")
    private String password;
    @Value("${alias}")
    private String alias;


    //IN-MEMORY
    //Stores token in token store, and send it back to user, who uses it to hit resource(ResourceServer config, which does authorization)
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore())
                 .accessTokenConverter(jwtAccessTokenConverter()) //Relevant only for JWT
                 .authenticationManager(authenticationManager)
                 .userDetailsService(userDetailsService);
    }

    //Authenticates two details based on grant type in OAuth, client details and user details
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
               .withClient("couponclientapp")
               .secret(passwordEncoder.encode("9999"))
               .authorizedGrantTypes("authorization_code", "password", "refresh_token")
               .scopes("read", "write")
               .resourceIds(RESOURCE_ID) //Link between authorizationserver and resourceserver
               .redirectUris("http://locahost:9091/codeHandlerPage") //applicable only for authorization_code type where user doesn't give password directly to client_app, rather gives to authorization server directly. Have to hit (http://localhost:9092/oauth/authorize?response_type=code&client_id=couponclientapp&scopre=read)
        ;
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()");
    }


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

        //For asymmetric key
        //        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource(keyFile),
        //                                                                       password.toCharArray());
        //        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias);
        //        jwtAccessTokenConverter.setKeyPair(keyPair);

        //For symmetric key
        jwtAccessTokenConverter.setSigningKey("testkey");

        return jwtAccessTokenConverter;

    }

}

