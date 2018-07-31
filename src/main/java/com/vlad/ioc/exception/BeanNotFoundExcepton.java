package com.vlad.ioc.exception;

public class BeanNotFoundExcepton extends RuntimeException {
    public BeanNotFoundExcepton(String message) {
        super(message);
    }

    public BeanNotFoundExcepton(String message, Throwable cause) {
        super(message, cause);
    }
}
