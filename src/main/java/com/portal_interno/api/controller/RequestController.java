package com.portal_interno.api.controller;

import com.portal_interno.api.domain.dto.request.RequestDTO;
import com.portal_interno.api.domain.dto.response.RequestResponseDTO;
import com.portal_interno.api.service.RequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Solicitações", description = "Endpoints para gerenciamento de Solicitações")
@RestController
@RequestMapping("/request")
public class RequestController {

    @Autowired
    private RequestService requestService;

    @PostMapping
    @Operation(summary = "Cadastrar uma nova solicitação", description = "Cria uma solicitação recebendo: Titulo, descrição e categoria")
    @ApiResponse(responseCode = "201", description = "Solicitação cadastrada com sucesso.")
    @ApiResponse(responseCode = "400", description = "Dados inválidos para cadastro.")
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado.")
    @ApiResponse(responseCode = "500", description = "Erro inesperado.")
    public ResponseEntity<RequestResponseDTO> requestCreated(@Valid @RequestBody RequestDTO requestDTO, @AuthenticationPrincipal UserDetails principal) {
        RequestResponseDTO response = requestService.createdRequest(requestDTO, principal.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
