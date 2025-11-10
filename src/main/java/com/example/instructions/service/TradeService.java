package com.example.instructions.service;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.PlatformTrade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TradeService {
    private final InMemoryStore store;
    private final KafkaPublisher publisher;
    private final String platformId;

    public TradeService(
            InMemoryStore store,
            KafkaPublisher publisher,
            @Value("${app.platform-id:P12345}") String platformId) {
        this.store = store;
        this.publisher = publisher;
        this.platformId = platformId;
    }

    /** Save canonical trade in memory, transform to json and publish */
    public PlatformTrade processAndPublish(CanonicalTrade canonical) {
        log.info("processAndPublish  : "+canonical);
        store.put(canonical);
        PlatformTrade out = PlatformTrade.of(platformId, canonical);
        publisher.publish(out);
        return out;
    }

    public CanonicalTrade get(String id) { return store.get(id); }
}
