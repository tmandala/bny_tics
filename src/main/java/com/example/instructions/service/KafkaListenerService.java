package com.example.instructions.service;

import com.example.instructions.model.CanonicalTrade;
import com.example.instructions.model.InboundInstruction;
import com.example.instructions.util.TradeTransformer;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaListenerService {
    private final ObjectMapper objectMapper;
    private final TradeService tradeService;

    public KafkaListenerService(ObjectMapper objectMapper, TradeService tradeService) {
        this.objectMapper = objectMapper;
        this.tradeService = tradeService;
    }

    @KafkaListener(topics = "${app.kafka.topics.inbound}", groupId = "${spring.kafka.consumer.group-id}")
    public void handleInbound(String msg) {
        try {
            // secure deserialization/parsing with strict known fields enforced by record & @JsonIgnoreProperties on unknowns
            InboundInstruction in = objectMapper.readValue(msg, InboundInstruction.class);
            CanonicalTrade c = TradeTransformer.toCanonical(in);
            tradeService.processAndPublish(c);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}

