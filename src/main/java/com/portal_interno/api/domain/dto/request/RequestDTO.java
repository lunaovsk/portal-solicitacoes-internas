package com.portal_interno.api.domain.dto.request;

import com.portal_interno.api.domain.model.request.RequestCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RequestDTO(
        @NotBlank
        @Size(max = 150)
        String title,
        @NotBlank
        String description,
        @NotNull
        RequestCategory category
) {
}
