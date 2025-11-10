package com.example.instructions.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import java.time.Instant;

/** Raw inbound message (REST or Kafka) before conversion */
@JsonIgnoreProperties(ignoreUnknown = true)
public record InboundInstruction(
        @NotBlank String accountNumber,
        @NotBlank String securityId,
        @NotBlank String tradeType, // e.g., Buy/Sell/B/S
        @Positive long amount,
        @NotNull Instant timestamp
) {}