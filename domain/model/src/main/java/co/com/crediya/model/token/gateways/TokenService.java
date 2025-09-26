package co.com.crediya.model.token.gateways;

import co.com.crediya.model.token.TokenClaims;
import reactor.core.publisher.Mono;

import java.time.Duration;

public interface TokenService {
    String generateToken(TokenClaims tokenClaims, Duration validity);
    Mono<Boolean> validateToken(String token);
    TokenClaims extractTokenClaims(String token);
}
