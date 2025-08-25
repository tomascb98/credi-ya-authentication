package co.com.crediya.r2dbc.entity;

import co.com.crediya.model.role.Role;
import jakarta.persistence.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigInteger;
import java.util.UUID;

@Table("users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column("user_id")
    private UUID id;
    private String name;
    private String email;
    private String password;
    private String documentNumber;
    private String phoneNumber;
    private BigInteger salaryBase;
    @ManyToOne
    @JoinColumn(name = "user_role_id") //columna fk en la tabla 'users'
    private UserRoleEntity userRole;
}
