package com.scarano.dev.auth.security.config;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenServico implements Serializable {

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
    public static final String ROLES = "ROLES";

    @Value("${jwt.secret}")
    private String secret;

    public String obterLoginToken(String token) {
        return obterClaimToken(token, Claims::getSubject);
    }

    public Date obterDataExpiracaoToken(String token) {
        return obterClaimToken(token, Claims::getExpiration);
    }

    public List<String> obterRoles(String token) {
        return obterClaimToken(token, claims -> (List) claims.get(ROLES));
    }

    public <T> T obterClaimToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = obterTodasClaimsToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims obterTodasClaimsToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean ehTokenExpirado(String token) {
        final Date expiration = obterDataExpiracaoToken(token);
        return expiration.before(new Date());
    }

    public String gerarToken(Authentication authentication) {
        final Map<String, Object> claims = new HashMap<>();
        final UserDetails user = (UserDetails)authentication.getPrincipal();
        final List<String> roles = authentication.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        claims.put(ROLES, roles);
        return geradorToken(claims, user.getUsername());
    }

    private String geradorToken(Map<String, Object> claims, String subject) {
        final long now = System.currentTimeMillis();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean validaToken(String token) {
        final String login = obterLoginToken(token);

        return login != null && !ehTokenExpirado(token);
    }
}