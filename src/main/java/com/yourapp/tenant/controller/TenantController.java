package com.yourapp.tenant.controller;

import com.yourapp.tenant.entity.Tenant;
import com.yourapp.tenant.service.TenantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tenants")
@RequiredArgsConstructor
public class TenantController {

    private final TenantService tenantService;

    @PostMapping
    public Tenant create(@RequestParam String name) {
        return tenantService.create(name);
    }
}