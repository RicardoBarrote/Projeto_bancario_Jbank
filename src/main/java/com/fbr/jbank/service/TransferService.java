package com.fbr.jbank.service;

import com.fbr.jbank.controller.dto.TransferMoneyDto;
import com.fbr.jbank.entities.Transfer;
import com.fbr.jbank.entities.Wallet;
import com.fbr.jbank.exception.TransferException;
import com.fbr.jbank.exception.WalletNotFoundException;
import com.fbr.jbank.repository.TransferRepository;
import com.fbr.jbank.repository.WalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TransferService {

    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;

    public TransferService(TransferRepository transferRepository, WalletRepository walletRepository) {
        this.transferRepository = transferRepository;
        this.walletRepository = walletRepository;
    }

    @Transactional
    public void transferMoney(TransferMoneyDto dto) {
        final var sender = getWalletSender(dto);
        final var receiver = getWalletReceiver(dto);

        if (sender.getBalance().compareTo(dto.value()) < 0) {
            throw new TransferException("Insufficient balance. You current balance in $" + sender.getBalance());
        }

        persistTransfer(dto, receiver, sender);
        updateWallets(dto, sender, receiver);
    }


    private Wallet getWalletReceiver(TransferMoneyDto dto) {
        return walletRepository.findById(dto.receiver())
                .orElseThrow(() -> new WalletNotFoundException("receiver does not exists"));
    }

    private Wallet getWalletSender(TransferMoneyDto dto) {
        return walletRepository.findById(dto.sender())
                .orElseThrow(() -> new WalletNotFoundException("sender does not exists"));
    }

    private void updateWallets(TransferMoneyDto dto, Wallet sender, Wallet receiver) {
        sender.setBalance(sender.getBalance().subtract(dto.value()));
        receiver.setBalance(receiver.getBalance().add(dto.value()));

        walletRepository.save(sender);
        walletRepository.save(receiver);
    }

    private void persistTransfer(TransferMoneyDto dto, Wallet receiver, Wallet sender) {
        var transfer = new Transfer();
        transfer.setReceiver(receiver);
        transfer.setSender(sender);
        transfer.setTransferValue(dto.value());
        transfer.setTransferDateTime(LocalDateTime.now());
        transferRepository.save(transfer);
    }
}
