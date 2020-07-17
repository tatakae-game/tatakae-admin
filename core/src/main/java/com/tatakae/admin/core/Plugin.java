package com.tatakae.admin.core;

public interface Plugin {
    void start();

    String getMainViewName();

    String getName();

    String getDescription();

    void startCLI();
}
