package com.portal_interno.api.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDTO(
        @NotBlank
        @Email
        String username,
        @NotBlank
        String password
) {
}
