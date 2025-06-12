package com.fbr.jbank.repository;

import com.fbr.jbank.entities.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DepositRepository extends JpaRepository<Deposit, UUID> {
}
