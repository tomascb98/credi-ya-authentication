package co.com.crediya.r2dbc.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", schema = "auth")
public class UserEntity {
    @Id
    @Column("user_id")
    private UUID id;
    
    private String name;
    
    @Column("last_name")
    private String lastName;
    
    @Column("birth_date")
    private LocalDate birthDate;
    
    private String address;
    
    @Column("phone_number")
    private String phoneNumber;
    
    private String email;
    
    @Column("salary_base")
    private BigDecimal salaryBase;
    
    private String password;
    
    @Column("document_number")
    private String documentNumber;
    
    @Column("user_role_id")
    private Integer userRoleId;
}
