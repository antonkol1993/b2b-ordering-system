package com.yourapp.user.entity;

import com.yourapp.tenant.entity.Tenant;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "app_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    private String role; // ADMIN, MANAGER

    @Column(name = "telegram_id")
    private Long telegramId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private Tenant tenant;
}