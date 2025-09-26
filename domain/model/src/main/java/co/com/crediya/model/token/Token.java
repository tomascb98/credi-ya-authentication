package co.com.crediya.model.token;
import lombok.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Token {
    private String tokenValue;
    private TokenClaims tokenClaims;
    private LocalDateTime createdAt;
}
