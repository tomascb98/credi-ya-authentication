package co.com.crediya.usecase.user;

import co.com.crediya.model.user.User;
import co.com.crediya.model.user.gateways.UserRepository;
import co.com.crediya.model.utils.PasswordHasher;
import co.com.crediya.usecase.role.RoleUseCase;
import co.com.crediya.usecase.tokenservice.TokenServiceUseCase;
import co.com.crediya.usecase.user.helper.UserValidatorHelper;
import co.com.crediya.model.token.Token;
import co.com.crediya.model.token.TokenClaims;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;


public class UserUseCase{
    private final UserRepository userRepository;
    private final TokenServiceUseCase tokenServiceUseCase;
    private final PasswordHasher passwordHasher;
    private final RoleUseCase roleUseCase;

    public UserUseCase(UserRepository userRepository, TokenServiceUseCase tokenServiceUseCase, PasswordHasher passwordHasher, RoleUseCase roleUseCase) {
        this.userRepository = userRepository;
        this.tokenServiceUseCase = tokenServiceUseCase;
        this.passwordHasher = passwordHasher;
        this.roleUseCase = roleUseCase;
    }

    public Mono<User> saveUser(User user) {
        return UserValidatorHelper.validateUser(user, userRepository)
                .flatMap(validUser -> validateRoleExists(validUser))
                .flatMap(validUser -> passwordHasher.hash(validUser.getPassword())
                        .map(hashedPassword -> {
                            validUser.setPassword(hashedPassword);
                            return validUser;
                        }))
                .flatMap(userRepository::saveUser);
    }
    
    private Mono<User> validateRoleExists(User user) {
        return roleUseCase.validateRoleExists(user.getRole().getId())
                .filter(exists -> exists)
                .switchIfEmpty(Mono.error(new RuntimeException("Role con id " + user.getRole().getId() + " no existe")))
                .map(exists -> user);
    }

    public Mono<Token> login(String email, String password){
        return UserValidatorHelper.validateStringField(email, "correo electrónico")
                .flatMap(validEmail -> UserValidatorHelper.validateStringField(password, "contraseña"))
                .flatMap(validPassword -> userRepository.findUserByEmail(email))
                .filter(user -> passwordHasher.matches(password, user.getPassword()))
                .switchIfEmpty(Mono.error(new RuntimeException("Credenciales inválidas")))
                .map(user -> {
                    // Generar el JWT usando el objeto User (sin consulta BD)
                    String jwt = tokenServiceUseCase.generateTokenForUser(user, Duration.ofMinutes(60));
                    
                    // Crear los TokenClaims
                    TokenClaims claims = TokenClaims.builder()
                            .email(user.getEmail())
                            .role(user.getRole().getName())
                            .userId(user.getId().toString())
                            .issuedAt(LocalDateTime.now())
                            .expiresAt(LocalDateTime.now().plus(Duration.ofMinutes(60)))
                            .build();
                    
                    // Retornar Token
                    return Token.builder()
                            .tokenValue(jwt)
                            .tokenClaims(claims)
                            .createdAt(LocalDateTime.now())
                            .build();
                });
    }

    public Mono<User> findUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public Mono<Boolean> validateUser(String documentNumber) {
        return userRepository.findUserByDocumentNumber(documentNumber);
    }

    public Flux<User> findUsersByDocumentNumber(String[] documentNumbers) {
        if (documentNumbers == null || documentNumbers.length == 0) {
            return Flux.error(new RuntimeException("La lista de números de documento no puede estar vacía"));
        }
        
        // Filtrar números de documento vacíos o nulos
        Set<String> documentNumberSet = Set.of(documentNumbers).stream()
                .filter(docNumber -> docNumber != null && !docNumber.trim().isEmpty())
                .collect(Collectors.toSet());
        
        if (documentNumberSet.isEmpty()) {
            return Flux.error(new RuntimeException("No se proporcionaron números de documento válidos"));
        }
        
        return userRepository.findUsersByDocumentNumber(documentNumberSet);
    }
}