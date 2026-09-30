package com.portal_interno.api.domain.repository;

import com.portal_interno.api.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
