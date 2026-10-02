package com.portal_interno.api.controller;

import com.portal_interno.api.domain.dto.request.LoginRequestDTO;
import com.portal_interno.api.service.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureJsonTesters
class LoginControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<LoginRequestDTO> loginRequestDTOJacksonTester;

    @MockitoBean
    private AuthenticationManager manager;

    @MockitoBean
    private TokenService tokenService;

    @Test
    @DisplayName("Deveria devolver 200 e token quando login for bem sucedido")
    void loginCenarioSucesso() throws Exception {
        var authentication = new UsernamePasswordAuthenticationToken("teste@portalsolicitacao.com", null, List.of());
        when(manager.authenticate(any())).thenReturn(authentication);
        when(tokenService.generateToken(any())).thenReturn("token-jwt-mockado");

        var response = mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestDTOJacksonTester.write(
                                new LoginRequestDTO("teste@portalsolicitacao.com", "senha123")
                        ).getJson()))
                .andReturn().getResponse();

        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.getContentAsString()).contains("token-jwt-mockado");
    }

    @Test
    @DisplayName("Deveria devolver 403/401/400 quando credenciais forem inválidas")
    void loginCenarioCredenciaisInvalidas() throws Exception {
        doThrow(new BadCredentialsException("Credenciais invalidas"))
                .when(manager).authenticate(any());

        var response = mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequestDTOJacksonTester.write(
                                new LoginRequestDTO("teste@portalsolicitacao.com", "123-senha")
                        ).getJson()))
                .andReturn().getResponse();
        assertThat(response.getStatus()).isIn(
                HttpStatus.FORBIDDEN.value(), 
                HttpStatus.UNAUTHORIZED.value(), 
                HttpStatus.BAD_REQUEST.value()
        );
    }
}