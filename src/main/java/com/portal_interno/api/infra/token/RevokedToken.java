package com.portal_interno.api.infra.token;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name="tb_auth")
@NoArgsConstructor
public class RevokedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String tokenJWT;
    private LocalDateTime expiresAt;

}
