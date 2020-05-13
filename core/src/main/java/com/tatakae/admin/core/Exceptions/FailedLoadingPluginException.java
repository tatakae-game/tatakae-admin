package com.tatakae.admin.core.Exceptions;

@SuppressWarnings("serial")
public class FailedLoadingPluginException extends Exception {
    public FailedLoadingPluginException(String message) {
        super(message);
    }
}