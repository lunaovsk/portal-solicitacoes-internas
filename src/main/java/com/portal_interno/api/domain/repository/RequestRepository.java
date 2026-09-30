package com.portal_interno.api.domain.repository;

import com.portal_interno.api.domain.model.request.Request;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Long> {

}
