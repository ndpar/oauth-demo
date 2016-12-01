package com.ndpar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;

import java.security.Principal;

@EnableOAuth2Sso
@RestController
@SpringBootApplication
public class ClientApplication {

    @RequestMapping("/")
    public String home(Principal user) {
        return "Hello " + user.getName();
    }

    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    @Autowired
    private OAuth2ProtectedResourceDetails resourceDetails;

    @Bean
    public RestOperations restTemplate() {
        return new OAuth2RestTemplate(resourceDetails, oauth2ClientContext);
    }

    @RequestMapping("/resource")
    public String callResourceServer() {
        return restTemplate().getForEntity("http://localhost:8888/me", String.class).toString();
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(ClientApplication.class).run(args);
    }
}