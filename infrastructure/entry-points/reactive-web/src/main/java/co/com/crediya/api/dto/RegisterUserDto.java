package co.com.crediya.api.dto;

import co.com.crediya.model.role.Role;

import java.math.BigDecimal;
import java.util.UUID;

public record RegisterUserDto (
        String name,
        String email,
        String password,
        String documentNumber,
        String phoneNumber,
        BigDecimal salaryBase,
        Integer userRoleId
){}
