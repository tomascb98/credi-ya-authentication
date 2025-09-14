package co.com.crediya.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de respuesta para la validación de usuario")
public record ValidateUserResponseDto(
        @Schema(description = "Mensaje descriptivo del resultado de la validación", example = "Validación exitosa")
        String message,
        
        @Schema(description = "Indica si el usuario es válido en el sistema", example = "true")
        Boolean isValid
) {
}
