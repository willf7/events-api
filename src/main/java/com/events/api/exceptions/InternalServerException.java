package com.events.api.exceptions;

public class InternalServerException extends RuntimeException {
    public InternalServerException() {
        super("An unexpected error occurred while processing your request. Please try again later.");
    }
}
