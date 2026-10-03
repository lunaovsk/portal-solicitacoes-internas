package com.portal_interno.api.domain.dto.request;

import com.portal_interno.api.domain.model.request.RequestStatus;
import jakarta.validation.constraints.NotNull;

public record RequestStatusUpdateDTO(
        @NotNull
        RequestStatus status
) {}
