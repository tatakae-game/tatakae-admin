package com.tatakae.plugins.boostrap;

import java.net.URL;

import com.tatakae.admin.core.Plugin;

public class Extension implements Plugin {
    public void start() {
        System.out.println("test");
    }

    public URL getView() {
        return getClass().getResource("/view.fxml");
    }
}
