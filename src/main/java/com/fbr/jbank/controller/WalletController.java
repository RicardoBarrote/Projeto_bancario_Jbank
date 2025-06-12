package com.fbr.jbank.controller;

import com.fbr.jbank.controller.dto.CreateWalletDto;
import com.fbr.jbank.controller.dto.DepositMoneyDto;
import com.fbr.jbank.controller.dto.StatementDto;
import com.fbr.jbank.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "/wallets")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    public ResponseEntity<Void> createWallet(@RequestBody @Valid CreateWalletDto dto) {
        var wallet = walletService.createWallet(dto);
        return ResponseEntity.created(URI.create("/wallets/"
                + wallet.getWalletId().toString())).build();
    }

    @PostMapping(path = "/{walletId}/deposits")
    public ResponseEntity<Void> deposit(@PathVariable("walletId") UUID walletId,
                                        @RequestBody @Valid DepositMoneyDto dto,
                                        HttpServletRequest servletRequest) {

        walletService.deposit(walletId, dto, servletRequest.getAttribute("x-user-ip").toString());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(path = "/{walletId}")
    public ResponseEntity<Void> deleteWallet(@PathVariable("walletId") UUID walletId) {
        var deleteWallet = walletService.deleteWallet(walletId);

        return deleteWallet ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }

    @GetMapping("/{walletId}/statements")
    public ResponseEntity<StatementDto> getStatement(@PathVariable("walletId") UUID walletId,
                                                     @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        var statement = walletService.getStatement(walletId, page, pageSize);
        return ResponseEntity.ok(statement);
    }
}
