package com.portal_interno.api.service;

import com.portal_interno.api.infra.token.RevokedRepository;
import com.portal_interno.api.infra.token.RevokedToken;
import com.portal_interno.api.infra.token.TokenJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class TokenService {

    @Autowired
    private JwtEncoder encoder;
    @Autowired
    private RevokedRepository revokedRepository;

    public Instant expirationToken() {
        return Instant.now().plusSeconds(3600);
    }

    public String generateToken(Authentication authentication) {
        String scopes = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        var claims = JwtClaimsSet.builder()
                .issuer("portal-solicitacao-back")
                .issuedAt(Instant.now())
                .expiresAt(expirationToken())
                .subject(authentication.getName())
                .claim("scope", scopes)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    public void validateToken(TokenJWT token) {
        if (revokedRepository.existsByTokenJWT(token.tokenJwt())) {
            throw new BadJwtException("Token revogado");
        }
    }

    @Transactional
    public void saveToken(String jwt) {
        var rvk = new RevokedToken(jwt);
        revokedRepository.save(rvk);
    }
}