package com.tatakae.admin.core.Exceptions;

@SuppressWarnings("serial")
public class FailedParsingJsonException extends Exception {
    public FailedParsingJsonException(String message) {
        super(message);
    }
}
