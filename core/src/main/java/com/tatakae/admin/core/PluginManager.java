package com.tatakae.admin.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.tatakae.admin.core.Exceptions.CannotCreateFileException;
import com.tatakae.admin.core.Exceptions.FailedLoadingPluginException;

public class PluginManager {
    public List<PluginEnvironment> environmentsActive = new ArrayList<>();
    public List<PluginEnvironment> environmentsInactive = new ArrayList<>();
    public Map<PluginEnvironment, String> environments = new HashMap<>();

    public PluginManager() {
        this.loadPlugins();
    }

    public void startPlugins() {
        this.environmentsActive.forEach(env -> env.getPlugin().start());
    }

    private void loadPlugins() {
        this.environmentsActive = getEnvironments("active");
        this.environmentsInactive = getEnvironments("inactive");

        initEnvironments();
    }

    private void initEnvironments() {
        environments.clear();
        for (final var env : environmentsActive) {
            environments.put(env, "Disable");
        }

        for (final var env : environmentsInactive) {
            environments.put(env, "Enable");
        }
    }

    private PluginEnvironment loadPlugin(final Path path) throws FailedLoadingPluginException {
        try {
            final var url = new URL[] { new URL("file:///" + path) };
            final var loader = new URLClassLoader(url, PluginManager.class.getClassLoader());

            final var pluginClass = Class.forName("Extension", true, loader);
            final var plugin = (Plugin) pluginClass.getDeclaredConstructor().newInstance();

            return new PluginEnvironment(loader, plugin);

        } catch (Exception e) {
            throw new FailedLoadingPluginException(
                    "Failed to load the plugin at path '" + path + "': " + e.getMessage());
        }
    }

    private List<PluginEnvironment> getEnvironments(final String name) {
        try {
            final var pluginsDirectory = LocalDataManager.getDirectory("plugins/" + name);

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

            final var res = environments.filter(Objects::nonNull).collect(Collectors.toList());
            paths.close();

            return res;

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean importPlugin(final File file) {
        try {
            loadPlugin(file.toPath());

            final var pluginDirectory = LocalDataManager.getDirectory("plugins/inactive").toPath();
            final var copiedFile = Paths.get(pluginDirectory + "/" + file.getName());

            Files.copy(file.toPath(), copiedFile, StandardCopyOption.REPLACE_EXISTING);

            loadPlugins();

            return Files.exists(copiedFile);

        } catch (CannotCreateFileException | IOException | FailedLoadingPluginException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
}