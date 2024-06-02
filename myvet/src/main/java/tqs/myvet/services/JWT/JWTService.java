package tqs.myvet.services.JWT;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.crypto.SecretKey;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import tqs.myvet.entities.User;

@Service
public class JWTService {
    
    private static final String SECRET = "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";
    private static final long VALIDITY = TimeUnit.DAYS.toMillis(1);

    public String generateToken(User user) {
        Map<String, String> claims = new HashMap<>();

        claims.put("iss", "myvet");
        claims.put("roles", user.getRoles().toString());

        return Jwts.builder()
            .claims(claims)
            .subject(user.getId().toString())
            .issuedAt(Date.from(Instant.now()))
            .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
            .signWith(getSecretKey())
            .compact();
    }

    private SecretKey getSecretKey() {
        byte[] decodeKey = Base64.getDecoder().decode(SECRET);
        return Keys.hmacShaKeyFor(decodeKey);
    }

    public Claims extractInfo(String jwt) {
        return Jwts.parser()
            .verifyWith(getSecretKey())
            .build()
            .parseSignedClaims(jwt)
            .getPayload();
    }

    public boolean isTokenValid(String jwt) {
        Claims claims = extractInfo(jwt);
        return claims.getExpiration().after(Date.from(Instant.now()));
    }
}

