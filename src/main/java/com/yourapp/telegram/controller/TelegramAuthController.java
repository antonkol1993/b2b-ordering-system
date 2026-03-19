package com.yourapp.telegram.controller;

import com.yourapp.telegram.service.TelegramAuthService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TelegramAuthController {

    private final TelegramAuthService telegramAuthService;

    @PostMapping("/api/telegram/auth")
    @ResponseStatus(HttpStatus.OK)
    public TelegramAuthResponse auth(@RequestBody TelegramAuthRequest request, HttpSession session) {
        var user = telegramAuthService.authFromInitData(request.initData(), session);
        return new TelegramAuthResponse(user.getId(), user.getTenant().getId(), user.getRole());
    }

    public record TelegramAuthRequest(String initData) {}

    public record TelegramAuthResponse(Long userId, Long tenantId, String role) {}
}

