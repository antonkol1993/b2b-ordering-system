package com.yourapp.tenant.service;

import com.yourapp.tenant.entity.Tenant;
import com.yourapp.tenant.repository.TenantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TenantService {

    private final TenantRepository tenantRepository;

    public Tenant create(String slug, String name, String telegramChatId) {

        Tenant tenant = new Tenant();
        tenant.setSlug(slug);
        tenant.setName(name);
        tenant.setCreatedAt(LocalDateTime.now());
        tenant.setTelegramChatId(telegramChatId);

        return tenantRepository.save(tenant);
    }

    public Tenant create(String name) {
        // Backward-compatible helper for old endpoints.
        // For SaaS flow we want slug explicitly.
        return create(name, name, null);
    }
}