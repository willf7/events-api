package com.events.api.exceptions;

public class FileUploadException extends RuntimeException {
    public FileUploadException(Throwable cause) {
        super("Unable to upload the file. Please try again.", cause);
    }
}
