package com.portal_interno.api.controller;


import com.portal_interno.api.domain.dto.request.LoginRequestDTO;
import com.portal_interno.api.infra.token.TokenJWT;
import com.portal_interno.api.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticação", description = "Endpoint de login ")
public class LoginController {

    @Autowired
    private AuthenticationManager manager;
    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    @Operation(summary = "Login no portal", description = "Realiza autenticação e retorna um token JWT.")
    @ApiResponse(responseCode = "200", description = "Login efetuado com sucesso!")
    @ApiResponse(responseCode = "500", description = "Erro inesperado.")
    public ResponseEntity<TokenJWT> login(@RequestBody @Valid LoginRequestDTO dto) {
        var credentials = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
        var authentication = manager.authenticate(credentials);
        var token = tokenService.generateToken(authentication);
        return ResponseEntity.ok(new TokenJWT(token));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout do portal", description = "Revoga o JWT apresentado nesta requisição.")
    @ApiResponse(responseCode = "204", description = "Logout efetuado com sucesso.")
    public ResponseEntity logout(@AuthenticationPrincipal Jwt principal) {
        tokenService.saveToken(principal.getTokenValue());
        return ResponseEntity.noContent().build();
    }
}
