package com.vlad.ioc.exception;

public class NotUniqueBeanException extends RuntimeException {
    public NotUniqueBeanException(String message) {
        super(message);
    }
}
