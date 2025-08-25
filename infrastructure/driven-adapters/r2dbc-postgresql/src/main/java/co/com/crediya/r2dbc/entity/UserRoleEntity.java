package co.com.crediya.r2dbc.entity;

import jakarta.persistence.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.Date;

@Table("user_roles")
public class UserRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column("user_role_id")
    private Integer id;
    private String name;
    private String description;
    private Date createdAt;
    private Date updatedAt;
}
