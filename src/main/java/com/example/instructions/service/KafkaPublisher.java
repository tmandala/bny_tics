package com.example.instructions.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaPublisher {
    private final KafkaTemplate<String, String> template;
    private final ObjectMapper objectMapper;
    private final String outboundTopic;

    public KafkaPublisher(
            KafkaTemplate<String, String> template,
            ObjectMapper objectMapper,
            @Value("${app.kafka.topics.outbound}") String outboundTopic) {
        this.template = template;
        this.objectMapper = objectMapper;
        this.outboundTopic = outboundTopic;
    }

    public void publish(Object payload) {
        try {
            String json = objectMapper.writeValueAsString(payload);
            template.send(new ProducerRecord<>(outboundTopic, json));
        } catch (Exception e) {
            throw new RuntimeException("Kafka publish failed", e);
        }
    }
}
