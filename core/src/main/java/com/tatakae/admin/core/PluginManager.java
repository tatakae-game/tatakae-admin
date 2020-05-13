package com.tatakae.admin.core;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;

import com.tatakae.admin.core.Exceptions.FailedLoadingPluginException;

public class PluginManager {
    ArrayList<Plugin> plugins = new ArrayList<>();

    public Plugin load(final String path) throws FailedLoadingPluginException {
        try {
            final var url = new URL[] { new URL("file:///" + path) };
            final var child = new URLClassLoader(url, PluginManager.class.getClassLoader());

            final var plugin = Class.forName("Extension", true, child);
            final var extension = (Plugin) plugin.getDeclaredConstructor().newInstance();

            plugins.add(extension);

            return extension;
        } catch (Exception e) {
            throw new FailedLoadingPluginException(
                    "Failed to load the plugin at path '" + path + "': " + e.getMessage());
        }
    }
}
