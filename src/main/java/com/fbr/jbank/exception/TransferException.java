package com.fbr.jbank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class TransferException extends JbankException {
    private final String detail;

    public TransferException(String detail) {
        super(detail);
        this.detail = detail;
    }

    @Override
    public ProblemDetail problemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);

        pd.setTitle("Transfer not allowed");
        pd.setDetail(detail);

        return pd;
    }
}
