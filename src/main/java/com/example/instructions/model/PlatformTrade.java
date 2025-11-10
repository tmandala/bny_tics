package com.example.instructions.model;

/** Final platform-specific JSON shape */
public record PlatformTrade(
        String platformId,
        Trade trade
) {
    public record Trade(String account, String security, String type, long amount, String timestamp) {}
    /** Convenience factory */
    public static PlatformTrade of(String platformId, CanonicalTrade c) {
        return new PlatformTrade(
                platformId,
                new Trade(c.maskedAccount(), c.security(), c.type(), c.amount(), c.timestamp().toString())
        );
    }
}