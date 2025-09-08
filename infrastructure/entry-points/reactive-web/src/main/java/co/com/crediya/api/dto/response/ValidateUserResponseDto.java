package co.com.crediya.api.dto.response;

public record ValidateUserResponseDto(
        String message,
        Boolean isValid
) {
}
