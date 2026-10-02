package com.portal_interno.api.infra.token;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RevokedRepository extends JpaRepository<RevokedToken, Long> {

    boolean existsByTokenJWT(String tokenJWT);
}
