package com.portal_interno.api.domain.model.request;


import com.portal_interno.api.domain.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "tb_request")
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String description;
    @Enumerated(EnumType.STRING)
    private RequestCategory category;
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    private LocalDate createdAt;
    @ManyToOne
    private User user;
}
