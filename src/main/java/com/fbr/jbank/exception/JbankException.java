package com.fbr.jbank.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public abstract class JbankException extends RuntimeException {
    public JbankException(String message) {
        super(message);
    }

    public JbankException(Throwable cause) {
        super(cause);
    }

    public ProblemDetail problemDetail() {
        var pb = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        pb.setTitle("Jbank Internal Server Error");
        pb.setDetail("Contact Jbank support");
        return pb;
    }
}
