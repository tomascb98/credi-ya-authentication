package co.com.crediya.model.user;
import co.com.crediya.model.role.Role;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
//@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private String documentNumber;
    private String phoneNumber;
    private BigInteger salaryBase;
    private Role role;
}
