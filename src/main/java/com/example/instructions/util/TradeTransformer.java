package com.example.instructions.util;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.InboundInstruction;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Pattern;

public final class TradeTransformer {
    private static final Pattern SECURITY_PATTERN = Pattern.compile("^[A-Z0-9]{1,12}$");

    private TradeTransformer() {}

    public static CanonicalTrade toCanonical(InboundInstruction in) {
        String masked = maskAccount(in.accountNumber());
        String security = in.securityId() == null ? "" : in.securityId().toUpperCase(Locale.ROOT);
        if (!SECURITY_PATTERN.matcher(security).matches()) {
            throw new IllegalArgumentException("Invalid securityId format");
        }
        String type = convertTradeType(in.tradeType());
        return new CanonicalTrade(
                UUID.randomUUID().toString(),
                masked,
                security,
                type,
                in.amount(),
                in.timestamp() == null ? Instant.now() : in.timestamp()
        );
    }
    /** Normalize TradeType */
    public static String convertTradeType(String tradeType) {
        if (tradeType == null) return "B";
        String r = tradeType.trim().toLowerCase(Locale.ROOT);
        return switch (r) {
            case "b", "buy" -> "B";
            case "s", "sell" -> "S";
            default -> throw new IllegalArgumentException("Invalid tradeType");
        };
    }

    /** masks all but last 4 digits */
    public static String maskAccount(String accountNo) {
        if (accountNo == null || accountNo.length() < 4) {
            throw new IllegalArgumentException("accountNumber too short");
        }
        String last4 = accountNo.substring(accountNo.length() - 4);
        return "****" + last4;
    }
}

