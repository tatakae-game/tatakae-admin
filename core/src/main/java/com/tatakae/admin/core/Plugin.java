package com.tatakae.admin.core;

public interface Plugin {
    public void start();

    public String getMainViewName();

    public String getName();

    public String getDescription();
}
