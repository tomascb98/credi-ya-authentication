package co.com.crediya.api.dto;

public record ValidateUserResponseDto(
        String message,
        Boolean isValid
) {
}
