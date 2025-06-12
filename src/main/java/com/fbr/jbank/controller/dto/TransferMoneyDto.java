package com.fbr.jbank.controller.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record TransferMoneyDto(@NotNull
                               UUID sender,
                               @NotNull
                               @DecimalMin("0.01") BigDecimal value,
                               @NotNull
                               UUID receiver) {
}
