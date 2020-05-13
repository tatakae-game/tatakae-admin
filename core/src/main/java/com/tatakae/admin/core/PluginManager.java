package com.tatakae.admin.core;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tatakae.admin.core.Exceptions.CannotCreatePluginsDirectoryException;
import com.tatakae.admin.core.Exceptions.FailedLoadingPluginException;

import net.harawata.appdirs.AppDirsFactory;

public class PluginManager {
    public List<Plugin> plugins = new ArrayList<>();

    public PluginManager() {
        this.loadPlugins();
    }

    public void startPlugins() {
        this.plugins.forEach(plugin -> plugin.start());
    }

    private void loadPlugins() {
        try {
            final var pluginsDirectory = this.getPluginsDirectory();

            Stream<Path> paths = Files.walk(pluginsDirectory);

            final var plugins = paths.filter(Files::isRegularFile).map((path) -> {
                try {
                    return this.loadPlugin(path);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();

                    return null;
                }
            });

            this.plugins = plugins.filter(Objects::nonNull).collect(Collectors.toList());
            paths.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Path getPluginsDirectory() throws CannotCreatePluginsDirectoryException {
        final var pluginsDirectory = this.getDataDirectory().resolve("plugins");
        final var pluginsDirectoryFile = pluginsDirectory.toFile();

        if (!pluginsDirectoryFile.exists()) {
            if (!pluginsDirectoryFile.mkdirs()) {
                throw new CannotCreatePluginsDirectoryException("Can't create plugins directory.");
            }
        }

        return pluginsDirectory;
    }

    private Path getDataDirectory() {
        final var appDirs = AppDirsFactory.getInstance();
        final var dataDirectory = appDirs.getUserConfigDir("tatakae", null, "tatakae-admin");

        return Paths.get(dataDirectory);
    }

    private Plugin loadPlugin(final Path path) throws FailedLoadingPluginException {
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