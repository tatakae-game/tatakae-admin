package com.tatakae.admin.core;

import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tatakae.admin.core.Exceptions.FailedLoadingPluginException;

public class PluginManager {
    public List<PluginEnvironment> environments = new ArrayList<>();

    public PluginManager() {
        this.loadPlugins();
    }

    public void startPlugins() {
        this.environments.forEach(env -> env.getPlugin().start());
    }

    private void loadPlugins() {
        try {
            final var pluginsDirectory = LocalDataManager.getDirectory("plugins");

            Stream<Path> paths = Files.walk(pluginsDirectory.toPath());

            final var environments = paths.filter(Files::isRegularFile).map((path) -> {
                try {
                    return this.loadPlugin(path);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    e.printStackTrace();

                    return null;
                }
            });

            this.environments = environments.filter(Objects::nonNull).collect(Collectors.toList());
            paths.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private PluginEnvironment loadPlugin(final Path path) throws FailedLoadingPluginException {
        try {
            final var url = new URL[] { new URL("file:///" + path) };
            final var loader = new URLClassLoader(url, PluginManager.class.getClassLoader());

            final var pluginClass = Class.forName("Extension", true, loader);
            final var plugin = (Plugin) pluginClass.getDeclaredConstructor().newInstance();

            final var environment = new PluginEnvironment(loader, plugin);

            this.environments.add(environment);

            return environment;
        } catch (Exception e) {
            throw new FailedLoadingPluginException(
                    "Failed to load the plugin at path '" + path + "': " + e.getMessage());
        }
    }
}