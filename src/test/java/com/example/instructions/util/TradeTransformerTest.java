package com.example.instructions.util;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.InboundInstruction;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class TradeTransformerTest {
    @Test
    void toCanonicalMasksAndNormalizes() {
        InboundInstruction in = new InboundInstruction("12345678", "abc123", "Buy", 1000, Instant.parse("2025-08-04T21:15:33Z"));
        CanonicalTrade c = TradeTransformer.toCanonical(in);
        assertTrue(c.maskedAccount().startsWith("****"));
        assertEquals("ABC123", c.security());
        assertEquals("B", c.type());
        assertEquals(1000, c.amount());
    }

    @Test
    void invalidSecurityThrows() {
        InboundInstruction in = new InboundInstruction("12345678", "bad!!", "Sell", 1, Instant.now());
        assertThrows(IllegalArgumentException.class, () -> TradeTransformer.toCanonical(in));
    }
}
