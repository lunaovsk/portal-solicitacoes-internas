package com.portal_interno.api.domain.repository;

import com.portal_interno.api.domain.model.request.Request;
import com.portal_interno.api.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.portal_interno.api.domain.model.request.RequestCategory;
import com.portal_interno.api.domain.model.request.RequestStatus;
import java.time.LocalDate;
import java.util.List;
@Repository
public interface RequestRepository extends JpaRepository<Request, Long> {

    @Query("SELECT r FROM Request r WHERE (:title IS NULL OR r.title LIKE CONCAT('%', :title, '%')) " +
            "AND (:category IS NULL OR r.category = :category) " +
            "AND (:status IS NULL OR r.status = :status) " +
            "AND (:startDate IS NULL OR r.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR r.createdAt <= :endDate) " +
            "AND (:username IS NULL OR r.user.username.username = :username)")
    List<Request> findFilterRequest(String title, RequestCategory category, RequestStatus status, LocalDate startDate, LocalDate endDate, String username);

    @Query("SELECT r FROM Request r WHERE (:title IS NULL OR r.title LIKE CONCAT('%', :title, '%')) " +
            "AND (:category IS NULL OR r.category = :category) " +
            "AND (:status IS NULL OR r.status = :status) " +
            "AND (:startDate IS NULL OR r.createdAt >= :startDate) " +
            "AND (:endDate IS NULL OR r.createdAt <= :endDate) ")
    List<Request> findFilterRequest(String title, RequestCategory category, RequestStatus status, LocalDate startDate, LocalDate endDate);
    Request findByIdAndStatus(Long id, RequestStatus status);
    Request findByIdAndUser(Long id, User user);
    /* Dashboard */

    long count();
    long countByStatus(RequestStatus status);

    /* Dashboard para colaboradores */

    long countByUser(User user);
    long countByUserAndStatus(User user, RequestStatus status);

}
