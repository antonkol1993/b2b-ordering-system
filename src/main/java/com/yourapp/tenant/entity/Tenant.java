package com.yourapp.tenant.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "tenants")
public class Tenant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String slug;

    private String name;

    @Column(name = "telegram_chat_id")
    private String telegramChatId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}