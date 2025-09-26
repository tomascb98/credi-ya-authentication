package co.com.crediya.jwt;

import co.com.crediya.model.token.TokenClaims;
import co.com.crediya.model.token.gateways.TokenService;
import co.com.crediya.model.user.gateways.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class JwtTokenService implements TokenService {
    private final SecretKey secretKey;
    private final UserRepository userRepository;

    public  JwtTokenService(@Value("${jwt.secret}") String jwtSecret, UserRepository userRepository) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        this.userRepository = userRepository;
    }

    @Override
    public String generateToken(TokenClaims tokenClaims, Duration validity) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + validity.toMillis());

        return Jwts.builder()
                .subject(tokenClaims.getEmail())
                .claim("role", tokenClaims.getRole())
                .claim("userId", tokenClaims.getUserId())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey, Jwts.SIG.HS384)
                .compact();
    }

    @Override
    public Mono<Boolean> validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String email = claims.getSubject();
            String tokenRole = claims.get("role", String.class);

            return userRepository.findUserByEmail(email)
                    .map(user -> user.getRole().getName().equals(tokenRole))
                    .defaultIfEmpty(false);
        } catch (Exception e) {
            return Mono.just(false);
        }
    }

    @Override
    public TokenClaims extractTokenClaims(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return TokenClaims.builder()
                    .email(claims.getSubject())
                    .userId(claims.get("userId") != null ? claims.get("userId").toString() : null)
                    .role(claims.get("role") != null ? claims.get("role").toString() : null)
                    .issuedAt(convertToLocalDateTime(claims.getIssuedAt()))
                    .expiresAt(convertToLocalDateTime(claims.getExpiration()))
                    .build();
        } catch (Exception e) {
            return null;
        }
    }


    private LocalDateTime convertToLocalDateTime(Date date) {
        return date != null ?
                LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()) :
                null;
    }
}
