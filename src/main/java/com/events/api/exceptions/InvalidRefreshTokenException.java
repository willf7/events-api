package com.events.api.exceptions;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException() {
        super("Unauthorized");
    }
}
