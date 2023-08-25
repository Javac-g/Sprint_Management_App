package com.softserve.edu.jom.exception;

public class MarathonIsNotEmptyException extends RuntimeException {
    public MarathonIsNotEmptyException() {
        super();
    }

    public MarathonIsNotEmptyException(String message) {
        super(message);
    }

    public MarathonIsNotEmptyException(String message, Throwable cause) {
        super(message, cause);
    }
}
