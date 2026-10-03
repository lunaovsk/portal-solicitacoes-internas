package com.portal_interno.api.domain.dto.request;

import com.portal_interno.api.domain.model.request.RequestCategory;
import com.portal_interno.api.domain.model.request.RequestStatus;

import java.time.LocalDate;

public record RequestFilterDTO(
        RequestCategory category,
        RequestStatus status,
        String title,
        LocalDate startDate,
        LocalDate endDate
) {
}
