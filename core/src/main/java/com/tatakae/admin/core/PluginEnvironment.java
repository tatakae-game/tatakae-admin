package com.tatakae.admin.core;

import java.net.URL;
import java.net.URLClassLoader;

public class PluginEnvironment {
    private URLClassLoader loader;
    private Plugin plugin;

    PluginEnvironment(URLClassLoader loader, Plugin plugin) {
        this.loader = loader;
        this.plugin = plugin;
    }

    public URL getResource(final String name) {
        return loader.getResource(name);
    }

    public Plugin getPlugin() {
        return plugin;
    }
}