package com.fbr.jbank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class WalletDataAlreadyExistsException extends JbankException {

    private final String detail;

    public WalletDataAlreadyExistsException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail problemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pb.setTitle("Wallet data already exists");
        pb.setDetail(detail);
        return pb;
    }
}

