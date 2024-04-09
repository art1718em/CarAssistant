package com.example.carassistant.core;

public final class Success<T> extends Result {
    private T data = null;

    public T getData() {
        return data;
    }

    public Success(T data) {
        this.data = data;
    }

    public Success() {
    }
}
