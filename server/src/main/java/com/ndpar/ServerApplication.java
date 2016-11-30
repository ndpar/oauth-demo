package com.ndpar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

/**
 * Step 1a. Authenticate user
 * Step 1b. Authorize client

http://localhost:8080/oauth/authorize?response_type=code&client_id=my-trusted-client&state=xyz&scope=read%20write&redirect_uri=http%3A%2F%2Flocalhost%3A9999%2Fclient

 * Step 2. Exchange authorization code with access token. Copy code from
 * the previous response

curl -v -X POST -H "Content-Type:application/x-www-form-urlencoded" \
-u my-trusted-client:my-trusted-client-pass \
-d grant_type=authorization_code \
-d code=dY1WNj \
-d redirect_uri=http%3A%2F%2Flocalhost%3A9999%2Fclient \
-d client_id=my-trusted-client \
http://localhost:8080/oauth/token

 * Step 3. Verify access token. Copy token from the previous response

curl -v -X POST -H "Content-Type:application/x-www-form-urlencoded" \
-u my-trusted-client:my-trusted-client-pass \
-d token=9a20a810-5299-49dc-a10b-425c30917a3e \
http://localhost:8080/oauth/check_token

 */
@SpringBootApplication
public class ServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Autowired
    private DataSource dataSource;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder);
    }


    @Configuration
    @EnableAuthorizationServer
    protected static class OAuth2Server extends AuthorizationServerConfigurerAdapter {

        @Autowired
        private AuthenticationManager auth;

        @Autowired
        private DataSource dataSource;

        private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        @Bean
        JdbcTokenStore tokenStore() {
            return new JdbcTokenStore(dataSource);
        }

        @Bean
        AuthorizationCodeServices authorizationCodeServices() {
            return new JdbcAuthorizationCodeServices(dataSource);
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
            security
                    .passwordEncoder(passwordEncoder)
                    .checkTokenAccess("isAuthenticated()");
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .authorizationCodeServices(authorizationCodeServices())
                    .authenticationManager(auth)
                    .tokenStore(tokenStore())
                    .approvalStoreDisabled();
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.jdbc(dataSource).passwordEncoder(passwordEncoder);
        }
    }
}
