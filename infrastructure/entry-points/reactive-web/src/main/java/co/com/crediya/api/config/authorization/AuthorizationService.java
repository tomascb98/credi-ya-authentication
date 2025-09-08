package co.com.crediya.api.config.authorization;

import co.com.crediya.api.dto.response.AuthorizationCheckResponseDto;
import co.com.crediya.model.token.TokenClaims;
import co.com.crediya.model.token.gateways.TokenService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Service
public class AuthorizationService {

    private final Map<String, Set<String>> endpointRolesMap;
    private final TokenService tokenService;

    public AuthorizationService(TokenService tokenService) {
        this.tokenService = tokenService;
        this.endpointRolesMap = initializeEndpointRoles();
    }

    private Map<String, Set<String>> initializeEndpointRoles() {
        Map<String, Set<String>> map = new HashMap<>();

        // Microservicio de Usuarios
        map.put("/api/v1/users/register", Set.of("ASESOR", "ADMIN"));

        // Microservicio de Créditos
        map.put("/api/v1/credits/create", Set.of("ASESOR", "MANAGER", "ADMIN"));

        return map;
    }

    // Método principal que valida token y autorización
    public Mono<AuthorizationCheckResponseDto> validateTokenAndAuthorization(String token, String path) {
        return validateToken(token)
                .flatMap(isTokenValid -> {
                    if (!isTokenValid) {
                        return Mono.just(new AuthorizationCheckResponseDto(
                                path, 
                                null, 
                                false, 
                                "Token inválido"
                        ));
                    }
                    
                    // Si el token es válido, extraer el rol y verificar autorización
                    return extractRoleFromToken(token)
                            .flatMap(userRole -> 
                                isAuthorized(path, userRole)
                                    .map(isAuthorized -> new AuthorizationCheckResponseDto(
                                            path,
                                            userRole,
                                            isAuthorized,
                                            isAuthorized ? "Acceso autorizado" : "Acceso denegado"
                                    ))
                            );
                });
    }

    // Validar si el token es válido
    private Mono<Boolean> validateToken(String token) {
        return tokenService.validateToken(token)
                .onErrorReturn(false);
    }

    // Extraer el rol del token
    private Mono<String> extractRoleFromToken(String token) {
        return Mono.fromCallable(() -> {
            TokenClaims claims = tokenService.extractTokenClaims(token);
            return claims != null ? claims.getRole() : "USER";
        });
    }

    // Verificar si el rol tiene acceso al endpoint
    private Mono<Boolean> isAuthorized(String endpoint, String userRole) {
        return Mono.fromCallable(() -> {
            // Buscar coincidencia exacta primero
            Set<String> allowedRoles = endpointRolesMap.get(endpoint);
            if (allowedRoles != null) {
                return allowedRoles.contains(userRole);
            }

            // Buscar coincidencia con wildcards
            for (Map.Entry<String, Set<String>> entry : endpointRolesMap.entrySet()) {
                if (matchesPattern(endpoint, entry.getKey())) {
                    return entry.getValue().contains(userRole);
                }
            }

            // Si no hay regla específica, permitir por defecto
            return true;
        });
    }

    private boolean matchesPattern(String endpoint, String pattern) {
        if (pattern.endsWith("/**")) {
            String prefix = pattern.substring(0, pattern.length() - 3);
            return endpoint.startsWith(prefix);
        }
        return endpoint.equals(pattern);
    }
}