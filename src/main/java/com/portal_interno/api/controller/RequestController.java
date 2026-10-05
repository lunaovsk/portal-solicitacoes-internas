package com.portal_interno.api.controller;

import com.portal_interno.api.domain.dto.request.RequestAttDTO;
import com.portal_interno.api.domain.dto.request.RequestDTO;
import com.portal_interno.api.domain.dto.request.RequestFilterDTO;
import com.portal_interno.api.domain.dto.request.RequestStatusUpdateDTO;
import com.portal_interno.api.domain.dto.response.RequestResponseDTO;
import com.portal_interno.api.service.RequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Solicitações", description = "Endpoints para gerenciamento de Solicitações")
@RestController
@RequestMapping("/api/v1/request")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @PostMapping
    @PreAuthorize("hasAuthority('SCOPE_ROLE_REQUESTER')")
    @Operation(summary = "Cadastrar uma nova solicitação", description = "Cria uma solicitação recebendo: Titulo, descrição e categoria")
    @ApiResponse(responseCode = "201", description = "Solicitação cadastrada com sucesso.")
    @ApiResponse(responseCode = "400", description = "Dados inválidos para cadastro.")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    @ApiResponse(responseCode = "500", description = "Erro inesperado.")
    public ResponseEntity<RequestResponseDTO> requestCreated(@Valid @RequestBody RequestDTO requestDTO, @AuthenticationPrincipal Jwt principal) {
        RequestResponseDTO response = requestService.createdRequest(requestDTO, principal.getSubject());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/filter")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_REQUESTER')")
    @Operation(summary = "Listar minhas solicitações", description = "Lista apenas as solicitações do usuário logado. Permite filtros.")
    public ResponseEntity<List<RequestResponseDTO>> listMyFilterRequests(@ModelAttribute RequestFilterDTO filter, @AuthenticationPrincipal Jwt principal) {
        List<RequestResponseDTO> response = requestService.listMyRequests(filter, principal.getSubject());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ATTENDANT')")
    @Operation(summary = "Listar todas as solicitações (Atendimento)", description = "Lista todas as solicitações do sistema. Exclusivo para atendentes. Permite filtros.")
    public ResponseEntity<List<RequestResponseDTO>> listAllRequests(@ModelAttribute RequestFilterDTO filter) {
        List<RequestResponseDTO> response = requestService.listFilterRequests(filter);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca de solicitação com detalhes", description = "Traz apenas a solicitação detalhada através do ID")
    public ResponseEntity<RequestResponseDTO> getRequestByIdAndUser(@PathVariable Long id, Authentication auth) {
        if (auth.getAuthorities().contains(new SimpleGrantedAuthority("SCOPE_ROLE_ATTENDANT"))) {
            RequestResponseDTO response = requestService.getRequestById(id);
            return ResponseEntity.ok(response);
        }
        RequestResponseDTO responseDTO = requestService.getRequestByIdAndUser(id, auth.getName());
        return ResponseEntity.ok(responseDTO);
    }

    @PutMapping("/{id}/update")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_REQUESTER')")
    public ResponseEntity<RequestResponseDTO> requestUpdate(@PathVariable Long id, @Valid @RequestBody RequestAttDTO requestAttDTO, @AuthenticationPrincipal Jwt principal) {
        RequestResponseDTO response = requestService.updateRequest(id, requestAttDTO, principal.getSubject());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_ATTENDANT')")
    @Operation(summary = "Alterar Status (Atendente)", description = "Permite ao atendente mudar o status da solicitação (Aberto, Em progresso e Concluído).")
    public ResponseEntity<RequestResponseDTO> updateStatus(@PathVariable Long id, @Valid @RequestBody RequestStatusUpdateDTO dto) {
        RequestResponseDTO response = requestService.updateRequestStatus(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAuthority('SCOPE_ROLE_REQUESTER')")
    @Operation(summary = "Excluir uma solicitação", description = "Exclui uma solicitação caso o usuário seja o dono e ela esteja com status OPEN")
    @ApiResponse(responseCode = "200", description = "Solicitação excluída com sucesso.")
    public ResponseEntity requestDelete(@PathVariable Long id, @AuthenticationPrincipal Jwt principal) {
        requestService.deleteRequest(id, principal.getSubject());
        return ResponseEntity.ok().build();
    }
}
