package com.yourapp.telegram;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;

public final class TelegramWebAppAuthUtil {

    private TelegramWebAppAuthUtil() {}

    public static Map<String, String> parseInitData(String initData) {
        Map<String, String> result = new HashMap<>();
        if (initData == null || initData.isBlank()) {
            return result;
        }

        String[] pairs = initData.split("&");
        for (String pair : pairs) {
            if (pair.isBlank()) continue;
            int idx = pair.indexOf('=');
            if (idx <= 0) continue;
            String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8);
            String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8);
            result.put(key, value);
        }
        return result;
    }

    public static void verifyInitDataSignature(Map<String, String> data, String botToken) {
        String providedHash = data.get("hash");
        if (providedHash == null || providedHash.isBlank()) {
            throw new IllegalArgumentException("initData.hash is missing");
        }

        // https://core.telegram.org/bots/webapps#validating-data-received
        List<String> keys = new ArrayList<>(data.keySet());
        keys.remove("hash");
        Collections.sort(keys);

        StringBuilder dataCheckString = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = data.get(key);
            if (value == null) value = "";
            if (i > 0) dataCheckString.append('\n');
            dataCheckString.append(key).append('=').append(value);
        }

        byte[] secretKey = hmacSha256(botToken.getBytes(StandardCharsets.UTF_8), "WebAppData");
        String calculatedHash = hmacSha256Hex(secretKey, dataCheckString.toString());

        if (!calculatedHash.equalsIgnoreCase(providedHash)) {
            throw new IllegalArgumentException("Invalid initData hash");
        }
    }

    public static void verifyAuthDate(Map<String, String> data, long maxAgeSeconds) {
        String authDateStr = data.get("auth_date");
        if (authDateStr == null) return; // Telegram should always provide it, but keep tolerant.
        long authDate = Long.parseLong(authDateStr);
        long now = Instant.now().getEpochSecond();
        if (Math.abs(now - authDate) > maxAgeSeconds) {
            throw new IllegalArgumentException("initData auth_date is too old");
        }
    }

    public static long extractTelegramUserId(Map<String, String> data, ObjectMapper objectMapper) {
        String userJson = data.get("user");
        if (userJson == null || userJson.isBlank()) {
            throw new IllegalArgumentException("initData.user is missing");
        }

        try {
            JsonNode userNode = objectMapper.readTree(userJson);
            return userNode.get("id").asLong();
        } catch (Exception e) {
            throw new IllegalArgumentException("Cannot parse initData.user JSON", e);
        }
    }

    private static byte[] hmacSha256(byte[] key, String message) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(key, "HmacSHA256"));
            return mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new IllegalStateException("HMAC-SHA256 failed", e);
        }
    }

    private static String hmacSha256Hex(byte[] key, String message) {
        byte[] digest = hmacSha256(key, message);
        StringBuilder sb = new StringBuilder(digest.length * 2);
        for (byte b : digest) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}

