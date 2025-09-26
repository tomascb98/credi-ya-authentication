package co.com.crediya.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "DTO de respuesta para el login exitoso de usuario")
public record LoginResponseDto(
        @Schema(description = "Correo electrónico del usuario autenticado", example = "juan.perez@email.com")
        String email,
        
        @Schema(description = "Rol del usuario autenticado", example = "ASESOR")
        String role,
        
        @Schema(description = "Token de acceso JWT generado", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken,
        
        @Schema(description = "Mensaje de confirmación", example = "Login exitoso")
        String message
) { }
