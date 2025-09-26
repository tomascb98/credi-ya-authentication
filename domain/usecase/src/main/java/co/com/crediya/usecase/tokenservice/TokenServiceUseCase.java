package co.com.crediya.usecase.tokenservice;

import co.com.crediya.model.token.TokenClaims;
import co.com.crediya.model.token.gateways.TokenService;
import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;

@RequiredArgsConstructor
public class TokenServiceUseCase {
    private final TokenService tokenService;
    private final UserRepository userRepository;

    public String generateTokenForUser(User user, Duration validity){
        TokenClaims claims = TokenClaims.builder()
                .email(user.getEmail())
                .role(user.getRole().getName())
                .userId(user.getId().toString())
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plus(validity))
                .build();
        
        return tokenService.generateToken(claims, validity);
    }

    // Validar token y obtener información del usuario
    public Mono<User> validateTokenAndGetUser(String token) {
        return tokenService.validateToken(token)
                .filter(isValid -> isValid)
                .switchIfEmpty(Mono.error(new RuntimeException("Token inválido")))
                .map(isValid -> tokenService.extractTokenClaims(token))
                .flatMap(claims -> userRepository.findUserByEmail(claims.getEmail()));
    }

    // Extraer claims del token
    public Mono<TokenClaims> extractTokenClaims(String token) {
        return tokenService.validateToken(token)
                .filter(isValid -> isValid)
                .switchIfEmpty(Mono.error(new RuntimeException("Token inválido")))
                .map(isValid -> tokenService.extractTokenClaims(token));
    }

    // Verificar si el token es válido
    public Mono<Boolean> isTokenValid(String token) {
        return tokenService.validateToken(token);
    }
}
