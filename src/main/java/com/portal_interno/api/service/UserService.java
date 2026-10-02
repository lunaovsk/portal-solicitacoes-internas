package com.portal_interno.api.service;

import com.portal_interno.api.domain.model.user.Password;
import com.portal_interno.api.domain.model.user.Role;
import com.portal_interno.api.domain.model.user.User;
import com.portal_interno.api.domain.model.user.Username;
import com.portal_interno.api.domain.repository.UserRepository;
import com.portal_interno.api.infra.token.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername_Username(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        return new UserDetailsImpl(user);
    }

    @Transactional
    public void createBootstrapUser(Username username, Password password, Role role) {
        if (!userRepository.existsByUsername_Username(username.getUsername())) {
            var user = new User(username.getUsername(), passwordEncoder.encode(password.getPassword()), role);
            userRepository.save(user);
        }
    }
}
