package com.portal_interno.api.domain.model.request;


import com.portal_interno.api.domain.model.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "tb_request")
@NoArgsConstructor
@Getter
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
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Request (String title, String description, RequestCategory category, User userRequest) {
        validateTitle(title);
        validateDescription(description);
        validateCategory(category);
        this.title = title;
        this.description = description;
        this.category = category;
        this.status = RequestStatus.OPEN;
        this.createdAt = LocalDate.now();
        this.user = userRequest;
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank() || title.length() > 150) {
            throw new IllegalArgumentException("Title must contain between 1 and 150 characters.");
        }
    }
    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is required.");
        }
    }
    private void validateCategory(RequestCategory category) {
        if (category == null) {
            throw new IllegalArgumentException("Category is required.");
        }
    }

    public void editar(String title, String description, RequestCategory category) {
        validateTitle(title);
        validateDescription(description);
        validateCategory(category);
        this.title = title;
        this.description = description;
        this.category = category;
    }

    public void updateStatus(RequestStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException("Status cannot be null.");
        }
        this.status = newStatus;
    }
}
