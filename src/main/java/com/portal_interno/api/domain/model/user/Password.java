package com.portal_interno.api.domain.model.user;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class Password {
    private String password;

    public Password(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.password = password;
    }
}
