package com.richards.mealsapp.exceptions;

public class UnauthorizedException  extends RuntimeException{
    private String debugMessage;

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(String message, String debugMessage) {
        super(message);
        this.debugMessage = debugMessage;
    }
}
