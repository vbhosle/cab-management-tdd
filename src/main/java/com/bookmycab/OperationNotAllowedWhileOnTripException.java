package com.bookmycab;

public class OperationNotAllowedWhileOnTripException extends RuntimeException{
    public OperationNotAllowedWhileOnTripException(String message) {
        super(message);
    }
}
