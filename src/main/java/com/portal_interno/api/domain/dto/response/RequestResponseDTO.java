package com.portal_interno.api.domain.dto.response;

import com.portal_interno.api.domain.model.request.Request;
import com.portal_interno.api.domain.model.request.RequestCategory;
import com.portal_interno.api.domain.model.request.RequestStatus;

import java.time.LocalDate;

public record RequestResponseDTO(
        Long id,
        String title,
        String description,
        RequestCategory requestCategory,
        String username,
        LocalDate createdAt,
        RequestStatus requestStatus
) {
    public RequestResponseDTO(Request request) {
        this(
                request.getId(),
                request.getTitle(),
                request.getDescription(),
                request.getCategory(),
                request.getUser().getUsername().getUsername(),
                request.getCreatedAt(),
                request.getStatus()
        );
    }
}
