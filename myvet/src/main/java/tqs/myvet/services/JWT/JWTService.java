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
    
    @Value("${jwt.secret}")
    private static final String SECRET;
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

