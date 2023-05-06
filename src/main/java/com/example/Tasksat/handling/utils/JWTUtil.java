package com.example.Tasksat.handling.utils;

import com.example.Tasksat.data.entities.accounts.Account;
import com.example.Tasksat.data.entities.accounts.Authority;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.*;
import java.util.stream.Collectors;

public class JWTUtil {

    private byte[] secret;

    private final Long expiration;

    public JWTUtil(String secret, String expiration) {
        this.secret = secret.getBytes();
        this.expiration = Long.parseLong(expiration);
    }

    public String extractUsername(String authToken) {

        String key = Base64.getEncoder().encodeToString(secret);
        String subject;

        try {

            subject = Jwts.parserBuilder().setSigningKey(key).build()
                    .parseClaimsJws(authToken).getBody()
                    .getSubject();
        }
        catch (Exception e) {
            subject = null;
            System.out.println(e);
        }

        return subject;
    }

    private Claims getClaimsFromToken(String authToken) {

        return Jwts.parserBuilder().setSigningKey(secret).build()
                .parseClaimsJws(authToken).getBody();
    }

    public boolean validateToken(String authToken) {

        return getClaimsFromToken(authToken)
                .getExpiration().before(new Date());
    }

    public String generateToken(Account account) {

        var claims = new HashMap<String, Object>();
        claims.put("roles", account.getAuthorities());
        claims.put("id", account.getId());


        var creation = new Date();
        var expiration = new Date(creation.getTime() + this.expiration);

        return "Bearer " + Jwts.builder()
                .setClaims(claims)
                .setSubject(account.getUsername())
                .setIssuedAt(creation)
                .setExpiration(expiration)
                .signWith(Keys.hmacShaKeyFor(secret))
                .compact();
    }

    public String extractId(String token) {
        return getClaimsFromToken(token).get("id").toString();
    }

    public Set<Authority> extractRoles(String authToken) {

        var roles = new HashSet<Authority>();

        for(var s: getClaimsFromToken(authToken).get("roles", List.class)) {
            roles.add(Authority.valueOf((String)s));
        }

        return roles;
    }

    public void setKey(byte[] key) {
        secret = key;
    }
}
