package co.com.crediya.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para verificar autorización de acceso a un endpoint")
public record AuthorizationCheckRequestDto(
        @Schema(description = "Ruta del endpoint a verificar", example = "/api/v1/users/register", required = true)
        String path
) {
}
