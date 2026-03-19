package com.yourapp.common.security;

import com.yourapp.common.AppConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Заполняет TenantContext из HttpSession (если он есть).
 * Важно: tenantId не принимаем из внешнего ввода.
 */
@Component
public class TenantContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        try {
            if (session != null) {
                Object tenantIdObj = session.getAttribute(AppConstants.SESSION_TENANT_ID);
                if (tenantIdObj instanceof Long tenantId) {
                    TenantContext.setTenantId(tenantId);
                }
            }
            filterChain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}

