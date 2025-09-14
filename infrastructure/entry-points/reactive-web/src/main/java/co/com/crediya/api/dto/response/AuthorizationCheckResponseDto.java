package co.com.crediya.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta para la verificación de autorización")
public record AuthorizationCheckResponseDto(
    @Schema(description = "Ruta del endpoint verificado", example = "/api/v1/users/register")
    String path,
    
    @Schema(description = "Rol del usuario autenticado", example = "ASESOR")
    String role,
    
    @Schema(description = "Indica si el usuario está autorizado para acceder al endpoint", example = "true")
    boolean authorized,
    
    @Schema(description = "Mensaje descriptivo del resultado", example = "Usuario autorizado")
    String message
) {}
