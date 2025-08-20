package com.hiimtuan.common_service.service;

import com.hiimtuan.common_service.config.AuthenticationConfiguration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
@AllArgsConstructor
public class JwtServiceImpl implements JwtService {
    private final AuthenticationConfiguration authenticationConfiguration;

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String generateToken(String key) {
        return generateToken(new HashMap<>(), key);
    }

    public String generateToken(Map<String, Object> extraClaims, String key) {
        return buildToken(extraClaims, key, authenticationConfiguration.getExpirationTime());
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            String key,
            long expiration
    ) {
        return Jwts
                .builder().claims(extraClaims).subject(key).issuedAt(new Date(System.currentTimeMillis())).expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, String key) {
        final String keySecret = extractUsername(token);

        return (keySecret.equals(key)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(authenticationConfiguration.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
