package com.duocuc.apolo.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;

@Component
public class JwtTokenUtil {

    // Clave secreta (usa una cadena larga y segura)
    private static final String SECRET_KEY = "TNKJENFKJFKKSDFJDSKFJI34JUR9U4JFJM499T99TJMFMF";

    // Duración del token: 1 día
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Generar token
    public String generateToken(Long userId, String email, String roleName) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", roleName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Validar token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    // Obtener email del token
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("role", String.class);
    }
}
