package co.com.crediya.api.dto.response;

import lombok.Builder;

@Builder
public record LoginResponseDto(
        String email,
        String role,
        String accessToken,
        String message
) { }
