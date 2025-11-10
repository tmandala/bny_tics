package com.example.instructions.model;

import jakarta.validation.constraints.*;
import java.time.Instant;

public record CanonicalTrade(
        @NotBlank String id,
        @NotBlank String maskedAccount,
        @NotBlank String security,
        @Pattern(regexp = "B|S") String type,
        @Positive long amount,
        @NotNull Instant timestamp
) {}
