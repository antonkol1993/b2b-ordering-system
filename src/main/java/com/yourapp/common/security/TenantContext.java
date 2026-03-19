package com.yourapp.common.security;

/**
 * Thread-local tenantId для multi-tenant.
 * Tenant id берется ТОЛЬКО из session (через фильтр), наружу не передаем.
 */
public final class TenantContext {

    private static final ThreadLocal<Long> TENANT_ID = new ThreadLocal<>();

    private TenantContext() {}

    public static void setTenantId(Long tenantId) {
        TENANT_ID.set(tenantId);
    }

    public static Long getTenantId() {
        return TENANT_ID.get();
    }

    public static long requireTenantId() {
        Long tenantId = TENANT_ID.get();
        if (tenantId == null) {
            throw new IllegalStateException("TenantId is not set in TenantContext");
        }
        return tenantId;
    }

    public static void clear() {
        TENANT_ID.remove();
    }
}

