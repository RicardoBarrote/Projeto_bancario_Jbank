package com.fbr.jbank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class WalletNotFoundException extends JbankException {

    private final String details;

    public WalletNotFoundException(String details) {
        super(details);
        this.details = details;
    }

    @Override
    public ProblemDetail problemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        pb.setTitle("Wallet not found");
        pb.setDetail(details);

        return pb;
    }
}
