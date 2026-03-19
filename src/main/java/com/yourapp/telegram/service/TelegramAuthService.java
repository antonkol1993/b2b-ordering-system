package com.yourapp.telegram.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourapp.common.AppConstants;
import com.yourapp.session.SessionService;
import com.yourapp.tenant.entity.Tenant;
import com.yourapp.tenant.repository.TenantRepository;
import com.yourapp.telegram.TelegramProperties;
import com.yourapp.telegram.TelegramWebAppAuthUtil;
import com.yourapp.user.entity.User;
import com.yourapp.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TelegramAuthService {

    private static final long MAX_AUTH_AGE_SECONDS = 24 * 60 * 60; // 24h

    private final TelegramProperties telegramProperties;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final ObjectMapper objectMapper;

    public User authFromInitData(String initData, HttpSession session) {
        Map<String, String> data = TelegramWebAppAuthUtil.parseInitData(initData);
        TelegramWebAppAuthUtil.verifyInitDataSignature(data, telegramProperties.getBotToken());
        TelegramWebAppAuthUtil.verifyAuthDate(data, MAX_AUTH_AGE_SECONDS);

        String tenantSlug = data.get("start_param");
        if (tenantSlug == null || tenantSlug.isBlank()) {
            throw new IllegalArgumentException("start_param is missing");
        }

        Tenant tenant = tenantRepository.findBySlug(tenantSlug)
                .orElseThrow(() -> new IllegalArgumentException("Unknown tenant slug: " + tenantSlug));

        long telegramUserId = TelegramWebAppAuthUtil.extractTelegramUserId(data, objectMapper);

        User user = userRepository.findByTelegramIdAndTenantId(telegramUserId, tenant.getId())
                .orElseGet(() -> {
                    User created = new User();
                    created.setTenant(tenant);
                    created.setTelegramId(telegramUserId);
                    created.setRole(AppConstants.ROLE_USER);
                    created.setCreatedAt(LocalDateTime.now());
                    // For Telegram users we don't need password.
                    created.setPassword(null);
                    return userRepository.save(created);
                });

        // Keep session always in sync.
        sessionService.setSession(session, user);
        return user;
    }
}

