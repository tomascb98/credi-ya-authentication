package co.com.crediya.api.dto.response;

public record AuthorizationCheckResponseDto(
    String path,
    String role,
    boolean authorized,
    String message
) {}
