package com.portal_interno.api.domain.model.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
@Entity
@Table(name = "tb_user")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Username username;
    @Embedded
    private Password password;
    private boolean isActive;
    private LocalDateTime expireAt;
    @Enumerated(EnumType.STRING)
    private Role role;

}
