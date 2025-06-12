package com.fbr.jbank.controller.dto;

import com.fbr.jbank.entities.Wallet;

import java.math.BigDecimal;
import java.util.UUID;

public record WalletDto(UUID walletId,
                        String cpf,
                        String name,
                        String email,
                        BigDecimal balance) {

    public static WalletDto convertToDto(Wallet wallet) {
        return new WalletDto(
                wallet.getWalletId(),
                wallet.getCpf(),
                wallet.getName(),
                wallet.getEmail(),
                wallet.getBalance()
        );
    }
}
