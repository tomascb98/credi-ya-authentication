package co.com.crediya.model.token;

import lombok.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TokenClaims {
    private String email;
    private String role;
    private String userId;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;
}
