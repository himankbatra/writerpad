package com.xebia.fs101.writerpad.services.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class JwtTokenService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expires_in}")
    private int expiresIn;
    @Value("${jwt.header}")
    private String authHeader;
    @Value("${jwt.cookie}")
    private String authCookie;
    @Autowired
    private UserDetailsService userDetailsService;
    private SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

    public String getUsernameFromToken(String token) {
        try {
            final Claims claims = this.getClaimsFromToken(token);
            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public String generateToken(String username) {
        return generateToken(username, generateCurrentDate(), generateExpirationDate());
    }

    public String generateToken(String username, Date issuedAt, Date expireAt) {
        return Jwts.builder()
                .setIssuer("writerpad")
                .setSubject(username)
                .setIssuedAt(issuedAt)
                .setExpiration(expireAt)
                .signWith(this.signatureAlgorithm, this.secret)
                .compact();
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(this.secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private long getCurrentTimeMillis() {
        return new Date().getTime();
    }

    private Date generateCurrentDate() {
        return new Date(getCurrentTimeMillis());
    }

    private Date generateExpirationDate() {
        return new Date(getCurrentTimeMillis() + this.expiresIn * 1000);
    }

    public String getToken(HttpServletRequest request) {
        Cookie authCookie = getCookieValueByName(request, this.authCookie);
        if (authCookie != null) {
            return authCookie.getValue();
        }
        String authHeader = request.getHeader(this.authHeader);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public Cookie getCookieValueByName(HttpServletRequest request, String name) {
        if (request.getCookies() == null) {
            return null;
        }
        for (int i = 0; i < request.getCookies().length; i++) {
            if (request.getCookies()[i].getName().equals(name)) {
                return request.getCookies()[i];
            }
        }
        return null;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
