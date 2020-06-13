package com.tatakae.admin.core;

import java.net.URL;

public interface Plugin {
    public void start();

    public URL getView();

    public String getName();
}
