package com.portal_interno.api.domain.repository;

import com.portal_interno.api.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername_Username(String username);

    boolean existsByUsername_Username(String username);
}
