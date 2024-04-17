package com.example.carassistant.core;

public final class Error extends Result {
    private final String message;

    public Error(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
