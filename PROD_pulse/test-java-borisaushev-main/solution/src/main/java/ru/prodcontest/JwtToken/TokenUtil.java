package ru.prodcontest.JwtToken;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class TokenUtil {

    //Token - login hashMap
    private static final HashMap<String, String> tokenMap = new HashMap<>();
    private static final HashMap<String, String> loginMap = new HashMap<>();

    public static synchronized void updateTokenMap(String token, String newLogin) {
        tokenMap.put(token, newLogin);
    }

    public static synchronized String getLoginByToken(String token) {
        return tokenMap.get(token);
    }

    public static synchronized void updateLoginMap(String login, String password) {
        loginMap.put(login, password);
    }

    public static synchronized String getPasswordByToken(String token) {
        return loginMap.get(tokenMap.get(token));
    }

    public static synchronized void recallAllTokens(String login) {
        for (Map.Entry<String, String> pair : tokenMap.entrySet())
            if (pair.getValue().equals(login))
                tokenMap.remove(pair.getKey());

    }


    public static String parseToken(String unparsedToken) {
        return unparsedToken.replace("Bearer ", "").replace("{", "").replace("}", "").replace(" ", "");
    }

    private static final String secret = "TTASCzxcrfaAFsecretAffTgasmdoasasdfgFGFSDAY";

    public static Boolean isValidToken(String token) {
        try {
            JWT.decode(token);
            return getLoginByToken(token) != null;
        } catch (JWTDecodeException exc) {
            return false;
        }
    }

    public static String createJWTToken() {
        Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(secret),
                SignatureAlgorithm.HS512.getJcaName());

        String jwtToken = Jwts.builder()
                .signWith(hmacKey)
                .setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(12L, ChronoUnit.HOURS)))
                .compact();

        return jwtToken;
    }

}
