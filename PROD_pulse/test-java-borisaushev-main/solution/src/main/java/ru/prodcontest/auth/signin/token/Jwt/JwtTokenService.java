package ru.prodcontest.auth.signin.token.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import ru.prodcontest.auth.signin.token.Exceptions.TokenDoesntExistException;
import ru.prodcontest.auth.signin.token.Exceptions.TokenExpiredException;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;

@Service
public class JwtTokenService {

    private final SecretKey jwtSecret;

    public JwtTokenService(@Value("${jwt.secret}") String jwtSecret) {
        this.jwtSecret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String generateToken(HashMap<String, Object> properties) {
        final LocalDateTime now = LocalDateTime.now();
        final Instant ExpirationInstant = now.plusMinutes(120).atZone(ZoneId.systemDefault()).toInstant();
        final Date Expiration = Date.from(ExpirationInstant);
        var builder = Jwts.builder();
        for(var entry : properties.entrySet())
                builder.claim(entry.getKey(), entry.getValue());

        builder.setExpiration(Expiration)
                .signWith(jwtSecret);
        return builder.compact();
    }

    public boolean validateToken(@NonNull String Token) {
        return validateToken(Token, jwtSecret);
    }

    private boolean validateToken(@NonNull String token, @NonNull Key secret) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secret)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException ignored) {
            throw new TokenExpiredException("token expired");
        }
    }

    public Claims getClaims(@NonNull String token) {
        return getClaims(token, jwtSecret);
    }
    
    private Claims getClaims(@NonNull String token, @NonNull Key secret) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Value("${token.bearer.prefix}")
    private String BEARER_PREFIX;

    public String parseToken(String token) {
        if(token.length() < BEARER_PREFIX.length() + 1)
            throw new TokenDoesntExistException("invalid token");
        System.out.println("token: " + token);
        token = token.substring(BEARER_PREFIX.length());
        System.out.println("parsed token: " + token);
        return token;
    }

    public int getIdByToken(String token) {
        return getClaims(token).get("userId", Integer.class);
    }

}