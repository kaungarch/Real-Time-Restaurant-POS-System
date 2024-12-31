package com.kaung_dev.RestaurantPOS.security.jwt;


import com.kaung_dev.RestaurantPOS.security.user.CustomUserDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.SecurityException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Date;
import java.util.List;

import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    @Value(value = "${auth.token.jwtSecret}")
    private String secret;

    @Value(value = "${auth.token.expirationInMills}")
    private int expirationTime;

    public String generateToken(Authentication authentication) {
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();
        List<String> roles = userPrincipal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        return Jwts.builder()
                .claim("name", userPrincipal.getName())
                .claim("id", userPrincipal.getId())
                .claim("role", roles)
                .expiration(new Date(new Date().getTime() + expirationTime))
                .issuedAt(new Date())
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();

    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(key())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SecurityException |
                 IllegalArgumentException e) {
            throw new JwtException(e.getMessage());
        }
    }

    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("name", String.class);
    }

    public Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(this.secret));
    }
}
