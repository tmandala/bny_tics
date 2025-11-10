package com.example.instructions.service;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.PlatformTrade;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TradeServiceTest {
    @Test
    void processPublishesAndStores() {
        InMemoryStore store = new InMemoryStore();
        KafkaPublisher publisher = mock(KafkaPublisher.class);
        TradeService svc = new TradeService(store, publisher, "P12345");

        CanonicalTrade c = new CanonicalTrade("id-1", "****1234", "ABC", "B", 10, Instant.now());
        PlatformTrade out = svc.processAndPublish(c);

        assertEquals("P12345", out.platformId());
        assertNotNull(store.get("id-1"));
        verify(publisher, times(1)).publish(any());
    }
}
