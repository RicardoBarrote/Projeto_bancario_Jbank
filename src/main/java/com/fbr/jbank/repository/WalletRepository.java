package com.fbr.jbank.repository;

import com.fbr.jbank.entities.Wallet;
import com.fbr.jbank.repository.dto.StatementView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    String SQL_STATEMENT = """
            SELECT
            	UUID_TO_BIN(transfer_id) as statement_id,
                "transfer" as type,
                transfer_value as statement_value,
                UUID_TO_BIN (wallet_receiver_id) as wallet_receiver,
                UUID_TO_BIN (wallet_sender_id) as wallet_sender,
                transfer_date_time as statement_date_time
             FROM
             	tb_transfer
             WHERE
                wallet_receiver_id = UUID_TO_BIN(?1) OR wallet_sender_id = UUID_TO_BIN(?1)
             UNION ALL
             SELECT
             	UUID_TO_BIN(deposit_id) as statement_id,
                "Deposit" as type,
                deposit_value as statement_value,
                UUID_TO_BIN(wallet_id) as wallet_receiver,
                "" as wallet_sender,
                deposit_date_time as statement_date_time
             FROM
             	tb_deposit
             WHERE
                wallet_id = UUID_TO_BIN(?1)
            """;


    String SQL_ACCOUNT_STATEMENT = """
            SELECT COUNT(*) FROM
            (
            """ + SQL_STATEMENT + """
            ) as total
            """;

    boolean findByCpfOrEmail(String cpf, String email);

    @Query(value = SQL_STATEMENT, countQuery = SQL_ACCOUNT_STATEMENT, nativeQuery = true)
    Page<StatementView> findStatements(String walletId, PageRequest pageRequest);
}
