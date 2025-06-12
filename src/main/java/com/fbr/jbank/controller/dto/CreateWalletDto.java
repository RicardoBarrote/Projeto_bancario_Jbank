package com.fbr.jbank.controller.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateWalletDto(@NotBlank
                              String cpf,
                              @Email
                              @NotBlank
                              String email,
                              @NotBlank
                              String name) {
}
