package com.yourapp.user.entity;

import com.yourapp.tenant.entity.Tenant;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String role; // ADMIN, MANAGER

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;
}