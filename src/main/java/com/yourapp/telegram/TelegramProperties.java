package com.yourapp.telegram;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "telegram")
public class TelegramProperties {
    /**
     * Telegram Bot token (used to verify WebApp initData hash).
     * Set via env var TELEGRAM_BOT_TOKEN.
     */
    private String botToken;
}

