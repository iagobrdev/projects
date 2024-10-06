package br.com.projects.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {

    private final String description;
    private final HttpStatus status;

    public BusinessException(String message, String description, HttpStatus status) {
        super(message);
        this.description = description;
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public HttpStatus getStatus() {
        return status;
    }
}