package com.portal_interno.api.infra.config;

import com.portal_interno.api.domain.model.user.Password;
import com.portal_interno.api.domain.model.user.Role;
import com.portal_interno.api.domain.model.user.Username;
import com.portal_interno.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Value("${attendant.bootstrap.email}")
    private String attendantEmail;

    @Value("${attendant.bootstrap.password}")
    private String attendantPassword;

    @Value("${requester.bootstrap.email}")
    private String requesterEmail;

    @Value("${requester.bootstrap.password}")
    private String requesterPassword;

    @Override
    public void run(String... args) {
        userService.createBootstrapUser(new Username(attendantEmail), new Password(attendantPassword), Role.ATTENDANT);
        userService.createBootstrapUser(new Username(requesterEmail), new Password(requesterPassword), Role.REQUESTER);
    }
}
