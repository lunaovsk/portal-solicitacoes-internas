package com.portal_interno.api.domain.model.user;

import jakarta.persistence.Embeddable;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Locale;

@Embeddable
@NoArgsConstructor
@Getter
public class Username {
    private String username;

    public Username (String username) {
        if (username == null || !isValidEmail(username)) {
            throw new IllegalArgumentException("Invalid username format. Must be a valid email.");
        }
        this.username = username.toLowerCase(Locale.ROOT);
    }
    private boolean isValidEmail(String username) {
        String regex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return username.matches(regex);
    }
}
