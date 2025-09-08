package co.com.crediya.api.dto.request;


public record AuthorizationCheckRequestDto(
        String path,
        String accessToken
) {
}
