package com.portal_interno.api.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class UserControllerTest {

    @Test
    @DisplayName("Deveria devolver 200 e token quando login for bem sucedido")
    void loginCenarioSucesso() throws Exception {
        var authentication = new UsernamePasswordAuthenticationToken("teste@email.com", null, List.of());
        when(manager.authenticate(any())).thenReturn(authentication);
        when(tokenService.generateToken(any())).thenReturn("token-jwt-mockado");

        var response = mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestDTOJacksonTester.write(
                                new UserRequestDTO("teste@email.com", "senha123")
                        ).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("token-jwt-mockado");
    }

    @Test
    @DisplayName("Deveria devolver 400 quando credenciais forem inválidas")
    void loginCenarioCredenciaisInvalidas() throws Exception {
        doThrow(new BadCredentialsException("Credenciais inválidas"))
                .when(manager).authenticate(any());

        var response = mvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestDTOJacksonTester.write(
                                new UserRequestDTO("teste@email.com", "senha-errada")
                        ).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}