package com.ndpar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestOperations;

import java.security.Principal;
import java.util.Map;

@EnableOAuth2Sso
@Controller
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
    public String callResourceServer(Model model) {
        Map resource = restTemplate().getForEntity("http://localhost:8888/me", Map.class).getBody();
        model.addAllAttributes(resource);
        return "resource";
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(ClientApplication.class).run(args);
    }
}