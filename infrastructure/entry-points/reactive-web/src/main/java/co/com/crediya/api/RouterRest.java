package co.com.crediya.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import co.com.crediya.api.dto.request.RegisterUserDto;
import co.com.crediya.api.dto.request.LoginRequestDto;
import co.com.crediya.api.dto.request.AuthorizationCheckRequestDto;
import co.com.crediya.api.dto.response.ErrorResponseDto;
import co.com.crediya.api.dto.response.LoginResponseDto;
import co.com.crediya.api.dto.response.AuthorizationCheckResponseDto;
import co.com.crediya.api.dto.response.ValidateUserResponseDto;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {
    @Value("${routes.paths.users}")
    private String usersPath;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/register",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "saveUser",
                    operation = @Operation(
                            operationId = "registerUser",
                            summary = "Registrar nuevo usuario",
                            description = "Registra un nuevo usuario validando campos requeridos y reglas de negocio",
                            tags = {"Usuarios"},
                            requestBody = @RequestBody(
                                    description = "Datos del usuario a registrar",
                                    required = true,
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = RegisterUserDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201", 
                                            description = "Usuario registrado exitosamente",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = RegisterUserDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400", 
                                            description = "Error de validación",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ErrorResponseDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "409", 
                                            description = "Email ya registrado",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ErrorResponseDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500", 
                                            description = "Error interno",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ErrorResponseDto.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/login",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "login",
                    operation = @Operation(
                            operationId = "loginUser",
                            summary = "Iniciar sesión de usuario",
                            description = "Autentica un usuario con email y contraseña, retornando un token JWT",
                            tags = {"Autenticación"},
                            requestBody = @RequestBody(
                                    description = "Credenciales de login",
                                    required = true,
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = LoginRequestDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200", 
                                            description = "Login exitoso",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = LoginResponseDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400", 
                                            description = "Credenciales inválidas",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ErrorResponseDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500", 
                                            description = "Error interno",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ErrorResponseDto.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/validateUser",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "validateUser",
                    operation = @Operation(
                            operationId = "validateUser",
                            summary = "Validar existencia de usuario",
                            description = "Valida si un usuario existe en el sistema basado en su número de documento",
                            tags = {"Usuarios"},
                            requestBody = @RequestBody(
                                    description = "Datos del usuario a validar",
                                    required = true,
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = RegisterUserDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200", 
                                            description = "Validación completada",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ValidateUserResponseDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "400", 
                                            description = "Error de validación",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ErrorResponseDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500", 
                                            description = "Error interno",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ErrorResponseDto.class)
                                            )
                                    )
                            }
                    )
            ),
            @RouterOperation(
                    path = "/check-authorization",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "checkAuthorization",
                    operation = @Operation(
                            operationId = "checkAuthorization",
                            summary = "Verificar autorización de acceso",
                            description = "Verifica si un usuario con un token JWT tiene autorización para acceder a un endpoint específico",
                            tags = {"Autorización"},
                            requestBody = @RequestBody(
                                    description = "Datos para verificación de autorización",
                                    required = true,
                                    content = @Content(
                                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                                            schema = @Schema(implementation = AuthorizationCheckRequestDto.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200", 
                                            description = "Verificación completada",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = AuthorizationCheckResponseDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "401", 
                                            description = "Token inválido o expirado",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ErrorResponseDto.class)
                                            )
                                    ),
                                    @ApiResponse(
                                            responseCode = "500", 
                                            description = "Error interno",
                                            content = @Content(
                                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                                    schema = @Schema(implementation = ErrorResponseDto.class)
                                            )
                                    )
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST(usersPath + "/register"), handler::saveUser)
                .andRoute(POST(usersPath + "/login"), handler::login)
                .andRoute(POST(usersPath + "/validateUser"), handler::validateUser)
                .andRoute(POST(usersPath + "/check-authorization"), handler::checkAuthorization);
    }
}