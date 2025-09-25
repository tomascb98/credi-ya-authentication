package co.com.crediya.api.dto.response;

import lombok.Builder;
import java.math.BigDecimal;

@Builder
public record GetBasicUserInfoResponseDto(
        String email,
        String documentNumber,
        String name,
        BigDecimal salaryBase
) {
}
