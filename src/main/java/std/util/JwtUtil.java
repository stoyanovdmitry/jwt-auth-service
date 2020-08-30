package std.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import std.dto.AuthResponse;

import javax.crypto.SecretKey;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtil {

    private static final JwtBuilder JWT_BUILDER = new DefaultJwtBuilder();

    private static final Long ACCESS_EXPIRATION = 30L;
    private static final Long REFRESH_EXPIRATION = 240L;
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final String USERNAME = "username";

    public AuthResponse generateTokens(Long userId, String username) {
        var access = getToken(userId, username, ACCESS_EXPIRATION);
        var refresh = getToken(userId, username, REFRESH_EXPIRATION);

        return AuthResponse.of(access, refresh);
    }

    public boolean isTokenExpired(String token) {
        Date expiration = parseBody(token).get(Claims.EXPIRATION, Date.class);

        return new Date().before(expiration);
    }

    public Long readUserId(String token) {
        String subject = parseBody(token).get(Claims.SUBJECT, String.class);

        return Long.parseLong(subject);
    }

    public String readUsername(String token) {
        return parseBody(token).get(USERNAME, String.class);
    }

    private Claims parseBody(String token) {
        return Jwts.parserBuilder()
                   .setSigningKey(SECRET_KEY)
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
    }

    private String getToken(Long userId, String username, Long expirationMinutes) {
        return JWT_BUILDER.setSubject(userId.toString())
                          .addClaims(Collections.singletonMap(USERNAME, username))
                          .setExpiration(defineExpirationTime(expirationMinutes))
                          .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                          .compact();
    }

    private Date defineExpirationTime(Long expiration) {
        return Optional.of(LocalDateTime.now())
                       .map(time -> time.plusMinutes(expiration))
                       .map(Timestamp::valueOf)
                       .get();
    }
}
