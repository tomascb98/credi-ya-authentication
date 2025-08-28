package co.com.crediya.model.user;
import co.com.crediya.model.role.Role;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String documentNumber;
    private String phoneNumber;
    private BigDecimal salaryBase;
    private Role role;
}
