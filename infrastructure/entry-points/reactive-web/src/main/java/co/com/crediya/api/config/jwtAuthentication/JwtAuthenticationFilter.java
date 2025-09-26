package co.com.crediya.api.config.jwtAuthentication;

import co.com.crediya.model.token.TokenClaims;
import co.com.crediya.model.token.gateways.TokenService;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;

public class JwtAuthenticationFilter implements WebFilter {

    private final TokenService tokenService;

    public JwtAuthenticationFilter(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return extractToken(exchange)
                .flatMap(this::createAuthenticationIfValid)
                .flatMap(auth -> chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth)))
                .switchIfEmpty(chain.filter(exchange));
    }

    private Mono<Authentication> createAuthenticationIfValid(String token) {
        TokenClaims claims = tokenService.extractTokenClaims(token);
        if (claims == null) {
            return Mono.empty();
        }

        // Valida contra BD (rol/usuario)
        return tokenService.validateToken(token)
                .filter(isValid -> isValid)
                .map(valid -> new UsernamePasswordAuthenticationToken(
                        claims.getEmail(),
                        token,
                        List.of(new SimpleGrantedAuthority("ROLE_" + claims.getRole()))
                ));
    }

    private Mono<String> extractToken(ServerWebExchange exchange) {
        String bearerToken = exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION);

        if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return Mono.just(bearerToken.substring(7));
        }

        return Mono.empty();
    }
}
