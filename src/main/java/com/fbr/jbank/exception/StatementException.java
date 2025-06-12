package com.fbr.jbank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class StatementException extends JbankException {

    private final String details;

    public StatementException(String details) {
        super(details);
        this.details = details;
    }

    @Override
    public ProblemDetail problemDetail() {
        var pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        pd.setTitle("Invalid statement scenario");
        pd.setDetail(details);

        return pd;
    }
}
