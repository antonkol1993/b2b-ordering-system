package com.yourapp.tenant.repository;

import com.yourapp.tenant.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
    java.util.Optional<Tenant> findBySlug(String slug);
}