package com.portal_interno.api.controller;

import com.portal_interno.api.domain.dto.response.DashboardResponseDTO;
import com.portal_interno.api.service.RequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Dashboard", description = "Indicadores e métricas do sistema")
@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {

    @Autowired
    private RequestService requestService;

    @GetMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_REQUESTER')")
    @Operation(summary = "Dashboard do Solicitante", description = "Retorna métricas das solicitações do usuário logado.")
    public ResponseEntity<DashboardResponseDTO> getMyDashboard(@AuthenticationPrincipal Jwt principal) {
        return ResponseEntity.ok(requestService.getDashboardForRequester(principal.getSubject()));
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ATTENDANT')")
    @Operation(summary = "Dashboard Global (Atendimento)", description = "Retorna as métricas de todo o sistema.")
    public ResponseEntity<DashboardResponseDTO> getGlobalDashboard() {
        return ResponseEntity.ok(requestService.getDashboardForAttendant());
    }
}
