package com.portal_interno.api.service;

import com.portal_interno.api.domain.dto.request.RequestDTO;
import com.portal_interno.api.domain.dto.response.RequestResponseDTO;
import com.portal_interno.api.domain.model.request.Request;
import com.portal_interno.api.domain.repository.RequestRepository;
import com.portal_interno.api.domain.repository.UserRepository;
import com.portal_interno.api.infra.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public RequestResponseDTO createdRequest(RequestDTO requestDTO, String username) {
        var user = userRepository.findByUsername_Username(username)
                .orElseThrow(() -> CustomException.userNotFound(username));
        Request request = new Request(requestDTO.title(), requestDTO.description(), requestDTO.category(), user);
        Request created = requestRepository.save(request);
        return new RequestResponseDTO(created);
    }
}
