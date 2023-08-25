package com.softserve.edu.jom.exception;

public class DuplicateUserEmailException extends RuntimeException {
    public DuplicateUserEmailException() {
        super();
    }

    public DuplicateUserEmailException(String message) {
        super(message);
    }

    public DuplicateUserEmailException(String message, Throwable cause) {
        super(message, cause);
    }
}
