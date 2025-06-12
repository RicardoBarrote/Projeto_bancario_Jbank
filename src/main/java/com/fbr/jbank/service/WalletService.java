package com.fbr.jbank.service;

import com.fbr.jbank.controller.dto.*;
import com.fbr.jbank.entities.Deposit;
import com.fbr.jbank.entities.Wallet;
import com.fbr.jbank.exception.DeleteWalletException;
import com.fbr.jbank.exception.StatementException;
import com.fbr.jbank.exception.WalletDataAlreadyExistsException;
import com.fbr.jbank.exception.WalletNotFoundException;
import com.fbr.jbank.repository.DepositRepository;
import com.fbr.jbank.repository.WalletRepository;
import com.fbr.jbank.repository.dto.StatementView;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final DepositRepository depositRepository;

    public WalletService(WalletRepository walletRepository,
                         DepositRepository depositRepository) {
        this.walletRepository = walletRepository;
        this.depositRepository = depositRepository;
    }

    public Wallet createWallet(CreateWalletDto dto) {
        var wallet = new Wallet();
        validateCpfOrEmail(dto);

        wallet.setCpf(dto.cpf());
        wallet.setName(dto.name());
        wallet.setEmail(dto.email());
        wallet.setBalance(BigDecimal.ZERO);

        return walletRepository.save(wallet);
    }

    private void validateCpfOrEmail(CreateWalletDto dto) {
        boolean existsCpfOrEmail = walletRepository.findByCpfOrEmail(dto.cpf(), dto.email());
        if (existsCpfOrEmail) {
            throw new WalletDataAlreadyExistsException("Cpf or Email already exists");
        }
    }

    public boolean deleteWallet(UUID walletId) {
        var wallet = walletRepository.findById(walletId);

        if (wallet.isPresent()) {
            if (wallet.get().getBalance().compareTo(BigDecimal.ZERO) != 0) {
                throw new DeleteWalletException("the balance is not zero. The current amount is $" + wallet.get().getBalance());
            }

            walletRepository.deleteById(walletId);
        }
        return wallet.isPresent();
    }

    @Transactional
    public void deposit(UUID walletId, DepositMoneyDto dto, String ipAddres) {
        var deposit = new Deposit();
        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("there is no wallet with this id"));

        deposit.setWallet(wallet);
        deposit.setDepositValue(dto.value());
        deposit.setDepositDateTime(LocalDateTime.now());
        deposit.setIpAddress(ipAddres);
        depositRepository.save(deposit);

        wallet.setBalance(wallet.getBalance().add(dto.value()));
        walletRepository.save(wallet);
    }

    public StatementDto getStatement(UUID walletId, Integer page, Integer pageSize) {
        var wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new WalletNotFoundException("there is no wallet with this id"));

        var pageRequest = PageRequest.of(page, pageSize, Sort.Direction.DESC, "statement_date_time");

        var statement = walletRepository.findStatements(walletId.toString(), pageRequest)
                .map(view -> mapToDto(walletId, view));


        return new StatementDto(
                WalletDto.convertToDto(wallet),
                statement.getContent(),
                PaginationDto.convertToDto(statement)
        );
    }

    private StatementItemDto mapToDto(UUID walletId, StatementView view) {
        if (view.getType().equalsIgnoreCase("deposit")) {
            return mapToDeposit(view);
        }

        if (view.getType().equalsIgnoreCase("transfer") &&
                view.getWalletSender().equalsIgnoreCase(walletId.toString())) {
            return mapWhenTransferSend(view);
        }

        if (view.getType().equalsIgnoreCase("transfer") &&
                view.getWalletReceiver().equalsIgnoreCase(walletId.toString())) {
            return mapWhenTransferReceived(view);
        }

        throw new StatementException("invalid type " + view.getType());
    }

    private StatementItemDto mapWhenTransferReceived(StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money receiver from " + view.getWalletSender(),
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.CREDIT
        );
    }

    private StatementItemDto mapWhenTransferSend(StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money sent to " + view.getWalletReceiver(),
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.DEBIT
        );
    }

    private StatementItemDto mapToDeposit(StatementView view) {
        return new StatementItemDto(
                view.getStatementId(),
                view.getType(),
                "money deposit",
                view.getStatementValue(),
                view.getStatementDateTime(),
                StatementOperation.CREDIT
        );
    }

}
