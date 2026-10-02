package com.portal_interno.api.domain.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
@Entity
@Table(name = "tb_user")
@NoArgsConstructor
@Getter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Username username;
    @Embedded
    private Password password;
    private boolean isActive;
    @Enumerated(EnumType.STRING)
    private Role role;

    public User (String username) {
        this.username = new Username(username);
    }

    public User (String username, String password,  Role role) {
        this.username = new Username(username);
        this.password = new Password(password);
        this.isActive = true;
        this.role = role;
    }

}
