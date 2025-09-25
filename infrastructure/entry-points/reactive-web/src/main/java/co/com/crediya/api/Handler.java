package co.com.crediya.api;

import co.com.crediya.api.advisor.GlobalExceptionHandler;
import co.com.crediya.api.config.authorization.AuthorizationService;
import co.com.crediya.api.dto.request.AuthorizationCheckRequestDto;
import co.com.crediya.api.dto.request.LoginRequestDto;
import co.com.crediya.api.dto.request.RegisterUserDto;
import co.com.crediya.api.dto.response.AuthorizationCheckResponseDto;
import co.com.crediya.api.dto.response.GetBasicUserInfoResponseDto;
import co.com.crediya.api.dto.response.LoginResponseDto;
import co.com.crediya.api.dto.response.ValidateUserResponseDto;
import co.com.crediya.api.mapper.UserMapperDtoMapper;
import co.com.crediya.model.exceptions.BusinessRuleException;
import co.com.crediya.model.exceptions.ValidationException;
import co.com.crediya.usecase.user.UserUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class Handler {

    private final UserUseCase userUseCase;
    private final UserMapperDtoMapper mapper;
    private final GlobalExceptionHandler exceptionHandler;
    private final AuthorizationService authorizationService;

    public Mono<ServerResponse> saveUser(ServerRequest request) {
        log.info("Iniciando registro de usuario");
        
        return request.bodyToMono(RegisterUserDto.class)
                .doOnNext(userDto -> log.info("DTO de usuario recibido: email={}", userDto.email()))
                .flatMap(userDto -> {
                    log.debug("Mapeando DTO a entidad de dominio");
                    return userUseCase.saveUser(mapper.mapToEntity(userDto));
                })
                .doOnNext(savedUser -> log.info("Usuario registrado exitosamente"))
                .flatMap(savedUser -> 
                    ServerResponse.status(HttpStatus.CREATED)
                            .bodyValue(mapper.mapToDto(savedUser))
                )
                .doOnNext(response -> log.info("Respuesta HTTP preparada con status CREATED"))
                .onErrorResume(ValidationException.class, exceptionHandler::handleValidationException)
                .onErrorResume(BusinessRuleException.class, exceptionHandler::handleBusinessRuleException)
                .onErrorResume(IllegalArgumentException.class, exceptionHandler::handleIllegalArgumentException)
                .onErrorResume(Exception.class, exceptionHandler::handleGenericException);
    }

    public Mono<ServerResponse> login(ServerRequest request) {
        log.info("Iniciando login de usuario");

        return request.bodyToMono(LoginRequestDto.class)
                .doOnNext(loginDto -> log.info("DTO de login recibido: email={}", loginDto.email()))
                .flatMap(loginDto -> 
                    userUseCase.login(loginDto.email(), loginDto.password())
                        .map(token -> LoginResponseDto.builder()
                                .accessToken(token.getTokenValue())
                                .email(token.getTokenClaims().getEmail())
                                .role(token.getTokenClaims().getRole())
                                .message("Login exitoso")
                                .build())
                )
                .doOnNext(response -> log.info("Login exitoso para usuario: {}", response.email()))
                .flatMap(loginResponse -> ServerResponse.ok()
                            .bodyValue(loginResponse)
                )
                .onErrorResume(Exception.class, ex -> {
                    log.error("Error en login: {}", ex.getMessage());
                    return ServerResponse.badRequest()
                            .bodyValue("Error en login: " + ex.getMessage());
                });
    }

    public Mono<ServerResponse> checkAuthorization(ServerRequest request) {
        return request.bodyToMono(AuthorizationCheckRequestDto.class)
                .doOnNext(authCheck -> log.info("Verificando autorización para path: {}", authCheck.path()))
                .flatMap(authCheck -> 
                    authorizationService.validateTokenAndAuthorization(request.headers().firstHeader(HttpHeaders.AUTHORIZATION), authCheck.path())
                        .flatMap(response -> {
                            log.info("Resultado de autorización: {} para rol: {}", 
                                    response.authorized() ? "AUTORIZADO" : "DENEGADO", 
                                    response.role());
                            return ServerResponse.ok().bodyValue(response);
                        })
                )
                .onErrorResume(Exception.class, ex -> {
                    log.error("Error en verificación de autorización: {}", ex.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue(new AuthorizationCheckResponseDto(
                                    null, null, false, "Error interno del servidor"
                            ));
                });
    }

    public Mono<ServerResponse> validateUser(ServerRequest request) {
        log.info("Iniciando validación de usuario");
        
        return request.bodyToMono(RegisterUserDto.class)
                .doOnNext(registerUserDto -> log.info("DTO de validación recibido"))
                .flatMap(registerUserDto -> {
                    log.debug("Validando usuario con documento: {}", registerUserDto.documentNumber());
                    return userUseCase.validateUser(registerUserDto.documentNumber());
                })
                .doOnNext(isValidUser -> log.info("Usuario validado: {}", isValidUser ? "válido" : "no encontrado"))
                .flatMap(isValidUser -> ServerResponse.status(HttpStatus.OK)
                        .bodyValue(new ValidateUserResponseDto(
                                "Validación exitosa",
                                isValidUser
                        )))
                .doOnNext(response -> log.info("Respuesta de validación preparada con status OK"));
    }

    public Mono<ServerResponse> findUsersByDocumentNumbers(ServerRequest request){
        log.info("Iniciando búsqueda de usuarios por números de documento");
        
        // Leer query parameters
        String documentNumbersParam = request.queryParam("documentNumbers")
                .orElse("");
        
        if (documentNumbersParam.isEmpty()) {
            log.warn("Parámetro documentNumbers no proporcionado");
            return ServerResponse.badRequest()
                    .bodyValue("El parámetro 'documentNumbers' es requerido");
        }
        
        String[] documentNumbers = documentNumbersParam.split(",");
        log.info("Buscando {} usuarios por números de documento: {}", 
                documentNumbers.length, String.join(", ", documentNumbers));
        
        return userUseCase.findUsersByDocumentNumber(documentNumbers)
                .collectList()
                .map(users -> {
                    log.info("Encontrados {} usuarios", users.size());
                    return users.stream()
                            .map(user -> GetBasicUserInfoResponseDto.builder()
                                    .name(user.getName() + " " + user.getLastName())
                                    .documentNumber(user.getDocumentNumber())
                                    .email(user.getEmail())
                                    .salaryBase(user.getSalaryBase())
                                    .build())
                            .collect(Collectors.toList());
                })
                .flatMap(users -> ServerResponse.ok().bodyValue(users))
                .doOnNext(response -> log.info("Respuesta preparada exitosamente"))
                .onErrorResume(ValidationException.class, ex -> {
                    log.error("Error de validación en búsqueda de usuarios: {}", ex.getMessage());
                    return ServerResponse.badRequest()
                            .bodyValue("Error de validación: " + ex.getMessage());
                })
                .onErrorResume(Exception.class, ex -> {
                    log.error("Error en búsqueda de usuarios: {}", ex.getMessage());
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue("Error interno del servidor");
                });
    }
}
