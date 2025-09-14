package co.com.crediya.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO para el login de usuario en el sistema")
public record LoginRequestDto (
    @Schema(description = "Correo electrónico del usuario", example = "juan.perez@email.com", required = true)
    String email,
    
    @Schema(description = "Contraseña del usuario", example = "Password123!", required = true)
    String password
){}
