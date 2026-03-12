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

    public Tenant create(String name) {

        Tenant tenant = new Tenant();
        tenant.setName(name);
        tenant.setCreatedAt(LocalDateTime.now());

        return tenantRepository.save(tenant);
    }
}