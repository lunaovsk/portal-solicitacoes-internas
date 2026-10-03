package com.portal_interno.api.service;

import com.portal_interno.api.domain.dto.request.RequestAttDTO;
import com.portal_interno.api.domain.dto.request.RequestDTO;
import com.portal_interno.api.domain.dto.request.RequestFilterDTO;
import com.portal_interno.api.domain.dto.request.RequestStatusUpdateDTO;
import com.portal_interno.api.domain.dto.response.DashboardResponseDTO;
import com.portal_interno.api.domain.dto.response.RequestResponseDTO;
import com.portal_interno.api.domain.model.request.Request;
import com.portal_interno.api.domain.model.request.RequestStatus;
import com.portal_interno.api.domain.repository.RequestRepository;
import com.portal_interno.api.domain.repository.UserRepository;
import com.portal_interno.api.infra.exception.CustomException;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<RequestResponseDTO> listMyRequests(RequestFilterDTO filter, String usernameLogado) {
        java.util.List<Request> requests = requestRepository
                .findFilterRequest(
                filter.title(), filter.category(), filter.status(), filter.startDate(), filter.endDate(), usernameLogado);
        return requests.stream().map(RequestResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public List<RequestResponseDTO> listAllRequests(RequestFilterDTO filter) {
        List<Request> requests = requestRepository.findFilterRequest(
                filter.title(), filter.category(), filter.status(), filter.startDate(), filter.endDate());
        return requests.stream().map(RequestResponseDTO::new).toList();
    }

    @Transactional(readOnly = true)
    public RequestResponseDTO getRequestById(Long id) {
        var req = requestRepository.findById(id).orElseThrow(() -> CustomException.requestNotFound(id));
        return new RequestResponseDTO(req);
    }

    @Transactional
    public RequestResponseDTO createdRequest(RequestDTO requestDTO, String username) {
        var user = userRepository.findByUsername_Username(username).orElseThrow(() -> CustomException.userNotFound(username));
        Request request = new Request(requestDTO.title(), requestDTO.description(), requestDTO.category(), user);
        Request created = requestRepository.save(request);
        return new RequestResponseDTO(created);
    }

    @Transactional
    public RequestResponseDTO updateRequest(Long id, RequestAttDTO dto, String username) {
        Request request = findAndValidateRequest(id, username);
        request.editar(dto.title(), dto.description(), dto.category());
        Request savedRequest = requestRepository.save(request);
        return new RequestResponseDTO(savedRequest);
    }

    @Transactional
    public RequestResponseDTO updateRequestStatus(Long id, RequestStatusUpdateDTO dto) {
        Request request = requestRepository.findById(id)
                .orElseThrow(() -> CustomException.requestNotFound(id));
        request.updateStatus(dto.status());
        Request savedRequest = requestRepository.save(request);
        return new RequestResponseDTO(savedRequest);
    }

    @Transactional
    public void deleteRequest(Long id, String username) {
        Request request = findAndValidateRequest(id, username);
        requestRepository.delete(request);
    }

    private Request findAndValidateRequest(Long id, String user) {
        var request = requestRepository.findByIdAndStatus(id, RequestStatus.OPEN);
        if (request == null) {
            throw CustomException.requestNotFound(id);
        }
        validateOwnership(request, user);
        return request;
    }

    private void validateOwnership(Request request, String user) {
        if (!request.getUser().getUsername().getUsername().equals(user)) {
            throw CustomException.accessDenied("Acesso negado");
        }
    }

    public DashboardResponseDTO getDashboardForRequester(@Nullable String username) {
        var userId = userRepository.findByUsername_Username(username).orElseThrow(() -> CustomException.userNotFound(username));
        Map<RequestStatus, Long> map = new EnumMap<>(RequestStatus.class);
        long totalRequest = requestRepository.countByUser(userId.getId());
        for (RequestStatus status : RequestStatus.values()) {
            long count = requestRepository.countByStatus(userId.getId(), status);
            map.put(status, count);
        }

        var dash = new DashboardResponseDTO(totalRequest, map.getOrDefault(RequestStatus.OPEN, 0L), map.getOrDefault(RequestStatus.IN_PROGRESS, 0L), map.getOrDefault(RequestStatus.COMPLETED, 0L));
        return dash;
    }

    public DashboardResponseDTO getDashboardForAttendant() {
        Map<RequestStatus, Long> map = new EnumMap<>(RequestStatus.class);
        long totalRequest = requestRepository.count();
        for (RequestStatus status : RequestStatus.values()) {
            long count = requestRepository.countByStatus(status);
            map.put(status, count);
        }
        var dash = new DashboardResponseDTO(totalRequest, map.getOrDefault(RequestStatus.OPEN, 0L), map.getOrDefault(RequestStatus.IN_PROGRESS, 0L), map.getOrDefault(RequestStatus.COMPLETED, 0L));
        return dash;
    }
}
