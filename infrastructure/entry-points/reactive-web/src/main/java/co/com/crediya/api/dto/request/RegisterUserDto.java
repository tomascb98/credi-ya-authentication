package co.com.crediya.api.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "DTO para el registro de un nuevo usuario en el sistema")
public record RegisterUserDto (
        @Schema(description = "Nombres del usuario", example = "Juan Carlos", required = true)
        String name,
        
        @Schema(description = "Apellidos del usuario", example = "Pérez García", required = true)
        String lastName,
        
        @Schema(description = "Fecha de nacimiento del usuario", example = "1990-05-15", required = true)
        LocalDate birthDate,
        
        @Schema(description = "Dirección completa del usuario", example = "Calle 123 #45-67, Apartamento 401, Bogotá", required = true)
        String address,
        
        @Schema(description = "Número de teléfono del usuario", example = "3113883677")
        String phoneNumber,
        
        @Schema(description = "Correo electrónico del usuario", example = "juan.perez@email.com", required = true)
        String email,
        
        @Schema(description = "Salario base del usuario (entre 0 y 15,000,000)", example = "3500000.0", required = true)
        BigDecimal salaryBase,
        
        @Schema(description = "Contraseña del usuario", example = "Password123!")
        String password,
        
        @Schema(description = "Número de documento de identidad", example = "1018500661")
        String documentNumber,
        
        @Schema(description = "ID del rol asignado al usuario", example = "1")
        Integer userRoleId
){}
