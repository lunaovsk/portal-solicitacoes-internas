package com.portal_interno.api.domain.dto.request;

import com.portal_interno.api.domain.model.request.RequestCategory;


public record RequestAttDTO (
        String title,
        String description,
        RequestCategory category
) {
}
