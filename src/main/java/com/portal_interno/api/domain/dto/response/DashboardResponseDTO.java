package com.portal_interno.api.domain.dto.response;

public record DashboardResponseDTO (
        long qntTotalRequest,
        long qntStatusOpen,
        long qntInProgress,
        long qntCompleted
) {
}
